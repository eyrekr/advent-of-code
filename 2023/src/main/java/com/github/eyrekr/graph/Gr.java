package com.github.eyrekr.graph;

import com.github.eyrekr.common.Indexed;
import com.github.eyrekr.mutable.Arr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * For sparse graphs.
 */
public final class Gr<T> {
    private final Arr<V<T>> vertices = Arr.empty();
    private final Arr<E<T>> edges = Arr.empty();
    private final Map<T, V<T>> map = new HashMap<>();

    private Gr() {
    }

    public static <E> Gr<E> empty() {
        return new Gr<>();
    }

    public Gr<T> addVertex(final T t, final String alias) {
        vertex(t, alias);
        return this;
    }

    private V<T> vertex(final T t, final String alias) {
        return map.computeIfAbsent(t, key -> {
            final int i = vertices.length();
            final V<T> v = new V<>(i, t, alias);
            vertices.addLast(v);
            return v;
        });
    }

    public Gr<T> addEdge(final T a, final T b) {
        edge(a, b, 1);
        return this;
    }

    public Gr<T> addEdge(final T a, final T b, final int weight) {
        edge(a, b, weight);
        return this;
    }

    private E<T> edge(final T a, final T b, final int weight) {
        final V<T> u = vertex(a, null), v = vertex(b, null);
        final int i = edges.length();
        final E<T> edge = new E<T>(i, u, v, weight);
        edges.addLast(edge);
        v.in.addLast(edge);
        u.out.addLast(edge);
        return edge;
    }

    public long distance_BellmanFordMoore(final T a, final T b) {
        final Arr<Integer> d = Arr.repeat(Integer.MAX_VALUE, vertices.length());
        final Arr<State> state = Arr.repeat(State.Unseen, vertices.length());
        final V<T> u = map.get(a), v = map.get(b);
        d.set(u, 0);
        state.set(u, State.Open);
        final Arr<V<T>> open = Arr.of(u);
        while (open.isNotEmpty()) {
            final V<T> w = open.removeFirst();
            state.set(w, State.Closed);
            for (final E<T> e : w.out)
                if (d.at(e.v) > d.at(e.u) + e.weight) {
                    d.set(e.v, d.at(e.u) + e.weight);
                    state.set(e.v, State.Open);
                    open.addLast(e.v);
                }
        }
        return d.at(v);
    }

    private static final class V<T> implements Indexed {
        final int i;
        final T t;
        final String alias;
        private final Arr<E<T>> in = Arr.empty(), out = Arr.empty();

        private V(final int i, final T t, final String alias) {
            this.i = i;
            this.t = t;
            this.alias = alias;
        }

        @Override
        public int i() {
            return i;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof final V that && this.i == that.i;
        }

        @Override
        public int hashCode() {
            return i;
        }

        @Override
        public String toString() {
            return alias != null ? alias : "v" + t;
        }
    }

    private static final class E<T> implements Indexed {
        final int i;
        final V<T> u, v;
        final int weight;

        private E(final int i, final V<T> u, final V<T> v, final int weight) {
            this.i = i;
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int i() {
            return i;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof final E that && this.i == that.i && this.weight == that.weight && this.v.i == that.v.i;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, weight, v.i);
        }

        @Override
        public String toString() {
            return String.format("%s--%d-->%s", u.t, weight, v.t);
        }
    }

    enum State {Unseen, Open, Closed}
}

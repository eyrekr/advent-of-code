package com.github.eyrekr.graph;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * For sparse graphs.
 */
public final class Gr<T> {
    private final Arr<V<T>> vertices = Arr.empty();
    private final Arr<E<V<T>>> edges = Arr.empty();
    private final Map<T, V<T>> map = new HashMap<>();

    public Gr<T> addVertex(final T t) {
        vertex(t);
        return this;
    }

    public Gr<T> addEdge(final T a, final T b) {
        edge(a, b, 1);
        return this;
    }

    public Gr<T> addEdge(final T a, final T b, final int weight) {
        edge(a, b, weight);
        return this;
    }

    private V<T> vertex(final T t) {
        final int i = vertices.length();
        final V<T> vertex = new V<T>(i, t);
        vertices.addLast(vertex);
        map.put(t, vertex);
        return vertex;
    }

    private E<T> edge(final T a, final T b, final int weight) {
        final V<T> u = map.computeIfAbsent(a, this::vertex), v = map.computeIfAbsent(b, this::vertex);
        final int i = edges.length();
        final E<T> edge = new E<T>(i, u, v, weight);
        v.in.addLast(edge);
        u.out.addLast(edge);
        return edge;
    }

    public long distance_BellmanFordMoore(final T a, final T b) {
        final int[] d = new int[vertices.length()];
        final State[] state = new State[vertices.length()];
        Arrays.fill(d, Integer.MAX_VALUE);
        Arrays.fill(state, State.Unseen);
        final V<T> u = map.get(a), v = map.get(b);
        d[u.i] = 0;
        state[u.i] = State.Open;
        final Arr<V<T>> open = Arr.of(u);
        while (open.isNotEmpty()) {
            final V<T>  w = open.removeFirst();
            state[w.i] = State.Closed;
            for(final E<T> e: w.out)
                if(d[e.v.i] > d[u.i] + e.weight) {
                    d[e.v.i] = d[u.i] + e.weight;
                    state[e.v.i] = State.Open;
                    open.addLast(e.v);
                }
        }
        return d[v.i];
    }

    private static final class V<T> {
        final int i;
        final T t;
        private final Arr<E<T>> in = Arr.empty(), out = Arr.empty();

        private V(final int i, final T t) {
            this.i = i;
            this.t = t;
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
            return String.valueOf(t);
        }
    }

    private static final class E<T> {
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

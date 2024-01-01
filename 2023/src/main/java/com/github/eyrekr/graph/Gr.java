package com.github.eyrekr.graph;

import com.github.eyrekr.mutable.Arr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * For sparse graphs.
 */
public final class Gr<T> {
    private final Arr<V<T>> v = Arr.empty();
    private final Arr<E<V<T>>> e = Arr.empty();
    private final Map<T, V<T>> map = new HashMap<>();

    public Gr<T> addVertex(final T t) {
        vertex(t);
        return this;
    }

    public Gr<T> addEdge(final T u, final T v) {
        edge(u, v, 1);
        return this;
    }

    public Gr<T> addEdge(final T u, final T v, final int weight) {
        edge(u, v, weight);
        return this;
    }

    private V<T> vertex(final T t) {
        final int i = v.length();
        final V<T> vertex = new V<T>(i, t);
        v.addLast(vertex);
        map.put(t, vertex);
        return vertex;
    }

    private E<T> edge(final T u, final T v, final int weight) {
        final V<T> source = map.computeIfAbsent(u, this::vertex), target = map.computeIfAbsent(v, this::vertex);
        final int i = e.length();
        final E<T> edge = new E<T>(i, source, target, weight);
        target.in.addLast(edge);
        source.out.addLast(edge);
        return edge;
    }


    public int distance(final T u, final T v) {

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
}

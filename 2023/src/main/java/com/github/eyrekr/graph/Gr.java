package com.github.eyrekr.graph;

import com.github.eyrekr.mutable.Arr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class Gr<T> {
    private final AtomicInteger n = new AtomicInteger();
    private final AtomicInteger m = new AtomicInteger();
    private final Map<T, V<T>> vertices = new HashMap<>();

    public Gr<T> addVertex(final T t) {
        vertices.put(t, new V<>(n.getAndIncrement(), t));
        return this;
    }

    public Gr<T> addEdge(final T u, final T v) {
        return addEdge(u, v, 1);
    }

    public Gr<T> addEdge(final T u, final T v, final int w) {
        final V<T> source = vertices.computeIfAbsent(u, z -> new V<>(n.getAndIncrement(), z)),
                target = vertices.computeIfAbsent(v, z -> new V<>(n.getAndIncrement(), z));
        source.out.addLast(new E<>(m.getAndIncrement(), target, w));
        target.in.addLast(new E<>(m.getAndIncrement(), source, w));
        return this;
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
        final V<T> v;
        final int w;

        private E(final int i, final V<T> v, final int w) {
            this.i = i;
            this.v = v;
            this.w = w;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof final E that && this.i == that.i && this.w == that.w && Objects.equals(this.v, that.v);
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, v, w);
        }

        @Override
        public String toString() {
            return String.format("--%d-->%s", w, v.t);
        }
    }
}

package com.github.eyrekr.graph;

import com.github.eyrekr.immutable.Arr;

import java.util.*;

public final class Gr {
    private final Map<Integer, V> vertices = new HashMap<>();

    public Gr addVertex(final int i, final String name) {
        vertices.put(i, new V(i, name));
        return this;
    }

    public Gr addEdge(final int u, final int v) {
        final V source = vertices.computeIfAbsent(u, V::new), target = vertices.computeIfAbsent(v, V::new);
        source.out.add(new E(target));
        target.in.add(new E(source));
        return this;
    }

    public Gr addEdge(final int u, final int v, final int w) {
        final V source = vertices.computeIfAbsent(u, V::new), target = vertices.computeIfAbsent(v, V::new);
        source.out.add(new E(target, w));
        target.in.add(new E(source, w));
        return this;
    }

    public int dijkstra(final int u, final int v) {
        final V source = vertices.computeIfAbsent(u, V::new), target = vertices.computeIfAbsent(v, V::new);
        final PriorityQueue<V> queue = new PriorityQueue<>(Comparator.comparing(ver))
        while (queue.isNotEmpty) {
            final V current = queue.remove();
            current.out.forEach(e->e.v.improve(current, ));

        }
    }


    static class V {
        final int i;
        final String name;
        private final Set<E> in = new HashSet<>(), out = new HashSet<>();
        private int distance = Integer.MAX_VALUE;
        private boolean visited = false;


        private V(final int i) {
            this(i, "v" + i);
        }

        private V(final int i, final String name) {
            this.i = i;
            this.name = name;
        }

        private V reset() {
            this.distance = Integer.MAX_VALUE;
            this.visited = false;
            return this;
        }

        private V improve(final V source, int w) {
            if (distance > source.distance + w) distance = source.distance + w;
            visited = true;
            return this;
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
            return name;
        }
    }

    static class E {
        final V v;
        final int w;

        E(final V v) {
            this(v, 1);
        }

        E(final V v, final int w) {
            this.v = v;
            this.w = w;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof final E that && Objects.equals(this.v, that.v);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v, w);
        }

        @Override
        public String toString() {
            return String.format("--%d-->%s", w, v.name);
        }
    }
}

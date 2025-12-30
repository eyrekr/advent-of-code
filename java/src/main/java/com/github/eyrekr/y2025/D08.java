package com.github.eyrekr.y2025;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;

class D08 {

    final Arr<Point> points;

    D08(final String input) {
        points = Arr.ofLinesFromString(input).contextMap(Point::new);
    }

    long star1(final int n) {
        points
                .prodUpperTriangleWith(points, Pair::new)
                .sortedBy(Pair::d)
                .first(n)
                .each(this::mergeCircuitsIfPossible);

        return points.toLongs(p -> p.circuit).frequencies().sorted().last(3).prod();
    }

    long star2() {
        final Arr<Pair> distinctPairsSortedByDistance = points.prodUpperTriangleWith(points, Pair::new).sortedBy(Pair::d);

        for (final Pair pair : distinctPairsSortedByDistance)
            if (mergeCircuitsIfPossible(pair)) return pair.p1.x * pair.p2.x;


        throw new IllegalStateException("unable to close the circuit");
    }

    boolean mergeCircuitsIfPossible(final Pair pair) {
        final int c1 = pair.p1.circuit, c2 = pair.p2.circuit;
        if (c1 == c2) return false; // already part of the same circuit

        boolean singleCircuit = true;
        for (final Point p : points) {
            if (p.circuit == c2) p.circuit = c1; // merge circuits
            if (p.circuit != c1) singleCircuit = false; // check if we only have one circuit
        }

        return singleCircuit;
    }

    static final class Point {
        final long x, y, z;
        int circuit;

        private Point(final String line, final int i, final boolean first, final boolean last) {
            final var c = Longs.fromString(line);
            this.x = c.at(0);
            this.y = c.at(1);
            this.z = c.at(2);
            this.circuit = i;
        }
    }

    record Pair(Point p1, Point p2, double d) {
        Pair(final Point p1, final Point p2) {
            this(p1, p2, Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z)));
        }
    }
}
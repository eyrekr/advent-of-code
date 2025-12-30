package com.github.eyrekr.y2025;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;

import java.util.Iterator;

class D08 {

    final Arr<Point> points;

    D08(final String input) {
        points = Arr.ofLinesFromString(input).contextMap(Point::deserialize);
    }

    public long star1(final int n) {
        final Arr<Long> circuits = Arr.range(0, points.length());
        points
                .prodUpperTriangleWith(points, Pair::of)
                .sortedBy(Pair::d)
                .first(n)
                .each(pair -> {
                    final long c1 = circuits.at(pair.i);
                    final long c2 = circuits.at(pair.j);
                    if (c1 != c2) circuits.replaceAll(c2, c1); // merge circuits
                });

        return Longs.fromIterable(circuits.frequencies().values()).sorted().last(3).prod();
    }

    public long star2() {
        final Arr<Long> circuits = Arr.range(0, points.length());
        final Iterator<Pair> distinctPairsSortedByDistance = points.prodUpperTriangleWith(points, Pair::of).sortedBy(Pair::d).iterator();

        boolean allJunctionBoxesFormSingleCircuit = false;
        Pair lastTwoJunctionBoxes = null;
        while (!allJunctionBoxesFormSingleCircuit && distinctPairsSortedByDistance.hasNext()) {
            final Pair pair = distinctPairsSortedByDistance.next();
            final long c1 = circuits.at(pair.i);
            final long c2 = circuits.at(pair.j);
            if (c1 != c2) {
                circuits.replaceAll(c2, c1);
                allJunctionBoxesFormSingleCircuit = circuits.allValuesAreTheSame();
                lastTwoJunctionBoxes = pair;
            }
        }

        return points.at(lastTwoJunctionBoxes.i).x * points.at(lastTwoJunctionBoxes.j).x;
    }

    record Point(int i, long x, long y, long z) {
        static Point deserialize(final String line, final int i, final boolean first, final boolean last) {
            final var c = Longs.fromString(line);
            return new Point(i, c.at(0), c.at(1), c.at(2));
        }

        double d(final Point that) {
            return Math.sqrt((this.x - that.x) * (this.x - that.x) + (this.y - that.y) * (this.y - that.y) + (this.z - that.z) * (this.z - that.z));
        }
    }

    record Pair(int i, int j, double d) {
        static Pair of(final Point p1, final Point p2) {
            return new Pair(p1.i, p2.i, p1.d(p2));
        }
    }
}
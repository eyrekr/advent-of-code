package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.output.Out;

class D08 extends Aoc {

    final Arr<Point> points;

    D08(final String input) {
        points = Arr.ofLinesFromString(input).contextMap(Point::deserialize);
    }

    @Override
    public long star1() {
        final Arr<Long> circuits = Arr.range(0, points.length());
        final Arr<Pair> pairsSortedByDistance = points
                .prodUpperTriangleWith(points, (p1, p2) -> new Pair(p1.i, p2.i, p1.d(p2)))
                .sortedBy(Pair::d);

        int matchedPairs = 0;
        for (final Pair pair : pairsSortedByDistance) {
            final long c1 = circuits.at(pair.i);
            final long c2 = circuits.at(pair.j);

//            Out.print("""
//                    %s - %s : %f  (c1=%d c2=%d)
//                    """,
//                    points.at(pair.i),
//                    points.at(pair.j),
//                    pair.d,
//                    c1,
//                    c2);

            if (c1 == c2) continue; // i-j already form a circuit

            circuits.replaceAll(c2, c1);

            System.out.println(circuits.frequencies().values().stream().sorted().toList());

            matchedPairs++;
            if (matchedPairs == 9) break;
        }

        return Longs.fromIterable(circuits.frequencies().values()).sorted().last(3).prod();
    }

    @Override
    public long star2() {
        return -1L;
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
    }
}
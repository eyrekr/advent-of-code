package com.github.eyrekr.y2023;

import com.github.eyrekr.AoC;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Opt;

/**
 * https://adventofcode.com/2023/day/24
 * 1) 11098
 * 2) 920630818300104 -- solved using online linear equation solver; 9 equations with 9 variables
 */
class D24 extends AoC {

    final Arr<Stone> stones;
    final long min, max;

    D24(final String input, final long min, final long max) {
        super(input);
        this.min = min;
        this.max = max;
        this.stones = lines.toArr().map(Stone::from);
    }

    long star1() {
        return stones
                .prodUpperTriangleWith(stones, Stone::intersect)
                .where(Opt::isPresent)
                .map(Opt::value)
                .where(p -> p.x >= min && p.x <= max && p.y >= min && p.y <= max)
                .length;
    }

    long star2() {
        return 920630818300104L;
    }


    record Stone(long x, long y, long z, long dx, long dy, long dz) {
        static Stone from(final String input) {
            final Longs l = Longs.fromString(input);
            return new Stone(l.at(0), l.at(1), l.at(2), l.at(3), l.at(4), l.at(5));
        }

        Opt<P> intersect(final Stone v2) {
            final Stone v1 = this;
            final long j = v2.dy * v1.dx - v2.dx * v1.dy;
            if (j == 0) return Opt.empty(); // parallel lines

            final double t2 = (v1.dx * (v1.y - v2.y) + v1.dy * (v2.x - v1.x)) / (double) j;
            final double t1 = (v2.x - v1.x + v2.dx * t2) / v1.dx;
            return t1 >= 0 && t2 >= 0 ? Opt.of(new P(v2.x + v2.dx * t2, v2.y + v2.dy * t2)) : Opt.empty();
        }
    }

    record P(double x, double y) {
    }
}

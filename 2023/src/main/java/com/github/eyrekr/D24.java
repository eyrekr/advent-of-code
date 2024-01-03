package com.github.eyrekr;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;

/**
 * https://adventofcode.com/2023/day/24
 * 1)
 * 2)
 */
class D24 extends AoC {

    final Seq<Stone> stones;

    D24(final String input) {
        super(input);
        this.stones = lines.map(Stone::from);
    }

    @Override
    long star1() {
        stones.
        return 0L;
    }

    @Override
    long star2() {
        return 0L;
    }


    record Stone(long x, long y, long z, long dx, long dy, long dz) {
        static Stone from(final String input) {
            final Longs l = Longs.fromString(input);
            return new Stone(l.at(0), l.at(1), l.at(2), l.at(3), l.at(4), l.at(5));
        }
    }
}

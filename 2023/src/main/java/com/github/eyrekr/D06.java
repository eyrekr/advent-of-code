package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

/**
 * https://adventofcode.com/2023/day/6
 * 1) 281600
 * 2) 33875953
 */
class D06 extends AoC {

    final Seq<Input> input;

    public D06(final Seq<Input> input) {
        super("");
        this.input = input;
    }

    @Override
    long star1() {
        return input.map(input -> waysToWin(input.time, input.distance)).reduce(1L, (acc, l) -> acc * l);
    }

    @Override
    long star2() {
        return star1();
    }

    static long waysToWin(final long T, final long R) {
        final double D = Math.sqrt(T * T - 4.0 * R), t1 = (T - D) / 2.0, t2 = (T + D) / 2.0;
        long a = (long) Math.round(t1), b = (long) Math.floor(t2);
        // correction for the integer math
        if (a * a - T * a + R >= 0) {
            a++;
        }
        if (b * b - T * b + R >= 0) {
            b--;
        }
        return b - a + 1;
    }

    record Input(long time, long distance) {
    }
}

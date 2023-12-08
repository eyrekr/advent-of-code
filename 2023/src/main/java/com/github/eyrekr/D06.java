package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

/**
 * https://adventofcode.com/2023/day/6
 * 1) 281600
 * 2) 33875953
 */
class D06 {

    static final Seq<Input> SAMPLE = Seq.of(new Input(7, 9), new Input(15, 40), new Input(30, 200));
    static final Seq<Input> STAR1 = Seq.of(new Input(47, 282), new Input(70, 1079), new Input(75, 1147), new Input(66, 1062));
    static final Seq<Input> STAR2 = Seq.of(new Input(47707566, 282107911471062L));

    record Input(long time, long distance) {
    }

    public static void main(String[] args) {
        System.out.println(STAR2.map(input -> waysToWin(input.time, input.distance)).reduce(1L, (acc, l) -> acc * l));
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
}

package com.github.eyrekr;

/**
 * <a href="https://adventofcode.com/2023/day/6">...</a>
 * 1) 281600
 * 2) 33875953
 */
class Day6 {

    static final Seq<Input> SAMPLE = Seq.of(new Input(7, 9), new Input(15, 40), new Input(30, 200));

    static final Seq<Input> STAR1 = Seq.of(new Input(47, 282), new Input(70, 1079), new Input(75, 1147), new Input(66, 1062));

    static final Seq<Input> STAR2 = Seq.of(new Input(47707566, 282107911471062L));

    record Input(long time, long distance) {
    }

    public static void main(String[] args) {
        final var prod = STAR2.map(input -> {
                    final var T = input.time;
                    final var R = input.distance;

                    final var D = Math.sqrt(T * T - 4.0 * R);
                    final double t1 = (T - D) / 2.0;
                    final double t2 = (T + D) / 2.0;

                    long a = (long) Math.round(t1), b = (long) Math.floor(t2);

                    // correction for the integer math
                    if (a * a - T * a + R >= 0) {
                        a++;
                    }
                    if (b * b - T * b + R >= 0) {
                        b--;
                    }

                    long waysToWin = b - a + 1;
                    System.out.printf("time: %3d  record:%3d  => %3d   [%3d;%3d]->[%3d, %3d]\n", T, R, waysToWin, a, b, a * a - T * a + R, b * b - T * b + R);
                    return waysToWin;
                })
                .reduce(1L, (acc, l) -> acc * l);

        System.out.println(prod);
    }
}

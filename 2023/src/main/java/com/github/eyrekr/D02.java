package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2023/day/2
 * 1) 514969
 * 2) 78915902
 */
class D02 {

    public static void main(String[] args) throws Exception {
        final Grid grid = Grid.from(Path.of("src/main/resources/D03.txt"));

        long sum = 0;
        final Multimap<Integer, Part> gears = LinkedHashMultimap.create();
        Part part = new Part();

        for (final Grid.It it : grid) {
            if (it.firstOnLine) {
                System.out.printf("%4d:  ", it.y + 1);
            }

            if (it.digit >= 0) {
                part.number = part.number * 10 + it.digit;
                part.nearSymbol = part.nearSymbol || hasSymbolAround(it);

                for (final int star : stars(it)) {
                    gears.put(star, part);
                }
            }
            if (it.digit < 0 || it.lastOnLine) {
                if (part.number > 0) {
                    System.out.printf("%s%3d\033[0m  ", part.nearSymbol ? "\033[0;32m" : "\033[0;31m", part.number);
                }
                if (part.nearSymbol && part.number > 0) {
                    sum = sum + part.number;
                }
                part = new Part();
            }
            if (it.lastOnLine) {
                System.out.println();
            }
        }

        System.out.printf(
                "SUM = %d\nGEARS = %d\n",
                sum,
                gears.asMap().values().stream()
                        .filter(collection -> collection.size() == 2)
                        .mapToInt(collection -> collection.stream().mapToInt(p -> p.number).reduce((a, b) -> a * b).getAsInt())
                        .sum());

    }

    static boolean hasSymbolAround(final Grid.It it) {
        for (final char ch : it.neighbours8) {
            if ("0123456789.\0".indexOf(ch) < 0) { // '\0' means "nothing there"
                return true;
            }
        }
        return false;
    }

    static int[] stars(final Grid.It it) {
        record P(int i, char ch) {
        }
        return Stream.of(
                        new P(it.i - it.m - 1, it.neighbours8[0]),
                        new P(it.i - it.m, it.neighbours8[1]),
                        new P(it.i - it.m + 1, it.neighbours8[2]),
                        new P(it.i - 1, it.neighbours8[3]),
                        new P(it.i + 1, it.neighbours8[4]),
                        new P(it.i + it.m - 1, it.neighbours8[5]),
                        new P(it.i + it.m, it.neighbours8[6]),
                        new P(it.i + it.m + 1, it.neighbours8[7]))
                .filter(p -> p.ch == '*')
                .mapToInt(p -> p.i)
                .toArray();
    }

    static class Part {
        int number = 0;
        boolean nearSymbol = false;
    }

}
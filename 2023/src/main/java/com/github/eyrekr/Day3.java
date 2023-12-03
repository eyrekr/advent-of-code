package com.github.eyrekr;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * <a href="https://adventofcode.com/2023/day/3">...</a>
 * 1) 514969
 * 2) 78915902
 */
class Day3 {

    public static void main(String[] args) throws Exception {
        final String[] lines = Files.readAllLines(Path.of("src/main/resources/03.txt")).toArray(String[]::new);
        long sum = 0;
        final Multimap<P, Part> gears = LinkedHashMultimap.create();

        for (int y = 0; y < lines.length; y++) {
            System.out.printf("%4d:  ", y + 1);
            Part part = new Part();

            for (int x = 0; x < lines[0].length(); x++) {
                final char ch = lines[y].charAt(x);
                final int digit = "0123456789".indexOf(ch);
                if (digit >= 0) {
                    part.addDigit(digit);
                    final Symbol[] symbols = nearbySymbols(lines, x, y);
                    part.nearSymbol = part.nearSymbol || symbols.length > 0;
                    for (final Symbol symbol : symbols) {
                        if (symbol.ch == '*') {
                            gears.put(symbol.position, part);
                        }
                    }
                } else {
                    if (part.number > 0) {
                        System.out.printf("%s%3d\033[0m  ", part.nearSymbol ? "\033[0;32m" : "\033[0;31m", part.number);
                    }
                    if (part.nearSymbol) {
                        sum = sum + part.number;
                    }
                    part = new Part();
                    part.nearSymbol = false;
                }
            }

            if (part.number > 0) {
                System.out.printf("%s%3d\033[0m  ", part.nearSymbol ? "\033[0;32m" : "\033[0;31m", part.number);
            }
            if (part.nearSymbol && part.number > 0) { // when line ends with number
                sum = sum + part.number;
            }

            System.out.println();
        }

        System.out.printf("SUM = %d\n", sum);

        // gears
        final var gearRatioSum = gears.asMap().values().stream()
                .filter(collection -> collection.size() == 2)
                .mapToInt(collection -> collection.stream().mapToInt(part -> part.number).reduce((a, b) -> a * b).getAsInt())
                .sum();
        System.out.printf("GEARS = %d\n", gearRatioSum);
    }

    static Symbol[] nearbySymbols(final String[] lines, final int x, final int y) {
        int n = lines.length;
        int m = lines[0].length();
        return Arrays.stream(P.around(x, y))
                .filter(p -> p.isValid(m, n))
                .map(p -> new Symbol(lines[p.y].charAt(p.x), p))
                .filter(symbol -> "0123456789.".indexOf(symbol.ch) < 0)
                .toArray(Symbol[]::new);
    }

    record Symbol(char ch, P position) {
    }

    static class Part {
        int number = 0;
        boolean nearSymbol = false;

        Part addDigit(final int digit) {
            number = number * 10 + digit;
            return this;
        }
    }

    record P(int x, int y) {
        boolean isValid(final int m, final int n) {
            return x >= 0 && y >= 0 && x < m && y < n;
        }

        static P[] around(final int x, final int y) {
            return new P[]{
                    new P(x - 1, y - 1),
                    new P(x, y - 1),
                    new P(x + 1, y - 1),
                    new P(x - 1, y),
                    new P(x + 1, y),
                    new P(x - 1, y + 1),
                    new P(x, y + 1),
                    new P(x + 1, y + 1)
            };
        }
    }

    static final String SAMPLE = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.581
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;
}
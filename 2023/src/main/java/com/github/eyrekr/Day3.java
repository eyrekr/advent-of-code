package com.github.eyrekr;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

//516986 too high
class Day3 {

    public static void main(String[] args) throws Exception {
        final String[] lines = Files.readAllLines(Path.of("src/main/resources/03.txt")).toArray(String[]::new);
        long sum = 0;

        for (int y = 0; y < lines.length; y++) {
            int number = 0;
            boolean isPart = false;

            System.out.printf("%4d:  ", y + 1);

            for (int x = 0; x < lines[0].length(); x++) {
                char ch = lines[y].charAt(x);
                int digit = "0123456789".indexOf(ch);
                if (digit >= 0) {
                    number = number * 10 + digit;
                    if (!isPart) {
                        isPart = isSymbolNearby(lines, x, y);
                    }
                } else {
                    if (number > 0) {
                        System.out.printf("%s%3d\033[0m  ", isPart ? "\033[0;32m" : "\033[0;31m", number);
                    }
                    if (isPart) {
                        sum = sum + number;
                    }
                    number = 0;
                    isPart = false;
                }
            }

            if (number > 0) {
                System.out.printf("%s%3d\033[0m  ", isPart ? "\033[0;32m" : "\033[0;31m", number);
            }
            if (isPart && number > 0) { // when line ends with number
                sum = sum + number;
            }

            System.out.println();
        }

        System.out.printf("SUM = %d\n", sum);
    }

    static boolean isSymbolNearby(String[] lines, int x, int y) {
        int n = lines.length;
        int m = lines[0].length();
        return Arrays.stream(P.around(x, y))
                .filter(p -> p.isValid(m, n))
                .map(p -> lines[p.y].charAt(p.x))
                .filter(ch -> "0123456789.".indexOf(ch) < 0)
                .count() > 0;
    }

    record P(int x, int y) {
        boolean isValid(int m, int n) {
            return x >= 0 && y >= 0 && x < m && y < n;
        }

        static P[] around(int x, int y) {
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

    static final String SAMPLE_INPUT = """
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


    static final String INPUT = """

            """;
}
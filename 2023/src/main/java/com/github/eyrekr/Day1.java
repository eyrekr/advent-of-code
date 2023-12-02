package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class Day1 {

    public static void main(String[] args) throws Exception {
        final String input = Files.readString(Path.of("src/main/resources/01.txt"));

        long sum = 0;
        for (final String line : StringUtils.split(input)) {
            final Result control = Transition.translate(line);
            sum += control.value;
            System.out.printf("%-60s%10d%20d%10d\n", line, control.number, control.number, control.value);
        }

        System.out.printf("SUM = %d\n", sum /*54518*/);
    }

    record Transition(int from, char over, int to, int emit) {

        private static final Transition RESET = new Transition(0, '*', 0);

        private Transition(final int from, final char over, final int to) {
            this(from, over, to, Integer.MIN_VALUE);
        }

        private static Transition find(final int from, final char over) {
            for (final var t : TABLE) {
                if (t.from == from && t.over == over) {
                    return t;
                }
            }
            //reset to state 0 and continue from there
            for (final var t : TABLE) {
                if (t.from == 0 && t.over == over) {
                    return t;
                }
            }
            // no such transition
            return RESET;
        }

        static Result translate(final String input) {
            int state = 0;
            int number = 0;
            int hi = 0;
            int lo = 0;
            for (final char ch : input.toCharArray()) {
                final var t = find(state, ch);
                if (t.emit >= 0) {
                    number = number * 10 + t.emit;
                    if (hi == 0) {
                        hi = t.emit;
                        lo = t.emit;
                    } else {
                        lo = t.emit;
                    }
                }
                state = t.to;
            }
            return new Result(number, hi * 10 + lo);
        }
    }

    record Result(int number, int value) {
    }

    private static final Transition[] TABLE = new Transition[]{
            new Transition(0, '1', 0, 1),
            new Transition(0, '2', 0, 2),
            new Transition(0, '3', 0, 3),
            new Transition(0, '4', 0, 4),
            new Transition(0, '5', 0, 5),
            new Transition(0, '6', 0, 6),
            new Transition(0, '7', 0, 7),
            new Transition(0, '8', 0, 8),
            new Transition(0, '9', 0, 9),
            new Transition(0, 'o', 1),
            new Transition(0, 't', 4),
            new Transition(0, 's', 16),
            new Transition(0, 'e', 9),
            new Transition(0, 'f', 10),
            new Transition(0, 'n', 22),
            new Transition(1, 'n', 2),
            new Transition(2, 'e', 9, 1),
            new Transition(2, 'i', 23),
            new Transition(4, 'w', 5),
            new Transition(4, 'h', 6),
            new Transition(5, 'o', 1, 2),
            new Transition(6, 'r', 7),
            new Transition(7, 'e', 8),
            new Transition(8, 'e', 9, 3),
            new Transition(8, 'i', 25),
            new Transition(9, 'i', 25),
            new Transition(10, 'i', 14),
            new Transition(10, 'o', 11),
            new Transition(11, 'u', 12),
            new Transition(11, 'n', 2),
            new Transition(12, 'r', 0, 4),
            new Transition(14, 'v', 15),
            new Transition(15, 'e', 9, 5),
            new Transition(16, 'i', 17),
            new Transition(16, 'e', 19),
            new Transition(17, 'x', 0, 6),
            new Transition(19, 'v', 20),
            new Transition(19, 'i', 25),
            new Transition(20, 'e', 21),
            new Transition(21, 'n', 22, 7),
            new Transition(21, 'i', 25),
            new Transition(22, 'i', 23),
            new Transition(23, 'n', 24),
            new Transition(24, 'i', 23),
            new Transition(24, 'e', 9, 9),
            new Transition(25, 'g', 26),
            new Transition(26, 'h', 27),
            new Transition(27, 't', 4, 8),
    };
}

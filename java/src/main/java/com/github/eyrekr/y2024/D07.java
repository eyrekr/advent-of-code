package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;

class D07 extends Aoc {

    final Arr<Equation> equations;

    D07(final String input) {
        equations = Arr.ofLinesFromString(input).map(Equation::fromString);
    }

    @Override
    public long star1() {
        return equations
                .where(Equation::solvable)
                .mapToLongs(Equation::value)
                .sum();
    }

    record Equation(long value, Longs numbers) {
        static Equation fromString(final String line) {
            final var longs = Longs.fromString(line);
            return new Equation(longs.peek(), longs.removeFirst());
        }

        boolean solvable() {
            return solve(value, numbers.peek(), numbers.removeFirst());
        }
    }

    private static boolean solve(final long expected, final long actual, final Longs numbers) {
        if (numbers.isEmpty) return expected == actual;
        if (expected < actual) return false;
        final var head = numbers.peek();
        final var rest = numbers.removeFirst();
        return solve(expected, actual * head, rest) || solve(expected, actual + head, rest);
    }
}
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
                .where(equation -> solve1(equation.value, equation.numbers.peek(), equation.numbers.removeFirst()))
                .mapToLongs(Equation::value)
                .sum();
    }

    @Override
    public long star2() {
        return equations
                .where(equation -> solve2(equation.value, equation.numbers.peek(), equation.numbers.removeFirst()))
                .mapToLongs(Equation::value)
                .sum();
    }

    record Equation(long value, Longs numbers) {
        static Equation fromString(final String line) {
            final var longs = Longs.fromString(line);
            return new Equation(longs.peek(), longs.removeFirst());
        }
    }

    static boolean solve1(final long expected, final long accumulator, final Longs numbers) {
        if (numbers.isEmpty) return expected == accumulator;
        if (expected < accumulator) return false;

        final var head = numbers.peek();
        final var rest = numbers.removeFirst();
        return solve1(expected, accumulator * head, rest) || solve1(expected, accumulator + head, rest);
    }

    static boolean solve2(final long expected, final long accumulator, final Longs numbers) {
        if (numbers.isEmpty) return expected == accumulator;
        if (expected < accumulator) return false;

        final var head = numbers.peek();
        final var rest = numbers.removeFirst();
        return solve2(expected, accumulator * head, rest)
                || solve2(expected, accumulator + head, rest)
                || solve2(expected, concat(accumulator, head), rest);
    }

    static long concat(final long a, final long b) { // 17|520 -> 17520
        long multiplier = 1, remainder = b >= 0 ? b : -b;
        while (remainder > 0) {
            multiplier *= 10;
            remainder /= 10;
        }
        return a * multiplier + b;
    }
}
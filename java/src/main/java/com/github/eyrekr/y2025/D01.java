package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;

class D01 extends Aoc {

    static final long N = 100L;
    static final Int RANGE = new Int(0, N);
    final Longs rotations;

    D01(final String input) {
        rotations = Seq.ofLinesFromString(input).toLongs(D01::rotations);
    }

    @Override
    public long star1() {
        return rotations.reduce(Dial.INITIAL, Dial::turnAndCountWhereDialEndsAtZero).count;
    }

    @Override
    public long star2() {
        return rotations.reduce(Dial.INITIAL, Dial::turnAndCountWhereDialCrossesZero).count;
    }

    static long rotations(final String line) {
        long value = 0;
        final char[] characters = line.toCharArray();
        for (final char ch : characters)
            if (Character.isDigit(ch)) value = 10 * value + Character.digit(ch, 10);
        return switch (characters[0]) {
            case 'R', 'r' -> value;
            case 'L', 'l' -> -value;
            default -> throw new IllegalStateException();
        };
    }

    record Dial(long state, long count) {
        static Dial INITIAL = new Dial(50L, 0L);

        Dial turnAndCountWhereDialEndsAtZero(final long rotation) {
            return turn(rotation, 0);
        }

        Dial turnAndCountWhereDialCrossesZero(final long rotation) {
            final long fullCycles = Math.abs(rotation) / N;
            final long extraCrossingNotEndingAtZero = state != 0 && RANGE.notContains(state + rotation % N) ? 1 : 0;
            return turn(rotation, fullCycles + extraCrossingNotEndingAtZero);
        }

        Dial turn(final long rotation, final long extra) {
            final long result = (state + rotation % N + N) % N; // result is guaranteed to be always 0..99
            return new Dial(result, count + (result == 0 ? 1 : 0) + extra);
        }
    }
}
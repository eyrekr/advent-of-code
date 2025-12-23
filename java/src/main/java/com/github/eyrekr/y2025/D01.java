package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;

class D01 extends Aoc {

    static final long N = 100L;
    final Seq<Rotation> rotations;

    D01(final String input) {
        rotations = Seq.ofLinesFromString(input).map(Rotation::from);
    }

    @Override
    public long star1() {
        return rotations.reduce(Dial.INITIAL, Dial::turnAndCountWhereDialEndsAtZero).count;
    }

    @Override
    public long star2() {
        return rotations.reduce(Dial.INITIAL, Dial::turnAndCountWhereDialCrossesZero).count;
    }


    enum Direction {
        Left(-1), Right(+1);

        final long δ;

        Direction(long δ) {
            this.δ = δ;
        }

        static Direction from(final char ch) {
            return switch (ch) {
                case 'R', 'r' -> Right;
                case 'L', 'l' -> Left;
                default -> throw new IllegalStateException();
            };
        }
    }

    record Rotation(Direction direction, long value) {
        static Rotation from(final String line) {
            long value = 0;
            final char[] characters = line.toCharArray();
            for (final char ch : characters)
                if (Character.isDigit(ch)) value = 10 * value + Character.digit(ch, 10);
            return new Rotation(Direction.from(characters[0]), value);
        }
    }

    record Dial(long value, long count) {
        static Dial INITIAL = new Dial(50L, 0L);

        Dial turnAndCountWhereDialEndsAtZero(final Rotation rotation) {
            final long result = (value + rotation.value * rotation.direction.δ) % N;
            return new Dial(result, result == 0 ? this.count + 1 : this.count);
        }

        Dial turnAndCountWhereDialCrossesZero(final Rotation rotation) {
            long state = value, crossings = 0;
            for (int i = 0; i < rotation.value; i++) {
                state = (state + rotation.direction.δ);
                if (state >= N) state = state - N;
                if (state < 0) state = N + state;
                if (state == 0) crossings++;
            }
            return new Dial(state, count + crossings);
        }
    }
}
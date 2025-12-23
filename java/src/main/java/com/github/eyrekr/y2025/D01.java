package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;

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

        final long delta;

        Direction(final long delta) {
            this.delta = delta;
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

        long apply(final long state) { // output guaranteed to be 0..99
            return (state + (value % N) * direction.delta + N) % N;
        }
    }

    record Dial(long value, long count) {
        static Dial INITIAL = new Dial(50L, 0L);

        Dial turnAndCountWhereDialEndsAtZero(final Rotation rotation) {
            final long result = rotation.apply(value);
            return new Dial(result, count + (result == 0 ? 1 : 0));
        }

        Dial turnAndCountWhereDialCrossesZero(final Rotation rotation) {
            final long t = value + (rotation.value % N) * rotation.direction.delta;
            final long fullCycles = rotation.value / N;
            final long extraCycles = (value != 0 && (t <= 0 || t >= N) ? 1 : 0); // is the remainder out of bounds?
            return new Dial(rotation.apply(value), count + fullCycles + extraCycles);
        }
    }
}
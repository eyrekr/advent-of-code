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

    record Rotation(Direction direction, long value, long fullCycles) {
        static Rotation from(final String line) {
            long value = 0;
            final char[] characters = line.toCharArray();
            for (final char ch : characters)
                if (Character.isDigit(ch)) value = 10 * value + Character.digit(ch, 10);
            return new Rotation(Direction.from(characters[0]), value % N, value / N);
        }

        long apply(final long state) { // output guaranteed to be 0..99
            return (state + value * direction.delta + N) % N;
        }
    }

    record Dial(long state, long count) {
        static Dial INITIAL = new Dial(50L, 0L);

        Dial turnAndCountWhereDialEndsAtZero(final Rotation rotation) {
            return to(rotation.apply(state), 0);
        }

        Dial turnAndCountWhereDialCrossesZero(final Rotation rotation) {
            final long extraCycles = switch (rotation.direction) {
                case Left -> state - rotation.value < 0 && state != 0 ? 1 : 0;
                case Right -> state + rotation.value > N && state != 0 ? 1 : 0;
            };
            return to(rotation.apply(state), rotation.fullCycles + extraCycles);
        }

        Dial to(final long result, final long extra) {
            return new Dial(result, count + extra + (result == 0 ? 1 : 0));
        }
    }
}
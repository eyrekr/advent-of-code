package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;

class D01 extends Aoc {

    static final long N = 100L;
    static final long DIAL_START_POSITION = 50L;
    final Longs rotations;

    D01(final String input) {
        rotations = Seq.ofLinesFromString(input).toLongs(D01::rotation);
    }

    @Override
    public long star1() {
        return rotations.reduce(
                        Longs.of(DIAL_START_POSITION),
                        (acc, value) -> acc.addLast((acc.getLast() + value) % N))
                .count(0L);
    }

    @Override
    public long star2() {
        return -1L;
    }

    static long rotation(final String line) {
        long value = 0;
        final char[] characters = line.toCharArray();
        for (final char ch : characters)
            if (Character.isDigit(ch)) value = 10 * value + Character.digit(ch, 10);
        return characters[0] == 'R' ? (value % N) : -(value % N);
    }
}
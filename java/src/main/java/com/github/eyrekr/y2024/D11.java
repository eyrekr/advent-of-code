package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.math.Algebra;

import java.util.HashMap;
import java.util.Map;

class D11 extends Aoc {

    final Longs stones;

    D11(final String input) {
        stones = Longs.fromString(input);
    }

    @Override
    public long star1() {
        var line = new Line(stones.frequency());
        for (int i = 0; i < 25; i++) line = line.blink();
        return line.sum;
    }

    @Override
    public long star2() {
        var line = new Line(stones.frequency());
        for (int i = 0; i < 75; i++) line = line.blink();
        return line.sum;
    }

    static Longs blink(final long n) {
        if (n == 0) return Longs.of(1L);
        final int d = Algebra.decimalDigits(n);
        if (d % 2 == 1) return Longs.of(n * 2024);
        final long power = Algebra.E10[d / 2];
        return Longs.of(n / power, n % power);
    }

    static class Line {
        final Map<Long, Long> stoneCount;
        long sum = 0L;

        Line(final Map<Long, Long> stoneCount) {
            this.stoneCount = stoneCount;
        }

        Line blink() {
            final Line line = new Line(new HashMap<>());
            stoneCount.forEach(line::stoneChange);
            return line;
        }

        void stoneChange(final long value, final long count) {
            if (value == 0) update(1, count);
            else {
                final int d = Algebra.decimalDigits(value);
                if (d % 2 == 1) update(value * 2024, count);
                else {
                    final long power = Algebra.E10[d / 2];
                    update(value / power, count);
                    update(value % power, count);
                }
            }
        }

        void update(final long value, final long increment) {
            sum += increment;
            final long count = stoneCount.getOrDefault(value, 0L) + increment;
            stoneCount.put(value, count);
        }
    }
}
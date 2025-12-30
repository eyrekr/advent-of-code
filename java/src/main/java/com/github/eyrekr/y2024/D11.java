package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.math.Algebra;

import java.util.HashMap;
import java.util.Map;

class D11 extends Aoc {

    final Line line;

    D11(final String input) {
        final var stones = Longs.fromString(input);
        line = new Line(stones.histogram());
    }

    @Override
    public long star1() {
        for (int i = 0; i < 25; i++) line.blink();
        return line.sum;
    }

    @Override
    public long star2() {
        for (int i = 0; i < 75; i++) line.blink();
        return line.sum;
    }

    static class Line {
        Map<Long, Long> stoneCount;
        long sum = 0L;

        Line(final Map<Long, Long> stoneCount) {
            this.stoneCount = stoneCount;
        }

        Line blink() {
            final Line line = new Line(new HashMap<>());
            stoneCount.forEach(line::changeOneStone);
            stoneCount = line.stoneCount;
            sum = line.sum;
            return this;
        }

        void changeOneStone(final long value, final long count) {
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
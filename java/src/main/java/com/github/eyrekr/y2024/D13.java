package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

class D13 extends Aoc {

    static final Int domain = new Int(0, 100);
    final Arr<ClawMachine> clawMachines;

    D13(final String input) {
        final var definitions = StringUtils.splitByWholeSeparator(input, "\n\n");
        clawMachines = Arr.fromArray(definitions).map(ClawMachine::fromString);
    }

    @Override
    public long star1() {
        return clawMachines
                .mapToLongs(ClawMachine::price)
                .where(price -> price > 0)
                .sum();
    }

    record ClawMachine(long ax, long ay, long bx, long by, long x, long y) {
        static ClawMachine fromString(final String definition) {
            final var l = Longs.fromString(definition);
            return new ClawMachine(l.at(0), l.at(1), l.at(2), l.at(3), l.at(4), l.at(5));
        }

        long price() {
            final long d = ax * by - ay * bx;
            if (d == 0)
                throw new IllegalStateException(String.format("A[%d,%d]  B=[%d,%d] are colinear", ax, ay, bx, by));
            final long b = (y * ax - x * ay) / d, a = (x - b * bx) / ax;
            if (a * ax + b * bx != x || a * ay + b * by != y) return -1; // solution does not exist
            return domain.contains(a) && domain.contains(b) ? 3 * a + b : -1;
        }
    }

}
package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

class D13 extends Aoc {

    final Int domain = new Int(0, 100);
    final long shift = 10_000_000_000_000L;
    final Arr<ClawMachine> clawMachines;

    D13(final String input) {
        final var definitions = StringUtils.splitByWholeSeparator(input, "\n\n");
        clawMachines = Arr.fromArray(definitions).map(ClawMachine::fromString);
    }

    @Override
    public long star1() {
        return clawMachines
                .map(ClawMachine::countPresses)
                .where(press -> domain.contains(press.a) && domain.contains(press.b))
                .mapToLongs(Press::price)
                .sum();
    }

    @Override
    public long star2() {
        return clawMachines
                .map(machine -> machine.shifted(shift))
                .map(ClawMachine::countPresses)
                .mapToLongs(Press::price)
                .sum();
    }

    record ClawMachine(long ax, long ay, long bx, long by, long x, long y) {
        static ClawMachine fromString(final String definition) {
            final var l = Longs.fromString(definition);
            return new ClawMachine(l.at(0), l.at(1), l.at(2), l.at(3), l.at(4), l.at(5));
        }

        Press countPresses() {
            final long d = ax * by - ay * bx;
            if (d == 0)
                throw new IllegalStateException(String.format("A[%d,%d]  B=[%d,%d] are colinear", ax, ay, bx, by));
            final long b = (y * ax - x * ay) / d, a = (x - b * bx) / ax;
            if (a * ax + b * bx != x || a * ay + b * by != y) return Press.Unsolvable;
            return new Press(a, b, 3 * a + b);
        }

        ClawMachine shifted(final long offset) {
            return new ClawMachine(ax, ay, bx, by, x + offset, y + offset);
        }
    }

    record Press(long a, long b, long price) {
        static Press Unsolvable = new Press(-1, -1, 0);
    }

}
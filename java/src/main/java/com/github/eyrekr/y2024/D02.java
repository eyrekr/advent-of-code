package com.github.eyrekr.y2024;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;

/**
 * https://adventofcode.com/2024/day/2
 * 1) 483
 * 2)
 */
class D02 {
    final Arr<Longs> reports;

    D02(final String input) {
        reports = Arr.ofLinesFromString(input).map(Longs::fromString);
    }

    long star1() {
        return reports.where(this::safe).length;
    }

    private boolean safe(final Longs levels) {
        final var differences = levels.deltas();
        return switch (levels.ordering()) {
            case Constant, Random -> false;
            case Ascending -> differences.min() >= 1 && differences.max() <= 3;
            case Descending -> differences.min() >= -3 && differences.max() <= -1;
        };
    }

    long star2() {
        return -1;
    }
}
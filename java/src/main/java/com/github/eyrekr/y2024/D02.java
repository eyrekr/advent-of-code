package com.github.eyrekr.y2024;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;

/**
 * https://adventofcode.com/2024/day/2
 * 1)
 * 2)
 */
class D02 {
    final Arr<Longs> reports;

    D02(final String input) {
        reports = Arr.ofLinesFromString(input).map(Longs::fromString);
    }

    long star1() {
        return -1;
    }

    long star2() {
        return -1;
    }
}
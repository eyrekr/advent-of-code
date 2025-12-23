package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;

class D02 extends Aoc {


    final Seq<Int> ranges;

    D02(final String input) {
        ranges = Seq.ofLinesFromString(input, ",").map(Int::fromString);
    }

    @Override
    public long star1() {
        return ranges.map(D02::enumerateInvalidIds).toLongs(Longs::sum).sum();
    }

    @Override
    public long star2() {
        return -1L;
    }

    static Longs enumerateInvalidIds(final Int interval) {
        return interval.where(D02::isIdInvalid);
    }

    static boolean isIdInvalid(final long id) {
        if (id == 0) return false;
        final int digits = Algebra.decimalDigits(id);
        if (digits % 2 == 1 || digits == 0) return false;
        final long exp = Algebra.E10[digits / 2];
        return (id / exp) == (id % exp);
    }
}
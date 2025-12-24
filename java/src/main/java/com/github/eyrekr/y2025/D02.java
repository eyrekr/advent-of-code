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
        return ranges.toLongs(range -> range.where(D02::isIdInvalid).sum()).sum();
    }

    @Override
    public long star2() {
        return ranges.toLongs(D02::invalidIds).sum();
    }

    static boolean isIdInvalid(final long id) {
        if (id == 0) return false;
        final int digits = Algebra.decimalDigits(id);
        if (digits % 2 == 1 || digits == 0) return false;
        final long exp = Algebra.E10[digits / 2];
        return (id / exp) == (id % exp);
    }

    static long invalidIds(final Int range) {
        final int digits = Algebra.decimalDigits(range.a);
        final Longs divisors = Algebra.allDivisors(digits).removeLast();
        return range.where(id -> divisors.atLeastOneIs()).sum();
    }

    static boolean isIdInvalid(final long id, final int segment) {
        final long t = id % Algebra.E10[segment];
    }
}
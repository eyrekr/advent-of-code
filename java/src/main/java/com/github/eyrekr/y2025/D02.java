package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;

class D02 extends Aoc {

    final Seq<Int> ranges;

    D02(final String input) {
        ranges = Seq.ofLinesFromString(input, ",").map(Int::fromString);
    }

    @Override
    public long star1() {
        return ranges.toLongs(range -> range.where(D02::madeOfSomeSequenceOfDigitsRepeatedTwice).sum()).sum();
    }

    @Override
    public long star2() {
        return ranges.toLongs(range -> range.where(D02::madeOfSomeSequenceOfDigitsRepeatedAtLeastTwice).sum()).sum();
    }

    static boolean madeOfSomeSequenceOfDigitsRepeatedTwice(final long id) {
        if (id == 0) return false;
        final int digits = Algebra.decimalDigits(id);
        if (digits % 2 == 1 || digits == 0) return false;
        final long exp = Algebra.E10[digits / 2];
        return (id / exp) == (id % exp);
    }

    static boolean madeOfSomeSequenceOfDigitsRepeatedAtLeastTwice(final long id) {
        final int digits = Algebra.decimalDigits(id);
        if (digits == 1) return false;
        return Algebra.allDivisors(digits).removeLast().atLeastOneIs(segmentLength -> {
            final int l = (int) segmentLength;
            final long exp = Algebra.E10[l];
            final long segment = id % exp;
            for (int i = l; i < digits; i += l)
                if ((id / Algebra.E10[i]) % exp != segment) return false;

            return true;
        });
    }
}
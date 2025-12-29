package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;

class D03 extends Aoc {

    final Seq<int[]> banks;

    D03(final String input) {
        banks = Seq.ofLinesFromString(input).map(D03::bank);
    }

    @Override
    public long star1() {
        return banks.toLongs(bank -> joltage(bank, 0, 2)).sum();
    }

    @Override
    public long star2() {
        return banks.toLongs(bank -> joltage(bank, 0, 12)).sum();
    }

    static int[] bank(final String line) {
        final int[] bank = new int[line.length()];
        for (int i = 0; i < bank.length; i++) bank[i] = Character.digit(line.charAt(i), 10);
        return bank;
    }

    static long joltage(final int[] bank, int i0, int l) {
        if (l <= 0) return 0L;
        int argmax = i0;
        for (int i = i0; i < bank.length - l + 1; i++) if (bank[i] > bank[argmax]) argmax = i;
        return bank[argmax] * Algebra.E10[l - 1] + joltage(bank, argmax + 1, l - 1);
    }

}
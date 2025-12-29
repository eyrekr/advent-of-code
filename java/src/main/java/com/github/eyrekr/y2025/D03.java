package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;

class D03 extends Aoc {

    final Seq<String> banks;

    D03(final String input) {
        banks = Seq.ofLinesFromString(input);
    }

    @Override
    public long star1() {
        return banks.toLongs(D03::joltage).sum();
    }

    @Override
    public long star2() {
        return -1L;
    }

    static long joltage(final String bank) {
        // find biggest number
        int max1 = -1, index = -1;
        for (int i = 0; i < bank.length() - 1; i++) {
            final int value = Character.digit(bank.charAt(i), 10);
            if (value > max1) {
                max1 = value;
                index = i;
            }
        }

        int max2 = -1;
        for (int i = index + 1; i < bank.length(); i++) {
            final int value = Character.digit(bank.charAt(i), 10);
            if (value > max2) max2 = value;
        }

        return max1 * 10 + max2;
    }

}
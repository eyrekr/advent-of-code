package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Seq;

public class D03 {

    final Seq<Rucksack> rucksacks;

    D03(final String input) {
        rucksacks = Seq.ofLinesFromString(input).map(Rucksack::from);
    }

    long star1() {
        return rucksacks.toLongs(rucksack -> {
                    if (rucksack.shared >= 'a' && rucksack.shared <= 'z') return rucksack.shared - 'a' + 1;
                    if (rucksack.shared >= 'A' && rucksack.shared <= 'Z') return rucksack.shared - 'A' + 27;
                    throw new IllegalStateException();
                })
                .sum();
    }

    long star2() {
        return 0L;
    }

    record Rucksack(String left, String right, char shared) {
        static Rucksack from(String input) {
            final String left = input.substring(0, input.length() / 2), right = input.substring(input.length() / 2);
            char shared = 0;
            for (int l = 0; l < left.length(); l++)
                for (int r = 0; r < right.length(); r++)
                    if (left.charAt(l) == right.charAt(r)) shared = left.charAt(l);
            return new Rucksack(left, right, shared);
        }
    }
}

package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Seq;

import java.util.Arrays;

public class D03 {

    final Seq<String> rucksacks;

    D03(final String input) {
        rucksacks = Seq.ofLinesFromString(input);
    }

    long star1() {
        return rucksacks.map(contents -> {
                    final String left = contents.substring(0, contents.length() / 2), right = contents.substring(contents.length() / 2);
                    return new Indicator().and(left).and(right);
                })
                .toLongs(Indicator::topPriority)
                .sum();
    }

    long star2() {
        return rucksacks.batch(3)
                .map(group -> group.reduce(new Indicator(), Indicator::and))
                .toLongs(Indicator::topPriority)
                .sum();
    }

    static class Indicator {
        final boolean[] ch = new boolean[64];

        {
            Arrays.fill(ch, true);
        }

        Indicator and(final String input) {
            final boolean[] t = new boolean[64];
            for (int i = 0; i < input.length(); i++) t[priority(input.charAt(i))] = true;
            for (int i = 0; i < 64; i++) ch[i] = ch[i] && t[i];
            return this;
        }

        int topPriority() {
            for (int i = 0; i < ch.length; i++)
                if (ch[i]) return i;
            throw new IllegalStateException();
        }

        static int priority(final char ch) {
            if (ch >= 'a' && ch <= 'z') return ch - 'a' + 1;
            if (ch >= 'A' && ch <= 'Z') return ch - 'A' + 27;
            throw new IllegalStateException("Unexpected character " + ch);
        }
    }
}

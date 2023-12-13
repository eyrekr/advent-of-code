package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

import java.util.Map;
import java.util.Optional;

/**
 * https://adventofcode.com/2023/day/13
 * 1)
 * 2)
 */
class D13 extends AoC {

    final String[] grids;

    D13(final String input) {
        super(input);
        grids = input.split("\n\n");
    }

    long star1() {
        long sum = 0L;
        for (final String grid : grids) {
            final String[] pattern = grid.split("\n");
            final long verticalMirror = mirror(pattern).orElse(0L);
            final long horizontalMirror = mirror(transpose(pattern)).orElse(0L);

            if(verticalMirror > horizontalMirror) {
                sum += verticalMirror;
            } else {
                sum += 100* horizontalMirror;
            }
        }
        return sum;
    }

    long star2() {
        return 0L;
    }

    Optional<Long> mirror(final String[] pattern) {
        Seq<Long> candidates = Seq.empty();
        for (int i = 0; i < pattern.length; i++) {
            for (int j = i + 1; j < pattern.length; j++) {
                if (pattern[i].equals(pattern[j])) {
                    candidates = candidates.prepend((long) (i + j) / 2);
                }
            }
        }
        return candidates.frequency().entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    String[] transpose(final String[] pattern) {
        final int m = pattern[0].length();
        final int n = pattern.length;
        final String[] t = new String[m];
        for (int column = 0; column < m; column++) {
            final StringBuilder builder = new StringBuilder();
            for (int row = 0; row < n; row++) {
                builder.append(pattern[row].charAt(column));
            }
            t[column] = builder.toString();
        }
        return t;
    }
}
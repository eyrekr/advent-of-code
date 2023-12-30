package com.github.eyrekr;

import com.github.eyrekr.immutable.Seq;

import java.util.Comparator;

/**
 * https://adventofcode.com/2023/day/13
 * 1) 30535
 * 2) 30844
 */
class D13 extends AoC {

    final String[] grids;

    D13(final String input) {
        super(input);
        grids = input.split("\n\n");
    }

    long star1() {
        return solveWithExpectedDefects(0);
    }

    long star2() {
        return solveWithExpectedDefects(1);
    }

    long solveWithExpectedDefects(final int expectedDefects) {
        long sum = 0L;
        for (final String grid : grids) {
            final String[] pattern = grid.split("\n"), transposed = transpose(pattern);
            final var horizontalMirror = solveWithExpectedDefects(pattern, expectedDefects);
            final var verticalMirror = solveWithExpectedDefects(transposed, expectedDefects);
            sum += verticalMirror.isEmpty ? 100L * horizontalMirror.value : verticalMirror.value;
        }
        return sum;
    }

    Seq<Integer> solveWithExpectedDefects(final String[] pattern, final int expectedDefects) {
        return seeds(pattern)
                .where(seed -> seed.defects <= expectedDefects)
                .where(seed -> defectsInMirror(pattern, seed.a, seed.b) == expectedDefects)
                .map(seed -> seed.b)
                .sortedBy(Comparator.comparingInt(a -> Math.abs(pattern.length / 2 - a)));
    }

    Seq<Seed> seeds(final String[] pattern) {
        Seq<Seed> seeds = Seq.empty();
        for (int i = 0; i < pattern.length - 1; i++) {
            seeds = seeds.addLast(new Seed(i, i + 1, defects(pattern[i], pattern[i + 1])));
        }
        return seeds;
    }

    int defects(final String a, final String b) {
        int defects = 0;
        for (int i = 0; i < a.length(); i++) {
            defects += a.charAt(i) == b.charAt(i) ? 0 : 1;
        }
        return defects;
    }

    int defectsInMirror(final String[] pattern, final int a, int b) {
        return a < 0 || b >= pattern.length ? 0 : defectsInMirror(pattern, a - 1, b + 1) + defects(pattern[a], pattern[b]);
    }

    String[] transpose(final String[] pattern) {
        final int m = pattern[0].length();
        final String[] transposed = new String[m];
        for (int column = 0; column < m; column++) {
            final StringBuilder builder = new StringBuilder();
            for (final String row : pattern) {
                builder.append(row.charAt(column));
            }
            transposed[column] = builder.toString();
        }
        return transposed;
    }

    record Seed(int a, int b, int defects) {
    }
}
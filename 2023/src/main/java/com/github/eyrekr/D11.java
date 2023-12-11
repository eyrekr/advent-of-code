package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;

/**
 * https://adventofcode.com/2023/day/11
 * 1) 9509330
 * 2) 635832237682
 */
class D11 extends AoC {

    final Grid grid;

    D11(final String input) {
        super(input);
        this.grid = Grid.of(lines);
    }

    long star1() {
        return solveFor(expandedStars(2L));
    }

    long star2() {
        return solveFor(expandedStars(1_000_000L));
    }

    Seq<long[]> expandedStars(final long gap) {
        final var stars = grid.where(it -> it.ch == '#').map(it -> new long[]{it.x, it.y});
        final var columnsWithStars = stars.map(p -> p[0]).unique();
        final var rowsWithStars = stars.map(p -> p[1]).unique();
        return stars.map(p -> new long[]{
                gap * p[0] + (1 - gap) * columnsWithStars.where(x -> x < p[0]).length,
                gap * p[1] + (1 - gap) * rowsWithStars.where(y -> y < p[1]).length
        });
    }

    long solveFor(final Seq<long[]> stars) {
        return stars.contextMap(star -> star.isLast ? 0L : star.tail.map(b -> d(star.value, b)).reduce(Long::sum)).reduce(Long::sum);
    }

    long d(final long[] a, final long[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}

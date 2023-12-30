package com.github.eyrekr;

import com.github.eyrekr.util.Grid;

import java.util.HashSet;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/21
 * 1) 3598
 * 2) 601441063166538
 */
class D21 extends AoC {

    final Grid grid;
    final long steps;

    D21(final String input, final long steps) {
        super(input);
        this.steps = steps;
        grid = Grid.of(input);
    }

    @Override
    long star1() {
        return run(grid, steps);
    }


    @Override
    long star2() {
        final int factor = grid.m, delta = grid.m / 2;
        final Grid inflatedGrid = grid.repeat(7).replace(ch -> ch == 'S' ? '.' : ch);
        inflatedGrid.it(inflatedGrid.m / 2, inflatedGrid.n / 2).set('S', 0, true);

        final long f0 = run(inflatedGrid, 0 * factor + delta);
        final long f1 = run(inflatedGrid, 1 * factor + delta);
        final long f2 = run(inflatedGrid, 2 * factor + delta);
        final long f3 = run(inflatedGrid, 3 * factor + delta);

        // fit the quadratic curve  f(x) = ax^2 + bx + c
        // using the first 3 values f(0), f(1), f(2)  -  3 points are enough to define the curve
        final long c = f0, a = (f2 - 2 * f1 + f0) / 2, b = f1 - f0 - a;
        // use the value at f(3) to validate the curve
        if (a * 3 * 3 + b * 3 + c != f3) throw new IllegalStateException();

        final long n = steps / grid.m;
        final long fn = a * n * n + b * n + c;
        return fn;
    }


    long run(final Grid grid, final long steps) {
        // let us treat grid.d as "parity" where -1 - not reached yet, 0 - even, 1 - odd
        grid.each(it -> it.set(it.ch, -1, false));
        final Grid.It start = grid.firstOneOf("S*");
        start.set('*', 0, true);
        Set<Integer> border = new HashSet<>();
        border.add(start.i);

        for (int step = 1; step <= steps; step++) {
            final Set<Integer> expandedBorder = new HashSet<>();
            final int parity = step % 2;
            for (final int i : border) {
                grid.it(i).unvisitedNeighbours()
                        .where(n -> n.ch != '#')
                        .each(n -> {
                            n.set(n.ch, parity, true);
                            expandedBorder.add(n.i);
                        });
            }
            border = expandedBorder;
        }
        return grid.sum(it -> it.d == steps % 2 ? 1 : 0);
    }

    void print(final Grid grid, final int parity) {
        grid.print(it -> switch (it.ch) {
            case '#' -> "@w#";
            case '*' -> "@y*";
            case '.' -> it.d == parity ? "@r**O**" : "@w.";
            default -> "@W?";
        });
    }

}

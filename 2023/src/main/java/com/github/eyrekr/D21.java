package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

import java.util.HashSet;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/21
 * 1) 3598
 * 2)
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
        // let us treat grid.d as "parity" where 0 - even, 1 - odd
        grid.each(it -> it.set(it.ch, -1, false));
        final Grid.It start = grid.chFirst(ch -> ch == 'S');
        start.set('*', 0, true);
        Set<Integer> border = new HashSet<>();
        border.add(start.i);

        // iterate
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

            Str.print("\n\nAfter step @c%d\n", step);
            grid.print(it -> switch (it.ch) {
                case '#' -> "@w#";
                case '*' -> "@y*";
                case '.' -> it.d == parity ? "@r**O**" : "@w.";
                default -> "@W?";
            });
        }

        // calculate the places with the given parity
        return grid.sum(it -> it.d == steps % 2 ? 1 : 0);
    }


    @Override
    long star2() {
        return 0L;
    }

}

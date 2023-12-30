package com.github.eyrekr;

import com.github.eyrekr.mutable.Grid;

import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/14
 * 1) 106186
 * 2) 106390
 */
class D14 extends AoC {

    D14(final String input) {
        super(input);
    }

    long star1() {
        final Grid grid = Grid.of(input);
        north(grid);
        return score(grid);
    }

    long star2() {
        final int n = 1_000_000_000;
        Grid grid = Grid.of(input);

        final Map<String, Integer> memory = new HashMap<>();
        memory.put(grid.toString(), 0);

        for (int iteration = 1; iteration <= n; iteration++) {
            grid = cycle(grid);
            final Integer firstIteration = memory.get(grid.toString());
            if (firstIteration != null) {
                final int cycleLength = iteration - firstIteration;
                int remainingCycles = (n - iteration) % cycleLength;
                while (remainingCycles > 0) {
                    grid = cycle(grid);
                    remainingCycles--;
                }
                break;
            }
            memory.put(grid.toString(), iteration);
        }
        return score(grid);
    }

    static Grid cycle(final Grid grid) {
        Grid tilted = grid;
        for (int t = 0; t < 4; t++) {
            north(tilted);
            tilted = tilted.rotateCW();
        }
        return tilted;
    }

    static void north(final Grid grid) {
        for (int column = 0; column < grid.m; column++) {
            int firstEmptyRow = 0;
            for (int row = 0; row < grid.n; row++) {
                switch (grid.at(column, row)) {
                    case 'O' -> {
                        if (firstEmptyRow < row) {
                            grid.a[column][firstEmptyRow] = 'O';
                            grid.a[column][row] = '.';
                            firstEmptyRow++;
                        } else {
                            firstEmptyRow = row + 1;
                        }
                    }
                    case '#' -> firstEmptyRow = row + 1;
                }
            }
        }
    }

    static long score(final Grid grid) {
        return grid.sum(it -> it.ch == 'O' ? it.n - it.y : 0);
    }
}
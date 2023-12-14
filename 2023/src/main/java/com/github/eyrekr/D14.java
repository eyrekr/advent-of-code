package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/14
 * 1)
 * 2)
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
        // idea is that at one point the configurations start to repeat
        // we just need to know the length of the cycle + length of the path that lead to the cycle
        Map<String, Integer> memory = new HashMap<>();
        Str.print("@GSTART\n");
        grid.print();
        for (int iteration = 0; iteration < n; iteration++) {
            grid = cycle(grid);
            //Str.print("AFTER @b%d@@ CYCLES\n", iteration + 1);
            //grid.print();
            final String key = encode(grid);
            final Integer firstIteration = memory.get(key);
            if (firstIteration != null) {
                // cycle detected!
                final int cycleLength = iteration - firstIteration;
                int remainingCycles = (n - firstIteration) % cycleLength;
                Str.print("@RCYCLE DETECTED@@   first=@c%d@@    iteration=@c%d@@   cycleLength=@c%d@@   remainingCycles=@c%d@@\n", firstIteration, iteration, cycleLength, remainingCycles);
                while (remainingCycles > 0) {
                    grid = cycle(grid);
                    remainingCycles--;
                }
                break;
            } else {
                memory.put(key, iteration);
            }
        }

        Str.print("@GFINISH\n");
        grid.print();
        return score(grid);
    }

    String encode(final Grid grid) {
        final StringBuilder b = new StringBuilder();
        for (final var it : grid) {
            b.append(it.ch);
        }
        return b.toString();
    }

    Grid cycle(final Grid grid) {
        Grid tilted = grid;
        for (int t = 0; t < 4; t++) {
            north(tilted);
            tilted = tilted.rotateClockwise();
        }
        return tilted;
    }

    void north(final Grid grid) {
        for (int column = 0; column < grid.m; column++) {
            int firstEmptyRow = 0;
            for (int row = 0; row < grid.n; row++) {
                switch (grid.at(column, row)) {
                    case '.' -> {
                        // do nothing
                    }
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

    long score(final Grid grid) {
        long score = 0L;
        for (final Grid.It it : grid) {
            if (it.ch == 'O') {
                score += it.n - it.y;
            }
        }
        return score;
    }
}
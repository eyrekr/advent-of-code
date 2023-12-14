package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

import java.util.BitSet;
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

        final Map<BitSet, State> memory = new HashMap<>();
        memory.put(encode(grid), new State(0, score(grid)));

        for (int iteration = 1; iteration <= n; iteration++) {
            grid = cycle(grid);
            final var key = encode(grid);
            final State firstState = memory.get(key);
            if (firstState != null) {
                final int cycleLength = iteration - firstState.iteration;
                final int remainingCycles = (n - iteration) % cycleLength;
                return memory.values().stream()
                        .filter(st -> st.iteration == firstState.iteration + remainingCycles)
                        .findFirst().orElseThrow()
                        .score;
            }
            memory.put(key, new State(iteration, score(grid)));
        }
        return score(grid);
    }

    static Grid cycle(final Grid grid) {
        Grid tilted = grid;
        for (int t = 0; t < 4; t++) {
            north(tilted);
            tilted = tilted.rotateClockwise();
        }
        return tilted;
    }

    static void north(final Grid grid) {
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

    static long score(final Grid grid) {
        return grid.reduce(0L, (score, it) -> it.ch == 'O' ? score + it.n - it.y : score);
    }

    static BitSet encode(final Grid grid) {
        return grid.reduce(new BitSet(grid.m * grid.n / 2), (bits, it) -> {
            if (it.ch == 'O') {
                bits.set(it.i);
            }
            return bits;
        });
    }

    record State(int iteration, long score) {
    }
}
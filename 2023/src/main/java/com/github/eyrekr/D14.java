package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

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

        final Map<String, State> memory = new HashMap<>();
        {// initial state
            final State initialState = State.from(grid, 0);
            memory.put(initialState.key, initialState);
        }
        for (int iteration = 1; iteration <= n; iteration++) {
            grid = cycle(grid);
            final State state = State.from(grid, iteration);
            final State firstState = memory.get(state.key);
            if (firstState != null) {
                final int cycleLength = iteration - firstState.iteration;
                final int remainingCycles = (n - iteration) % cycleLength;
                final State terminalState = memory.values().stream()
                        .filter(st -> st.iteration == firstState.iteration + remainingCycles)
                        .findFirst().orElseThrow();
                Str.print(
                        "**CYCLE DETECTED**\n first=@c%d@@\n iteration=@c%d@@\n cycleLength=@c%d@@\n remainingCycles=@c%d@@\n terminalState=@G%d@@\n score=@G%s@@\n",
                        firstState.iteration,
                        iteration,
                        cycleLength,
                        remainingCycles,
                        terminalState.iteration,
                        terminalState.score);
                return terminalState.score;
            }
            memory.put(state.key, state);
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

    record State(Grid grid, int iteration, long score, String key) {
        static State from(Grid grid, int iteration) {
            final String key = grid.reduce(new StringBuilder(), (builder, it) -> builder.append(it.ch)).toString();
            return new State(grid, iteration, D14.score(grid), key);
        }
    }
}
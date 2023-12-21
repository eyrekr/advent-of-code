package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

/**
 * https://adventofcode.com/2023/day/21
 * 1) 3598
 * 2)
 */
class D21 extends AoC {

    static final int STONE = -9999;
    static final int UNREACHABLE = -9;

    final Grid grid;

    D21(final String input) {
        super(input);
        grid = Grid.of(input);
    }

    long star1(final int steps) {
        // init
        int[][] d = new int[grid.m][grid.n];
        for (final var it : grid) {
            switch (it.ch) {
                case '#' -> d[it.x][it.y] = STONE;
                case 'S' -> d[it.x][it.y] = 0;
                default -> d[it.x][it.y] = UNREACHABLE;
            }
        }

        // iterate
        for (int step = 0; step < steps; step++) {
            int[][] tmp = new int[grid.m][grid.n];
            for (int y = 0; y < grid.n; y++) {
                for (int x = 0; x < grid.m; x++) {
                    if (d[x][y] == STONE) {
                        tmp[x][y] = STONE;
                    }
                    if (d[x][y] == step) {
                        if (x - 1 >= 0 && tmp[x - 1][y] != STONE) tmp[x - 1][y] = step + 1;
                        if (x + 1 < grid.m && tmp[x + 1][y] != STONE) tmp[x + 1][y] = step + 1;
                        if (y - 1 >= 0 && tmp[x][y - 1] != STONE) tmp[x][y - 1] = step + 1;
                        if (y + 1 < grid.n && tmp[x][y + 1] != STONE) tmp[x][y + 1] = step + 1;
                    }
                }
            }
            d = tmp;

            print(step, d);
        }

        // calculate the steps
        long sum = 0;
        for (int i = 0; i < grid.m * grid.n; i++)
            if (d[i % grid.m][i / grid.m] == steps) sum++;
        return sum;
    }


    long star2(final int steps) {
        return 0L;
    }


    void print(final int step, final int[][] d) {
        Str.print("After step @c%d\n", step + 1);
        for (int y = 0; y < grid.n; y++) {
            for (int x = 0; x < grid.m; x++) {
                if (d[x][y] == STONE) {
                    Str.print("@w#");
                } else if (d[x][y] == step + 1) {
                    Str.print("@rO");
                } else {
                    Str.print("@w.");
                }
            }
            Str.print("\n");
        }
        Str.print("\n\n");
    }

}

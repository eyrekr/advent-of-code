package com.github.eyrekr.y2022;

import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.mutable.Grid.It;
import com.github.eyrekr.mutable.State;
import com.github.eyrekr.raster.Direction;

public class D08 {

    final Grid grid;

    D08(final String input) {
        grid = Grid.of(input);
    }

    long star1() {
        Grid forest = grid;
        for (int i = 0; i < 4; i++) forest = calculateVisibility(forest).rotateCW();
        forest.print(it -> it.state == State.Open ? "@GX" : "@WO");
        return forest.reduce(0L, (sum, it) -> it.state == State.Open ? sum + 1 : sum);
    }

    Grid calculateVisibility(final Grid grid) {
        for (int x = 0; x < grid.m; x++) {
            int h = -1;
            for (int y = 0; y < grid.n; y++) {
                final It a = grid.it(x, y);
                if (a.digit > h) {
                    h = a.digit;
                    a.setState(State.Open);
                }
            }
        }
        return grid;
    }

    long star2() {
        grid.each(it -> it.setDistance(countTrees(it, Direction.Down) * countTrees(it, Direction.Up) * countTrees(it, Direction.Left) * countTrees(it, Direction.Right)));
        grid.print(it -> it.d > 0 ? String.format("@k%3d", it.d) : "@W  0");
        return grid.reduce(0, (max, it) -> Math.max(max, it.d));
    }

    int countTrees(final It it, final Direction direction) {
        int trees = 0;
        for (final It next : it.iterate(direction)) {
            trees++;
            if (next.digit >= it.digit) break;
        }
        return trees;
    }
}

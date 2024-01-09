package com.github.eyrekr.y2022;

import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.mutable.Grid.It;
import com.github.eyrekr.mutable.Grid.State;

public class D08 {

    final Grid grid;

    D08(final String input) {
        grid = Grid.of(input);
    }

    long star1() {
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
        for (int x = 0; x < grid.m; x++) {
            int h = -1;
            for (int y = grid.n - 1; y >= 0; y--) {
                final It a = grid.it(x, y);
                if (a.digit > h) {
                    h = a.digit;
                    a.setState(State.Open);
                }
            }
        }
        for (int y = 0; y < grid.n; y++) {
            int h = -1;
            for (int x = 0; x < grid.m; x++) {
                final It a = grid.it(x, y);
                if (a.digit > h) {
                    h = a.digit;
                    a.setState(State.Open);
                }
            }
        }
        for (int y = 0; y < grid.n; y++) {
            int h = -1;
            for (int x = grid.m - 1; x >= 0; x--) {
                final It a = grid.it(x, y);
                if (a.digit > h) {
                    h = a.digit;
                    a.setState(State.Open);
                }
            }
        }
        grid.print(it-> it.state==State.Open?"@GX":"@rX");
        return grid.reduce(0L, (sum, it) -> it.state == State.Open ? sum + 1 : sum);
    }

    long star2() {
        return 0L;
    }
}

package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Grid.It;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

import java.util.Arrays;


/**
 * https://adventofcode.com/2023/day/17
 * 1)
 * 2)
 */
class D17 extends AoC {

    final Direction[] directions = Direction.values();
    final Grid grid = Grid.of(input);

    D17(final String input) {
        super(input);
    }

    long star1() {
        final Seq<Tail>[][] paths = new Seq[grid.m][grid.n];
        paths[0][0] = Seq.of(new Tail(0));
        grid.d[0][0] = 0;

        while (true) {
            final It p0 = grid.reduce(null, (min, p) -> min == null || (!p.b && p.d < min.d) ? p : min);
            if (p0 == null) break; // everything visited
            p0.set('*', p0.d, true);
            for (final Direction direction : directions) {
                p0.tryToGo(direction)
                        .filter(p1 -> !p1.b) // only the not visited
                        .ifPresent(p1 -> {
                            final int d1 = p0.d + p1.digit;
                            if (p1.d > d1) { // we can improve
                                p1.set(p1.ch, d1, p1.b);
                                paths[p1.x][p1.y] = paths[p0.x][p0.y]
                                        .map(path -> path.add(direction))
                                        .unique();
                            } else if (p1.d == d1) { // there is an alternative path
                                paths[p1.x][p1.y] = paths[p1.x][p1.y]
                                        .addSeq(paths[p0.x][p0.y].map(path -> path.add(direction)))
                                        .unique();
                            }
                        });
            }

            {// print distances
                Str.print("@y-----------------------------------------------------------------\n");
                for (int y = 0; y < grid.n; y++) {
                    for (int x = 0; x < grid.m; x++) {
                        final String color = (p0.x == x && p0.y == y) ? "@r" : grid.b[x][y] ? "@g" : "@w";
                        final String value = grid.d[x][y] == Integer.MAX_VALUE ? "." : Integer.toString(grid.d[x][y]);
                        Str.print("%s%4s@@ ", color, value);
                    }
                    Str.print("\n");
                }
            }
        }
        return grid.d[grid.m - 1][grid.n - 1];
    }


    long star2() {
        return 0L;
    }

    record Tail(int t) {
        Tail add(final Direction d) {
            return new Tail((t *10 + (d.ordinal() + 1))/1000);
        }
    }
}
package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Grid.It;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;


/**
 * https://adventofcode.com/2023/day/17
 * 1)
 * 2)
 */
class D17 extends AoC {

    final Seq<Direction> directions = Seq.of(Direction.Right, Direction.Down, Direction.Left, Direction.Up);
    final Grid grid = Grid.of(input);

    D17(final String input) {
        super(input);
    }

    long star1() {
        // initialize the distances and the boolean flag, that means "visited"
        grid.each(it -> it.set(it.ch, Integer.MAX_VALUE, false));
        final Seq<Tail>[][] paths = new Seq[grid.m][grid.n];
        paths[0][0] = Seq.of(new Tail(null, null, null));
        grid.d[0][0] = 0;

        while (true) {
            final It p0 = grid.reduce(null, (min, p) -> !p.b && (min == null || p.d < min.d) ? p : min);
            if (p0 == null) break; // everything visited
            p0.set('*', p0.d, true);
            // if there is only one way to get to p0 and the last 3 steps are in the same direction,
            // it means that this direction is forbidden now
            final Direction forbidden = paths[p0.x][p0.y].length == 1 ? paths[p0.x][p0.y].value.forbidden() : null;
            directions
                    .where(direction -> direction != forbidden)
                    .each(direction -> {
                        p0.tryToGo(direction)
                                .filter(p1 -> !p1.b) // only if not visited already
                                .ifPresent(p1 -> {
                                    final int d1 = p0.d + p1.digit;
                                    if (p1.d > d1) { // we can improve
                                        p1.set(p1.ch, d1, p1.b);
                                        paths[p1.x][p1.y] = paths[p0.x][p0.y].map(tail -> tail.add(direction)).unique();
                                    } else if (p1.d == d1) { // there are alternative paths
                                        paths[p1.x][p1.y] = paths[p1.x][p1.y].addSeq(paths[p0.x][p0.y].map(tail -> tail.add(direction))).unique();
                                    }
                                });
                    });

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

    record Tail(Direction a, Direction b, Direction c) {
        Tail add(final Direction d) {
            return new Tail(b, c, d);
        }

        Direction forbidden() {
            return a == b && b == c ? a : null;
        }

        @Override
        public String toString() {
            return "" + (a != null ? a.ch : ' ') + (b != null ? b.ch : ' ') + (c != null ? c.ch : ' ');
        }
    }
}
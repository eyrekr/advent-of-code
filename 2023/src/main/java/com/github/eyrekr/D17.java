package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Objects;

import static com.github.eyrekr.D17.Direction.*;

/**
 * https://adventofcode.com/2023/day/17
 * 1)
 * 2)
 */
class D17 extends AoC {

    final int[][] a;
    final int m;
    final int n;

    D17(final String input) {
        super(input);
        final Grid grid = Grid.of(input);
        m = grid.m;
        n = grid.n;
        a = new int[m][n];
        grid.each(it -> a[it.x][it.y] = it.digit);
    }

    long star1() {
        final var distance = new int[m][n];
        for (int i = 0; i < m * n; i++) distance[i % m][i / m] = Integer.MAX_VALUE;
        distance[0][0] = 0;

        final boolean[][] visited = new boolean[m][n];
        final Seq<Seq<Direction>>[][] paths = new Seq[m][n];

        while (true) {
            int remaining = 0;
            int x0 = 0, y0 = 0; // the closest unvisited point so far
            int d0 = Integer.MAX_VALUE;
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    if (!visited[x][y]) {
                        remaining++;
                        int d = distance[x][y];
                        if (d < d0) {
                            x0 = x;
                            y0 = y;
                            d0 = d;
                        }
                    }
                }
            }
            if (remaining == 0) break;
            visited[x0][y0] = true;
            final var p0 = new P(x0, y0);




            // update distances of all unvisited nodes connected with p0 = [x0,y0]; d0 is the best score so far
            // update only when there is an improvement!
            for (final var direction : Direction.values()) {
                // direction is forbidden if it is the 4th step in the same direction



                final var p1 = p0.go(direction);
                if (p1.valid(m, n) && !visited[p1.x][p1.y]) {
                    final var d1 = distance[p1.x][p1.y];
                    final var candidate = d0 + a[p1.x][p1.y];
                    if (d1 > candidate) { // distance can be improved
                        distance[p1.x][p1.y] = candidate;
                        //predecessors.removeAll(p1);
                        //predecessors.put(p1, p0);
                    } else if (d1 == candidate) { // same distance
                        //predecessors.put(p1, p0);
                    }
                }
            }


            {// print distances
                Str.print(
                        "@y----------------------------------------------------------------- @c%d @r%d\n",
                        remaining,
                        d0);
                for (int y = 0; y < n; y++) {
                    for (int x = 0; x < m; x++) {
                        final String color = (x0 == x && y0 == y) ? "@r" : visited[x][y] ? "@g" : "@w";
                        final String value = distance[x][y] == Integer.MAX_VALUE ? "." : Integer.toString(distance[x][y]);
                        Str.print("%s%4s@@ ", color, value);
                    }
                    Str.print("\n");
                }
            }
        }

        return distance[m - 1][n - 1];
    }



    long star2() {
        return 0L;
    }

    enum Direction {
        Left(-1, 0), Right(+1, 0), Up(0, -1), Down(0, +1);
        final int dx, dy;

        Direction(final int dx, final int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    record P(int x, int y) {
        P go(final Direction d) {
            return new P(x + d.dx, y + d.dy);
        }

        boolean valid(final int m, final int n) {
            return x >= 0 && x < m && y >= 0 && y < n;
        }
    }
}
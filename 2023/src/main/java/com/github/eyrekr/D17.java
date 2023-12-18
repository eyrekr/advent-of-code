package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

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
        final var score = new int[m][n];
        for (int i = 0; i < m * n; i++) score[i % m][i / m] = Integer.MAX_VALUE;
        score[0][0] = 0;

        final Seq<Direction>[][] path = new Seq[m][n];
        path[0][0] = Seq.empty();

        final boolean[][] visited = new boolean[m][n];
        record Bod(int x, int y) {
        }
        final Bod[][] prev = new Bod[m][n];
        prev[0][0] = new Bod(0, 0);

        while (true) {
            int remaining = 0;
            int x0 = 0, y0 = 0; // the closest unvisited point so far
            int d0 = Integer.MAX_VALUE;
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    if (!visited[x][y]) {
                        remaining++;
                        int d = score[x][y];
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
            final var p = path[x0][y0];
            final var nogo = Direction.forbidden(p);
            // update score of all unvisited nodes connected with x0,y0; d0 is the best score so far
            if (Up != nogo && y0 > 0 && !visited[x0][y0 - 1]) {
                score[x0][y0 - 1] = d0 + a[x0][y0 - 1];
                path[x0][y0 - 1] = p.addFirst(Up);
                prev[x0][y0 - 1] = new Bod(x0, y0);
            }
            if (Down != nogo && y0 + 1 < n && !visited[x0][y0 + 1]) {
                score[x0][y0 + 1] = d0 + a[x0][y0 + 1];
                path[x0][y0 + 1] = p.addFirst(Down);
                prev[x0][y0 + 1] = new Bod(x0, y0);
            }
            if (Left != nogo && x0 > 0 && !visited[x0 - 1][y0]) {
                score[x0 - 1][y0] = d0 + a[x0 - 1][y0];
                path[x0 - 1][y0] = p.addFirst(Left);
                prev[x0 - 1][y0] = new Bod(x0, y0);
            }
            if (Right != nogo && x0 + 1 < m && !visited[x0 + 1][y0]) {
                score[x0 + 1][y0] = d0 + a[x0 + 1][y0];
                path[x0 + 1][y0] = p.addFirst(Right);
                prev[x0 + 1][y0] = new Bod(x0, y0);
            }


//            {// print state
//                Str.print(
//                        "@y----------------------------------------------------------------- @c%d @r%d\n",
//                        remaining,
//                        d0);
//                for (int y = 0; y < n; y++) {
//                    for (int x = 0; x < m; x++)
//                        Str.print(visited[x][y] ? "@b*@@" : " ");
//                    Str.print("\n");
//                }
//            }
        }

        {//print path
            final boolean[][] Q = new boolean[m][n];
            Bod b = prev[m - 1][n - 1];
            while (!(b.x == 0 && b.y == 0)) {
                Q[b.x][b.y] = true;
                b = prev[b.x][b.y];
            }
            for (int y = 0; y < n; y++) {
                for (int x = 0; x < m; x++)
                    Str.print(Q[x][y] ? "@r*@@" : "@w.@@");
                Str.print("\n");
            }
            Str.print("@g%d\n", score[m - 1][n - 1]);
        }

        return score[m - 1][n - 1];
    }

    long star2() {
        return 0L;
    }

    enum Direction {
        Left, Right, Up, Down;

        static Direction forbidden(final Seq<Direction> path) {
            final var last3 = path.first(3);
            return (last3.length == 3 && Objects.equals(last3.value, last3.tail.value) && Objects.equals(last3.value, last3.tail.tail.value))
                    ? last3.value
                    : null;
        }
    }
}
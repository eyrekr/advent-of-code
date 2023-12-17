package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

import java.util.LinkedList;

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

        final var buffer = new LinkedList<S>();
        buffer.addLast(S.of(Direction.Right));
        buffer.addLast(S.of(Direction.Down));

        while (!buffer.isEmpty()) {
            final var s = buffer.removeFirst();
            // we are standing at [s.x, s.y] and we should explore the tile in the direction d
            // can we improve what we see at the next tile?
            final var p = path[s.x][s.y].addFirst(s.d);
            if (acceptable(p)) {// is the path acceptable?
                final var t = s.next();
                if (t.x >= 0 && t.x < m && t.y >= 0 && t.y < n) { // are we inside the map?
                    if (score[t.x][t.y] > score[s.x][s.y] + a[t.x][t.y]) { // can we improve the score?
                        // yes!
                        score[t.x][t.y] = score[s.x][s.y] + a[t.x][t.y];
                        path[t.x][t.y] = p;
                        // update neighbours
                        for (final var d : Direction.values()) {
                            if (d != s.d) { // do not go back
                                buffer.addLast(t.to(d));
                            }
                        }
                    }
                }
            }


            {// print state
                Str.print("@y----------------------------------------------------------------- @c%d\n", buffer.size());
                final var q = new String[m][n];
                for (int x = 0; x < m; x++)
                    for (int y = 0; y < n; y++)
                        q[x][y] = score[x][y] < Integer.MAX_VALUE ? "@gX@@" : " ";
                for (final var b : buffer) if (b.x >= 0 && b.y >= 0 && b.x < m && b.y < n) q[b.x][b.y] = "@RX@@";
                for (int x = 0; x < m; x++) {
                    for (int y = 0; y < n; y++)
                        Str.print(q[x][y]);
                    System.out.println();
                }
            }
        }

        {// print path
            Str.print("@y-----------------------------------------------------------------\n");
            final var q = new String[m][n];
            for (int x = 0; x < m; x++) for (int y = 0; y < n; y++) q[x][y] = " ";
            final var p = path[m - 1][n - 1].reverse();
            {
                var s = S.of(Direction.Right);
                q[0][0] = "#";
                for (var d : p) {
                    s = s.to(d);
                    s=s.next();
                    q[s.x][s.y] = "#";
                }
            }
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++)
                    Str.print(q[x][y]);
                System.out.println();
            }
        }
        return score[m - 1][n - 1];
    }

    long star2() {
        return 0L;
    }

    static boolean acceptable(final Seq<Direction> path) {
        final var last4 = path.first(4);
        final var illegal = last4.length == 4 && last4.allMatch(last4.value); // all 4 values are the same as the first
        return !illegal;
    }

    record S(int x, int y, Direction d) {
        static S of(final Direction d) {
            return new S(0, 0, d);
        }

        S next() {
            return switch (d) {
                case Left -> new S(x - 1, y, d);
                case Right -> new S(x + 1, y, d);
                case Up -> new S(x, y - 1, d);
                case Down -> new S(x, y + 1, d);
            };
        }

        S to(final Direction d) {
            return new S(x, y, d);
        }
    }

    enum Direction {Left, Right, Up, Down}
}
package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;

/**
 * https://adventofcode.com/2023/day/10
 * 1) 6903
 * 2) 265
 */
class D11 extends AoC {

    final Grid grid;

    D11(final String input) {
        super(input);
        this.grid = Grid.of(lines);
    }

    long star1() {
        var stars = starsInExpandedSpace();
        return stars
                .contextMap(starA -> starA.tail
                        .contextMap(starB -> distance(starA.value, starB.value))
                        .min(Long::compareTo))
                .reduce(Long::sum);
    }

    long distance(final Grid.P a, final Grid.P b) {
        long steps = 0L;
        int x1 = b.x, x0 = a.x, y1 = b.y, y0 = a.y;

        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;

        while (true) {
            steps++;//plot

            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * error;
            if (e2 >= dy) {
                if (x0 == x1) break;
                error = error + dy;
                x0 = x0 + sx;
            }
            if (e2 <= dx) {
                if (y0 == y1) break;
                error = error + dx;
                y0 = y0 + sy;
            }
        }

        return steps;
    }

    Seq<Grid.P> starsInExpandedSpace() {
        Seq<Integer> emptyRows = emptyRows().reverse();
        int[] yMap = new int[grid.n];
        int yOffset = 0;
        for (int i = 0; i < grid.n; i++) {
            if (!emptyRows.isEmpty && i == emptyRows.value) {
                yOffset++;
                emptyRows = emptyRows.tail;
            }
            yMap[i] = i + yOffset;
        }
        Seq<Integer> emptyCols = emptyCols().reverse();
        int[] xMap = new int[grid.m];
        int xOffset = 0;
        for (int i = 0; i < grid.m; i++) {
            if (!emptyCols.isEmpty && i == emptyCols.value) {
                xOffset++;
                emptyCols = emptyCols.tail;
            }
            xMap[i] = i + xOffset;
        }
        return grid.where(it -> it.ch == '#')
                .map(it -> it.p)
                .map(p -> new Grid.P(xMap[p.x], yMap[p.y]));
    }

    Seq<Integer> emptyRows() {
        Seq<Integer> rows = Seq.empty();
        for (int row = 0; row < grid.n; row++) {
            boolean isEmpty = true;
            for (int x = 0; x < grid.m; x++) {
                if (grid.at(x, row) == '#') {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                rows = rows.prepend(row);
            }
        }
        return rows;
    }

    Seq<Integer> emptyCols() {
        Seq<Integer> cols = Seq.empty();
        for (int col = 0; col < grid.m; col++) {
            boolean isEmpty = true;
            for (int y = 0; y < grid.n; y++) {
                if (grid.at(col, y) == '#') {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                cols = cols.prepend(col);
            }
        }
        return cols;
    }

    long star2() {
        return 0L;
    }
}

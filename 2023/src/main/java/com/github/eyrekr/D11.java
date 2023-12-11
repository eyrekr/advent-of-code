package com.github.eyrekr;

import com.github.eyrekr.util.Geo;
import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

/**
 * https://adventofcode.com/2023/day/10
 * 1) 9509330
 * 2)
 */
class D11 extends AoC {

    final Grid grid;

    D11(final String input) {
        super(input);
        this.grid = Grid.of(lines);
    }

    long star1() {
        int i = 1;
        var stars = starsInExpandedSpace(1L);
        long sum = 0L;
        while(stars.length >1) {
            var others = stars.tail;
            int j = i+1;
            while(!others.isEmpty) {
                final Geo.P a = stars.value, b = others.value;
                //final long d = a.distance(b) - 1 + Math.min(Math.abs(a.x-b.x), Math.abs(a.y - b.y));
                final long dx = Math.abs(a.x-b.x), dy = Math.abs(a.y-b.y);
                final long d = dx+dy;
                //Str.print("%2d -> %2d   =   %d\n", i, j, d);
                sum += d;
                others = others.tail;
                j++;
            }
            i++;
            stars = stars.tail;
        }
        return sum;
    }

    Seq<Geo.P> starsInExpandedSpace(final long rate) {
        Seq<Integer> emptyRows = emptyRows().reverse();
        long[] yMap = new long[grid.n];
        long yOffset = 0;
        for (int i = 0; i < grid.n; i++) {
            if (!emptyRows.isEmpty && i == emptyRows.value) {
                yOffset += rate;
                emptyRows = emptyRows.tail;
            }
            yMap[i] = i + yOffset;
        }
        Seq<Integer> emptyCols = emptyCols().reverse();
        long[] xMap = new long[grid.m];
        int xOffset = 0;
        for (int i = 0; i < grid.m; i++) {
            if (!emptyCols.isEmpty && i == emptyCols.value) {
                xOffset += rate;
                emptyCols = emptyCols.tail;
            }
            xMap[i] = i + xOffset;
        }
        return grid.where(it -> it.ch == '#')
                //.print("\n", it-> it.x +","+it.y)
                .map(it -> it.p)
                .map(p -> Geo.P.of(xMap[p.x], yMap[p.y]))
                //.print("\n", p-> p.x+","+p.y)
                ;
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
        int i = 1;
        var stars = starsInExpandedSpace(1_000_000L - 1L);
        long sum = 0L;
        while(stars.length >1) {
            var others = stars.tail;
            int j = i+1;
            while(!others.isEmpty) {
                final Geo.P a = stars.value, b = others.value;
                final long dx = Math.abs(a.x-b.x), dy = Math.abs(a.y-b.y);
                final long d = dx+dy;
                //Str.print("%2d -> %2d   =   %d\n", i, j, d);
                sum += d;
                if(sum<0) {
                    throw new IllegalStateException("overflow");
                }
                others = others.tail;
                j++;
            }
            i++;
            stars = stars.tail;
        }
        return sum;
    }
}

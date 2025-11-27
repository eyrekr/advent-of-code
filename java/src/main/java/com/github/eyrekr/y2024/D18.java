package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.State;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

class D18 extends Aoc {

    final Arr<P> positionsOfCorruptedBytes;
    final int n;
    final EGrid grid;

    D18(final String input) {
        positionsOfCorruptedBytes = Arr.ofLinesFromString(input).map(P::from);
        n = positionsOfCorruptedBytes.map(P::max).reduce(0, Math::max);
        grid = EGrid.empty(n + 1, n + 1);
    }

    @Override
    public long star1() {
        return solve(1024);
    }

    @Override
    public long star2() {
        int l = 0, r = positionsOfCorruptedBytes.length() - 1;
        while (r - l > 1) { // bisection
            final int k = (l + r) / 2;
            final boolean isExitStillReachable = solve(k) > 0;
            if (isExitStillReachable) l = k;
            else r = k;
        }
        final P p = positionsOfCorruptedBytes.at(r - 1);
        Out.print("Path is closed with byte @b#%d@@ at (@b%d@@,@b%d@@)\n", r, p.x, p.y); // AoC requires the coordinates this time
        return r;
    }

    long solve(final int steps) {
        grid.reset();
        positionsOfCorruptedBytes.copyFirst(steps).each(p -> grid.it(p.x, p.y).setSymbol(Symbol.CorruptedMemory).setState(State.Closed));
        return grid.it().bfs().to(n, n).value();
    }

    record P(int x, int y) {
        static P from(final String line) {
            final String[] a = StringUtils.split(line, ',');
            return new P(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
        }

        int max() {
            return x < y ? y : x;
        }
    }

    interface Symbol {
        char CorruptedMemory = '#';
    }
}
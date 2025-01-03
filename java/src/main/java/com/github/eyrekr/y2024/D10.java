package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;
import java.util.Set;

class D10 extends Aoc {

    interface Symbol {
        char Peak = '9';
        char TrailHead = '0';
        char[] SubPeakHeights = "876543210".toCharArray();
    }

    final Direction[] directions = new Direction[]{Direction.Up, Direction.Down, Direction.Left, Direction.Right};
    final EGrid grid;

    D10(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        final Set<Integer>[][] reachablePeaks = new Set[grid.m][grid.n];
        grid.where(Symbol.Peak).each(it -> reachablePeaks[it.x][it.y] = Set.of(it.id()));

        for (final char height : Symbol.SubPeakHeights) {
            grid.where(height).each(it -> {
                final Set<Integer> peaks = new HashSet<>();
                reachablePeaks[it.x][it.y] = peaks;
                for (final Direction direction : directions)
                    if (it.la(direction) == height + 1)
                        peaks.addAll(reachablePeaks[it.x + direction.dx][it.y + direction.dy]);
            });
        }

        return grid.where(Symbol.TrailHead).reduce(0L, (sum, it) -> sum + reachablePeaks[it.x][it.y].size());
    }

    @Override
    public long star2() {
        grid.where(it -> it.is(Symbol.Peak)).each(it -> it.setValue(1));

        for (final char height : Symbol.SubPeakHeights) {
            grid.where(it -> it.is(height)).each(it -> {
                for (final Direction direction : directions)
                    if (it.la(direction) == height + 1)
                        it.incValue(it.duplicate().go(direction).value());
            });
        }

        return grid.where(Symbol.TrailHead).reduce(0L, (sum, it) -> sum + it.value());
    }
}
package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

class D10 extends Aoc {

    interface Symbol {
        char Peak = '9';
        char TrailHead = '0';
        char[] SubPeakHeights = "876543210".toCharArray();
    }

    final Direction[] directions = new Direction[]{Direction.Up, Direction.Down, Direction.Left, Direction.Right};
    final EGrid<Collection<Integer>> grid;

    D10(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        grid.all().each(it -> it.setContext(it.is(Symbol.Peak) ? Set.of(it.id()) : new HashSet<>()));

        for (final char height : Symbol.SubPeakHeights) {
            grid.where(it -> it.is(height)).each(it -> {
                for (final Direction direction : directions) {
                    if (it.la(direction) == height + 1) {
                        final var reachablePeaks = it.duplicate().go(direction).context();
                        if (isNotEmpty(reachablePeaks))
                            it.visitContext(peaks -> peaks.addAll(reachablePeaks));
                    }
                }
            });
        }

        return grid
                .where(it -> it.is(Symbol.TrailHead))
                .reduce(0L, (sum, trailhead) -> sum + trailhead.context().size());
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

        return grid
                .where(it -> it.is(Symbol.TrailHead))
                .reduce(0L, (sum, trailhead) -> sum + trailhead.value());
    }
}
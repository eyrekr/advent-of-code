package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

class D10 extends Aoc {

    interface Symbol {
        char Peak = '9';
        char TrailHead = '0';
    }

    final Direction[] directions = new Direction[]{Direction.Up, Direction.Down, Direction.Left, Direction.Right};
    final EGrid<Collection<Integer>> grid;

    D10(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        grid.all().each(it -> it.setContext(it.is(Symbol.Peak) ? Set.of(it.id()) : new HashSet<>()));

        for (final char height : "876543210".toCharArray()) {
            grid.where(it -> it.is(height)).each(it -> {
                for (final Direction direction : directions) {
                    it.setDirection(direction);
                    if (it.la((char) (height + 1))) {
                        final var reachablePeaks = it.duplicate().go().context();
                        if (isNotEmpty(reachablePeaks))
                            it.visitContext(peaks -> peaks.addAll(reachablePeaks));
                    }
                }
            });
        }

        return grid.where(it -> it.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.context().size());
    }

    @Override
    public long star2() { // not the best approach, but it still runs in 2ms // FIXME Should be enough only to remember the length, no need to keep the whole list!
        grid.all().each(sc -> sc.setContext(sc.is(Symbol.Peak) ? List.of(sc.id()) : new ArrayList<>()));

        for (final char ch : "876543210".toCharArray()) {
            grid.where(it -> it.is(ch)).each(it -> {
                for (final Direction direction : directions) {
                    final var neighbour = it.duplicate().setDirection(direction).go();
                    if (neighbour.is((char) (ch + 1))) it.context().addAll(neighbour.context());
                }
            });
        }

        return grid.where(sc -> sc.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.context().size());
    }
}
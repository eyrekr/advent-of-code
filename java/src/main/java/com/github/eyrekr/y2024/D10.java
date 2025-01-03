package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.*;

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
        grid.all().each(it -> it.let(it.is(Symbol.Peak) ? Set.of(it.id()) : new HashSet<>()));

        for (final char ch : "876543210".toCharArray()) {
            grid.where(it -> it.is(ch)).each(it -> {
                for (final Direction direction : directions) {
                    final var neighbour = it.duplicate().set(direction).go();
                    if (neighbour.is((char) (ch + 1))) neighbour.load().addAll(it.load());
                }
            });
        }

        return grid.where(sc -> sc.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.load().size());
    }

    @Override
    public long star2() { // not the best approach, but it still runs in 2ms // FIXME Should be enough only to remember the length, no need to keep the whole list!
        grid.all().each(sc -> sc.let(sc.is(Symbol.Peak) ? List.of(sc.id()) : new ArrayList<>()));

        for (final char ch : "876543210".toCharArray()) {
            grid.where(it -> it.is(ch)).each(it -> {
                for (final Direction direction : directions) {
                    final var next = it.duplicate().set(direction).go();
                    if (next.is((char) (ch + 1))) next.load().addAll(it.load());
                }
            });
        }

        return grid.where(sc -> sc.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.load().size());
    }
}
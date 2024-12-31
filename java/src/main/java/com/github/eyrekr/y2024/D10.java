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
        grid.scan().each(sc -> sc.store(sc.is(Symbol.Peak) ? Set.of(sc.id()) : new HashSet<>()));

        for (final char ch : "876543210".toCharArray()) {
            grid.scan(sc -> sc.is(ch)).each(level -> {
                for (final Direction direction : directions) {
                    final var it = level.it().set(direction).go();
                    if (it.is((char)(ch + 1))) level.load().addAll(it.load());
                }
            });
        }

        return grid.scan(sc -> sc.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.load().size());
    }

    @Override
    public long star2() { // not the best approach, but it still runs in 2ms
        grid.scan().each(sc -> sc.store(sc.is(Symbol.Peak) ? List.of(sc.id()) : new ArrayList<>()));

        for (final char ch : "876543210".toCharArray()) {
            grid.scan(sc -> sc.is(ch)).each(level -> {
                for (final Direction direction : directions) {
                    final var it = level.it().set(direction).go();
                    if (it.is((char)(ch + 1))) level.load().addAll(it.load());
                }
            });
        }

        return grid.scan(sc -> sc.is(Symbol.TrailHead)).reduce(0L, (sum, trailhead) -> sum + trailhead.load().size());
    }
}
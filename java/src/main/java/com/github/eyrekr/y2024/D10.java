package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid2;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;
import java.util.Set;

class D10 extends Aoc {

    final Grid2 grid;

    D10(final String input) {
        grid = Grid2.fromString(input);
    }

    @Override
    public long star1() {
        final Set<Integer>[][] reachablePeaks = new Set[grid.m][grid.n];
        grid.scan().each(sc -> reachablePeaks[sc.x][sc.y] = sc.is('9') ? Set.of(sc.id()) : new HashSet<>());

        final Seq<Direction> directions = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
        for (final char ch : "876543210".toCharArray()) {
            grid.scan(sc -> sc.is(ch)).each(hit -> directions
                    .where(direction -> grid.at(hit.x + direction.dx, hit.y + direction.dy) == (ch + 1))
                    .map(direction -> reachablePeaks[hit.x + direction.dx][hit.y + direction.dy])
                    .each(reachablePeaks[hit.x][hit.y]::addAll));
        }

        return grid.scan(sc->sc.is('0')).collect().toLongs(sc->reachablePeaks[sc.x][sc.y].size()).sum();
    }
}
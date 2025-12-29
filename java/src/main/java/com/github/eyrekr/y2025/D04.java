package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

class D04 extends Aoc {

    static final Seq<Direction> D8 = Seq.of(
            Direction.Up, Direction.Down, Direction.Left, Direction.Right,
            Direction.LeftUp, Direction.RightUp, Direction.LeftDown, Direction.RightDown);
    final EGrid grid;

    D04(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        return grid.where(D04::fewerThanFourRollsOfPaper).count();
    }

    @Override
    public long star2() {
        return -1L;
    }

    static boolean fewerThanFourRollsOfPaper(final EGrid.It it) {
        if(it.isNot('@')) return false;
        final long rollsOfPaperAround = D8.reduce(0L, (acc, direction) -> it.isAhead(direction, '@') ? acc + 1 : acc);
        return rollsOfPaperAround < 4;
    }
}
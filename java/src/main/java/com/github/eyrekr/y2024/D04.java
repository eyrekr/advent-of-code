package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.raster.Direction;

class D04 extends Aoc {

    final Seq<Direction> directions = Seq.of(
            Direction.UpLeft, Direction.Up, Direction.UpRight,
            Direction.Left, Direction.Right,
            Direction.DownLeft, Direction.DownRight, Direction.Down);
    final Grid grid;

    D04(final String input) {
        grid = Grid.of(input);
    }

    @Override
    public long star1() {
        return grid.reduce(
                0L,
                (sum, it) -> sum + directions.countWhere(direction -> it.checkAhead(direction, "XMAS")));
    }

}
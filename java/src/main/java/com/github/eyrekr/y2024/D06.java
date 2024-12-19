package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.raster.Direction;

class D06 extends Aoc {

    final Grid grid;

    D06(final String input) {
        this.grid = Grid.of(input);
    }

    @Override
    public long star1() {
        Grid.It it = grid.first('^');
        Direction direction = Direction.Up;

        while (it != null) {
            it.set('X');
            if (it.lookAhead(direction) == Grid.WALL) direction = direction.turn90DegreesRight();
            else it = it.go(direction);
        }

        return grid.count('X');
    }
}
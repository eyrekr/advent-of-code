package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.EGrid.It;
import com.github.eyrekr.mutable.State;
import com.github.eyrekr.raster.Direction;

class D16 extends Aoc {

    final EGrid grid;

    D16(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        final It start = grid.where(Symbol.StartTile).first().setDirection(Direction.Right);
        final Arr<It> queue = Arr.of(start);
        while (queue.isNotEmpty()) {
            final It it = queue.removeFirst();
            if(it.is(State.Closed)) continue;
            it.setState(State.Closed);
            Arr.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
        }
        return grid.where(Symbol.EndTile).first().value();
    }

    @Override
    public long star2() {
        return -1L;
    }

    interface Symbol {
        char Wall = '#';
        char Empty = '.';
        char StartTile = 'S';
        char EndTile = 'E';
    }
}
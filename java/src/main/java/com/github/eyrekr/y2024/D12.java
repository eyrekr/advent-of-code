package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.State;
import com.github.eyrekr.raster.Direction;

class D12 extends Aoc {

    final Seq<Direction> directions = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
    final EGrid<?> grid;

    D12(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        return grid.scan(sc -> sc.is(State.Unseen)).reduce(0L, (sum, sc) -> sum + flood(sc));
    }

    long flood(final EGrid.Sc start) {
        final char plant = start.symbol();
        long area = 0, perimeter = 0;
        final Arr<EGrid.It> queue = Arr.of(start.it());
        while (queue.isNotEmpty()) {
            final var it = queue.removeFirst();
            if (!it.is(State.Unseen)) continue;
            it.set(State.Closed);
            final var neighbours = directions.where(direction -> it.la(direction) == plant);
            area++;
            perimeter += (4 - neighbours.length);
            neighbours
                    .map(direction -> it.duplicate().set(direction).go())
                    .where(n -> n.is(State.Unseen))
                    .each(queue::addLast);
        }
        start.set(area * perimeter);
        return area * perimeter;
    }
}
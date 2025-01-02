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
        return grid
                .scan(sc -> sc.is(State.Unseen))
                .reduce(0L, (sum, sc) -> sum + flood(sc, Fence.StandardPerimeter));
    }

    @Override
    public long star2() {
        return grid
                .scan(sc -> sc.is(State.Unseen))
                .reduce(0L, (sum, sc) -> sum + flood(sc, Fence.DiscountPerimeter));
    }

    long flood(final EGrid.Sc start, final Fence fence) {
        final char plant = start.symbol();
        long area = 0, perimeter = 0;
        final Arr<EGrid.It> queue = Arr.of(start.it());
        while (queue.isNotEmpty()) {
            final var it = queue.removeFirst();
            if (!it.is(State.Unseen)) continue;
            it.set(State.Closed);
            area++;
            perimeter += fence.perimeter(
                    it.la(Direction.UpLeft) == plant,
                    it.la(Direction.Up) == plant,
                    it.la(Direction.UpRight) == plant,
                    it.la(Direction.Left) == plant,
                    it.la(Direction.Right) == plant,
                    it.la(Direction.DownLeft) == plant,
                    it.la(Direction.Down) == plant,
                    it.la(Direction.DownRight) == plant);
            directions.where(direction -> it.la(direction) == plant)
                    .map(direction -> it.duplicate().set(direction).go())
                    .where(n -> n.is(State.Unseen))
                    .each(queue::addLast);
        }
        start.set(area * perimeter);
        return area * perimeter;
    }

    @FunctionalInterface
    interface Fence {
        Fence StandardPerimeter = (a, b, c, d, e, f, g, h) ->
                (b ? 0 : 1) + (d ? 0 : 1) + (e ? 0 : 1) + (g ? 0 : 1);
        Fence DiscountPerimeter = (a, b, c, d, e, f, g, h) ->
                (b || !a && !b && d ? 0 : 1) + (g || d && !f && !g ? 0 : 1) + (d || !a && b && !d ? 0 : 1) + (e || b && !c && !e ? 0 : 1);

        long perimeter(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h);
    }
}
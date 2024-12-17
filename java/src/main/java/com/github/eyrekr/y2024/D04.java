package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.raster.Direction;

class D04 extends Aoc {

    final Grid grid;

    D04(final String input) {
        grid = Grid.of(input);
    }

    @Override
    public long star1() {
        final Seq<Direction> directions = Seq.of(
                Direction.UpLeft, Direction.Up, Direction.UpRight,
                Direction.Left, Direction.Right,
                Direction.DownLeft, Direction.DownRight, Direction.Down);

        return grid.reduce(
                0L,
                (sum, it) -> sum + directions.countWhere(direction -> it.checkAhead(direction, "XMAS")));
    }

    @Override
    public long star2() {
        final Longs patterns = Longs.of(
                code('M', 'M', 'S', 'S'),
                code('M', 'S', 'M', 'S'),
                code('S', 'S', 'M', 'M'),
                code('S', 'M', 'S', 'M'));

        return grid
                .replace(ch -> switch (ch) {
                    case 'M', 'A', 'S' -> ch;
                    default -> '.';
                })
                .collect('A')
                .map(it -> it.neighbours8)
                .map(n8 -> code(n8[0], n8[2], n8[5], n8[7]))
                .countWhere(patterns::has);
    }

    static int code(final char c0, final char c1, final char c2, final char c3) {
        return (c0 << 24) | (c1 << 16) | (c2 << 8) | (c3);
    }
}
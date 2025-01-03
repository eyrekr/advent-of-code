package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

class D15 extends Aoc {

    interface Symbol {
        char Wall = '#';
        char Robot = '@';
        char Box = 'O';
        char Empty = '.';
    }

    final EGrid<?> grid;
    final char[] instructions;

    D15(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        grid = EGrid.fromString(blocks[0]);
        instructions = blocks[1].toCharArray();
    }

    @Override
    public long star1() {
        final var robot = grid.where(it -> it.is(Symbol.Robot)).it;
        for (final char instruction : instructions) {
            final Direction direction = Direction.fromChar(instruction);
            if (direction == Direction.None) continue; // newlines
            robot.setDirection(direction);
        }
        return 0L;
    }

}
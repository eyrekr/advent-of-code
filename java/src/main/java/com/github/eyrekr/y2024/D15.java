package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.EGrid.It;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

class D15 extends Aoc {

    interface Symbol {
        char Wall = '#';
        char Robot = '@';
        char Box = 'O';
        char Empty = '.';
    }

    final EGrid grid;
    final Arr<Direction> instructions;

    D15(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        grid = EGrid.fromString(blocks[0]);
        instructions = Arr.empty();
        for (final char instruction : blocks[1].toCharArray()) {
            final Direction direction = Direction.fromChar(instruction);
            if (direction != Direction.None) instructions.addLast(direction);
        }
    }

    @Override
    public long star1() {
        final var robot = grid.where(Symbol.Robot).it;
        instructions.each(direction -> {
            final boolean robotCanMove = robot
                    .setDirection(direction)
                    .findFirstAhead(Symbol.Empty, Symbol.Wall) == Symbol.Empty;
            if (robotCanMove) moveAndPushBoxes(robot);
        });
        return grid.where(Symbol.Box).sum(it -> 100L * it.y + it.x);
    }

    void moveAndPushBoxes(final It robot) {
        robot.duplicate().goUntil(it -> it.is(Symbol.Empty)).setSymbol(Symbol.Box);
        robot.setSymbol(Symbol.Empty);
        robot.go().setSymbol(Symbol.Robot);
    }

}
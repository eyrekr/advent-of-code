package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

class D15 extends Aoc {

    interface Symbol {
        char Wall = '#';
        char Robot = '@';
        char Box = 'O';
        char BoxL = '[';
        char BoxR = ']';
        char Empty = '.';
    }

    final String inputForGrid;
    final Arr<Direction> instructions;

    D15(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        inputForGrid = blocks[0];
        instructions = Arr.empty();
        for (final char instruction : blocks[1].toCharArray()) {
            final Direction direction = Direction.fromChar(instruction);
            if (direction != Direction.None) instructions.addLast(direction);
        }
    }

    @Override
    public long star1() {
        return answer(EGrid.fromString(inputForGrid), this::moveAndPushBoxes);
    }

    @Override
    public long star2() {
        return answer(EGrid.fromString(twiceAsWide(inputForGrid)), null);
    }

    long answer(final EGrid grid, final Consumer<EGrid.It> move) {
        final var robot = grid.where(Symbol.Robot).it;
        instructions.each(direction -> move.accept(robot.setDirection(direction)));
        return grid.where(Symbol.Box).sum(it -> 100L * it.y + it.x);
    }

    void moveAndPushBoxes(final EGrid.It robot) {
        final var end = robot.duplicate().goUntil(Symbol.Empty, Symbol.Wall);
        if (end.is(Symbol.Empty)) {
            end.setSymbol(Symbol.Box);
            robot.setSymbol(Symbol.Empty);
            robot.go().setSymbol(Symbol.Robot);
        }
    }

    static String twiceAsWide(final String input) {
        final var sb = new StringBuilder();
        for (final char ch : input.toCharArray())
            switch (ch) {
                case Symbol.Wall -> sb.append(Symbol.Wall).append(Symbol.Wall);
                case Symbol.Robot -> sb.append(Symbol.Robot).append(Symbol.Empty);
                case Symbol.Box -> sb.append(Symbol.BoxL).append(Symbol.BoxR);
                case Symbol.Empty -> sb.append(Symbol.Empty).append(Symbol.Empty);
                default -> sb.append(ch);
            }
        return sb.toString();
    }
}
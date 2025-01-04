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
        char BoxLeftEdge = '[';
        char BoxRightEdge = ']';
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
        final EGrid grid = EGrid.fromString(inputForGrid);
        run(grid, this::moveAndPushBoxes);
        return grid.where(Symbol.Box).sum(it -> 100L * it.y + it.x);
    }

    @Override
    public long star2() {
        final EGrid grid = EGrid.fromString(twiceAsWide(inputForGrid));
        run(grid, this::moveAndPushBigBoxes);
        return grid.where(Symbol.BoxLeftEdge).sum(it -> 100L * it.y + it.x);
    }

    void run(final EGrid grid, final Consumer<EGrid.It> move) {
        final var robot = grid.where(Symbol.Robot).it;
        instructions.each(direction -> {
            move.accept(robot.setDirection(direction));
        });
    }

    EGrid.It moveAndPushBoxes(final EGrid.It robot) {
        final var la = robot.la();
        if (la == Symbol.Wall) return robot;
        if (la == Symbol.Empty) return moveRobotForward(robot);

        final var end = robot.duplicate().goUntil(Symbol.Empty, Symbol.Wall);
        if (end.is(Symbol.Empty)) {
            end.setSymbol(Symbol.Box);
            moveRobotForward(robot);
        }
        return robot;
    }

    EGrid.It moveAndPushBigBoxes(final EGrid.It robot) {
        final var la = robot.la();
        if (la == Symbol.Wall) return robot;
        if (la == Symbol.Empty) return moveRobotForward(robot);

        if (robot.dy == 0) { // ← and → is similar to moveAndPushBoxes()
            final var end = robot.duplicate().goUntil(Symbol.Empty, Symbol.Wall);
            if (end.is(Symbol.Empty)) {
                moveBoxes(end.turnAround(), Symbol.BoxLeftEdge, Symbol.BoxRightEdge);
                moveRobotForward(robot);
            }
        } else { // ↑ and ↓ is more complicated
            final var other = la == Symbol.BoxLeftEdge ? Direction.Right : Direction.Left;
            final EGrid.It end = robot.duplicate().go().goWhile(la);
            if (end.is(Symbol.Empty) && end.isAhead(other, Symbol.Empty)) {
                final var otherEnd = end.turnAround().duplicate().go(other);
                moveBoxes(end, la);
                moveBoxes(otherEnd, la == Symbol.BoxLeftEdge ? Symbol.BoxRightEdge : Symbol.BoxLeftEdge);
                moveRobotForward(robot);
            }
        }
        return robot;
    }

    EGrid.It moveRobotForward(final EGrid.It robot) {
        return robot.setSymbol(Symbol.Empty).go().setSymbol(Symbol.Robot);
    }

    EGrid.It moveBoxes(final EGrid.It end, final char... chars) {
        end.doWhile(it -> it.setSymbol(it.la()), it -> it.isAheadOneOf(chars));
        return end.setSymbol(Symbol.Empty);
    }

    static String twiceAsWide(final String input) {
        final var sb = new StringBuilder();
        for (final char ch : input.toCharArray())
            switch (ch) {
                case Symbol.Wall -> sb.append(Symbol.Wall).append(Symbol.Wall);
                case Symbol.Robot -> sb.append(Symbol.Robot).append(Symbol.Empty);
                case Symbol.Box -> sb.append(Symbol.BoxLeftEdge).append(Symbol.BoxRightEdge);
                case Symbol.Empty -> sb.append(Symbol.Empty).append(Symbol.Empty);
                default -> sb.append(ch);
            }
        return sb.toString();
    }
}
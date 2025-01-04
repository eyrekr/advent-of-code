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
        instructions.each(direction -> move.accept(robot.setDirection(direction)));
    }

    EGrid.It moveAndPushBoxes(final EGrid.It robot) {
        final var la = robot.la();
        if (la == Symbol.Wall) return robot;
        if (la == Symbol.Empty) return moveRobotForward(robot);

        final var end = robot.duplicate().go().goWhile(Symbol.Box);
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
            final var end = robot.duplicate().goWhile(Symbol.Box);
            if (end.is(Symbol.Empty)) {
                end.turnAround().doUntil(it -> it.setSymbol(it.la()), Symbol.Robot); // ugly
                moveRobotForward(robot);
            }
        } else { // ↑ and ↓ is more complicated
            final EGrid.It leftEnd = robot.duplicate().go(), rightEnd = robot.duplicate().go();
            if (la == Symbol.BoxLeftEdge) rightEnd.go(Direction.Right);
            if (la == Symbol.BoxRightEdge) leftEnd.go(Direction.Left);
            if (leftEnd.goWhile(Symbol.BoxLeftEdge).is(Symbol.Empty) && rightEnd.goWhile(Symbol.BoxRightEdge).is(Symbol.Empty)) {

                moveRobotForward(robot);
            }
        }
        return robot;
    }

    EGrid.It moveRobotForward(final EGrid.It robot) {
        return robot.setSymbol(Symbol.Empty).go().setSymbol(Symbol.Robot);
    }

    EGrid.It moveBoxes(final EGrid.It start, final EGrid.It end) {
return start;
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
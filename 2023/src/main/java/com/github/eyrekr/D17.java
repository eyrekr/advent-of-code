package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Seq;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;


/**
 * https://adventofcode.com/2023/day/17
 * 1)
 * 2)
 */
class D17 extends AoC {

    final Seq<Direction> directions = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
    final Grid grid = Grid.of(input);

    D17(final String input) {
        super(input);
    }

    long star1() {
        final Set<State> visited = new HashSet<>();
        final PriorityQueue<Step> steps = new PriorityQueue<>(Comparator.comparing(Step::score));
        steps.add(new Step(new State(0, Direction.None, 0), 0L));

        while (isNotEmpty(steps)) {
            final Step current = steps.poll();
            if (visited.contains(current.state)) continue;
            visited.add(current.state);

            if (current.state.i == (grid.m  * grid.n - 1)) return current.score;

            for (final Direction direction : directions) {
                final boolean sameDirectionAsBefore = direction == current.state.direction;
                if (sameDirectionAsBefore && current.state.stepsInTheDirection >= 3) continue;
                if (current.state.direction.isOpposite(direction)) continue;
                final Grid.It it = grid.it(current.state.i);
                it.tryToGo(direction)
                        .map(next -> new Step(
                                new State(
                                        next.i,
                                        direction,
                                        sameDirectionAsBefore ? current.state.stepsInTheDirection + 1 : 1),
                                current.score + next.digit))
                        .ifPresent(steps::add);
            }
        }
        throw new IllegalStateException();
    }


    long star2() {
        return 0L;
    }

    record State(int i, Direction direction, int stepsInTheDirection) {
    }

    record Step(State state, long score) {
    }

}
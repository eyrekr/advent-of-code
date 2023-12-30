package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.immutable.Seq;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;


/**
 * https://adventofcode.com/2023/day/17
 * 1) 684
 * 2) 822
 */
class D17 extends AoC {

    final Seq<Direction> directions = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
    final Grid grid = Grid.of(input);

    D17(final String input) {
        super(input);
    }

    long star1() {
        return solve(0, 3);
    }


    long star2() {
        return solve(4, 10);
    }

    long solve(final int minStepsInOneDirection, final int maxStepsInOneDirection) {
        final Set<State> visited = new HashSet<>();
        final PriorityQueue<Step> steps = new PriorityQueue<>(Comparator.comparing(Step::score));
        steps.add(new Step(new State(0, Direction.None, 0), 0L));

        while (isNotEmpty(steps)) {
            final Step current = steps.poll();
            if (visited.contains(current.state)) continue;
            visited.add(current.state);

            if (current.state.i == (grid.m * grid.n - 1)) return current.score;

            Seq<Direction> allowedDirections;
            if (current.state.direction == Direction.None) { // we are at the start [0,0]
                allowedDirections = Seq.of(Direction.Down, Direction.Right);
            } else if (current.state.stepsInTheDirection < minStepsInOneDirection) { // we cannot change direction
                allowedDirections = Seq.of(current.state.direction);
            } else { // we can go anywhere, but we cannot turn around
                allowedDirections = directions.removeFirst(current.state.direction.opposite());
            }
            if (current.state.stepsInTheDirection >= maxStepsInOneDirection) { // we must change direction
                allowedDirections = allowedDirections.removeFirst(current.state.direction);
            }

            for (final Direction direction : allowedDirections) {
                final boolean sameDirectionAsBefore = direction == current.state.direction;
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

    record State(int i, Direction direction, int stepsInTheDirection) {
    }

    record Step(State state, long score) {
    }

}
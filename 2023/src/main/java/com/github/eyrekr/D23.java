package com.github.eyrekr;

import com.github.eyrekr.graph.Gr;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.mutable.Grid.It;
import com.github.eyrekr.mutable.Grid.State;
import com.github.eyrekr.raster.Direction;

/**
 * https://adventofcode.com/2023/day/23
 * 1) 2070
 * 2)
 */
class D23 extends AoC {

    final Grid grid;

    D23(final String input) {
        super(input);
        this.grid = Grid.of(input, true);
    }

    @Override
    long star1() {
        final It start = grid.first('.'), end = grid.last('.');
        final Seq<It> crossroads = grid.collect(it -> !it.wall && it.neighbours().length >= 3).addFirst(end).addFirst(start);
        final Gr<Integer> graph = Gr.empty();

        for (final It source : crossroads) {
            if (source.i == end.i) continue; // nothing emanates from the END, but END is still a crossroads
            for (final It next : source.neighbours(this::isInValidDirection)) {
                It current = next;
                int distance = 1;
                while (crossroads.noneMatch(current)) {
                    current.setState(State.Closed); // we cannot close crossroads, we would never re-enter them
                    current = current.neighbours(neighbour -> neighbour.i != source.i && neighbour.state != State.Closed).value;
                    distance++;
                }

                graph.addEdge(source.i, current.i, -distance);
            }
        }
        return -graph.distance_BellmanFordMoore(start.i, end.i); //
    }

    boolean isInValidDirection(final It neighbour) {
        return neighbour.state != State.Closed && switch (neighbour.ch) {
            case '^' -> neighbour.direction == Direction.Up;
            case 'v' -> neighbour.direction == Direction.Down;
            case '>' -> neighbour.direction == Direction.Right;
            case '<' -> neighbour.direction == Direction.Down;
            default -> true;
        };
    }

    @Override
    long star2() {
        return 0L;
    }

}

package com.github.eyrekr;

import com.github.eyrekr.graph.Gr;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.mutable.Grid.It;
import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;

import java.util.Objects;

/**
 * https://adventofcode.com/2023/day/23
 * 1)
 * 2)
 */
class D23 extends AoC {

    final Seq<Direction> directions = Seq.of(Direction.Down, Direction.Right, Direction.Up, Direction.Left);

    final Grid grid;

    D23(final String input) {
        super(input);
        this.grid = Grid.of(input, true);
    }

    @Override
    long star1() {
        final It start = grid.first('.'), end = grid.last('.');
        final Seq<It> waypoints = grid.collect(it -> it.ch == '.' && it.neighbours().length >= 3).addFirst(start).addFirst(end);

        final Gr<Integer> graph = Gr.empty();
        graph.addVertex(start.i, "ENTRY").addVertex(end.i, "EXIT");
        for (final It source : waypoints) {
            if (Objects.equals(source, end)) continue;
            directions
                    .where(direction -> isValidDirection(source, direction))
                    .map(direction -> findNextWaypoint(waypoints, source, direction))
                    .each(edge -> graph.addEdge(edge.a, edge.b, -edge.distance));
        }

        return -graph.distance_BellmanFordMoore(start.i, end.i);
    }

    Edge findNextWaypoint(final Seq<It> waypoints, final It source, final Direction direction) {
        It current = source.go(direction);
        int distance = 1, previous = source.i;
        while (!waypoints.has(current)) {
            final int forbidden = previous;
            previous = current.i;
            current = current.neighbours().where(it -> it.i != forbidden).value;
            distance++;
        }
        Out.print("%d -> %d = %d\n", source.i, current.i, distance);
        return new Edge(source.i, current.i, distance);
    }

    boolean isValidDirection(final It source, final Direction direction) {
        final It next = source.go(direction);
        if (next == null) return false;
        return next.ch == '.' || switch (direction) {
            case Up -> next.ch == '^';
            case Down -> next.ch == 'v';
            case Left -> next.ch == '<';
            case Right -> next.ch == '>';
            default -> false;
        };
    }

    @Override
    long star2() {
        return 0L;
    }

    record Edge(int a, int b, int distance) {
    }
}

package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Grid.It;
import com.github.eyrekr.util.Seq;

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
        this.grid = Grid.of(input);
    }

    @Override
    long star1() {
        final It start = grid.chFirst(ch -> ch == '.'), end = grid.chLast(ch -> ch == '.');
        final Seq<It> waypoints = waypoints().addFirst(start).addFirst(end);
        waypoints.each(it -> it.set(it.ch, 0, true));

        Seq<Edge> edges = Seq.empty();
        for (final It source : waypoints) {
            if (Objects.equals(source, end)) continue;
            final Seq<Edge> e = directions
                    .where(direction -> isValidDirection(source, direction))
                    .map(direction -> findNextWaypoint(waypoints, source, source.go(direction), direction, 1));
            edges = edges.addSeq(e);
        }

        edges.print("\n");
        return 0L;
    }

    Seq<It> waypoints() {
        return grid.where(it -> it.ch == '.' && it.neighbours().where(i -> i.ch != '#').length >= 3);
    }

    Edge findNextWaypoint(
            final Seq<It> waypoints,
            final It source,
            final It current,
            final Direction direction,
            final int distance) {
        if (waypoints.has(current)) return new Edge(source.i, current.i, distance);
        final Direction forward = directions
                .where(d -> d != direction.opposite()) // do not go back
                .where(d -> isValidDirection(current, d))
                .value;
        return findNextWaypoint(waypoints, source, current.go(forward), forward, distance + 1);
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

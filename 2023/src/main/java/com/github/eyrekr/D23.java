package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Grid.It;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

import java.util.LinkedList;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

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
        final Seq<Integer> waypoints = waypoints().addFirst(start.i).addFirst(end.i);

        final LinkedList<State> queue = new LinkedList<>();
        queue.add(new State(start, start, Direction.None, 0));

        Seq<Edge> edges = Seq.empty();
        while (isNotEmpty(queue)) {
            final State state = queue.removeFirst();
            final boolean thisIsTheNextWaypoint = state.current.i != state.start.i && waypoints.has(state.current.i);
            if (thisIsTheNextWaypoint) edges = edges.addFirst(new Edge(state.start.i, state.current.i, state.distance));
            state.current.set(state.current.ch, state.distance, true);
            directions
                    .where(direction -> direction != state.in.opposite())
                    .where(direction -> {
                        final char next = grid.at(state.current.x + direction.dx, state.current.y + direction.dy);
                        return next == '.' || switch (direction) {
                            case Up -> next == '^';
                            case Down -> next == 'v';
                            case Left -> next == '<';
                            case Right -> next == '>';
                            default -> false;
                        };
                    })
                    .map(direction -> thisIsTheNextWaypoint
                            ? new State(state.current, state.current.go(direction), direction, 1)
                            : new State(state.start, state.current.go(direction), direction, state.distance + 1))
                    .each(queue::addLast);

            Str.print("\n=====\n");
            grid.print(it -> {
                if (it.ch == '#') return "#";
                if (it.i == state.current.i) return "@R*@@";
                if(waypoints.has(it.i)) return "@c*@@";
                if(queue.stream().anyMatch(s->s.current.i ==it.i)) return "@bO@@";
                return ""+it.ch;
            });
        }


        edges.print("\n");
        return 0L;
    }

    Seq<Integer> waypoints() {
        return grid.where(it -> it.ch == '.' && it.neighbours().where(i -> i.ch != '#').length >= 3).map(it -> it.i);
    }

    @Override
    long star2() {
        return 0L;
    }

    record State(It start, It current, Direction in, int distance) {
    }

    record Edge(int a, int b, int distance) {
    }
}

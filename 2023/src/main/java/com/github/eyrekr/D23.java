package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.Direction;
import com.github.eyrekr.util.Grid.It;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
        Map<Integer, String> names = waypoints.reduceWith(
                Seq.range(0, waypoints.length),
                new HashMap<>(),
                (map, i, index) ->{
                    map.put(i, "↓↓ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(index) + "");
                    return map;
                });

        final LinkedList<State> queue = new LinkedList<>();
        queue.add(new State(start, start.go(Direction.Down), 1));

        Seq<Edge> edges = Seq.empty();
        while (isNotEmpty(queue)) {
            final State state = queue.removeFirst();
            final boolean thisIsTheNextWaypoint = state.current.i != state.start.i && waypoints.has(state.current.i);
            if (thisIsTheNextWaypoint) {
                edges = edges.addFirst(new Edge(state.start.i, state.current.i, state.distance));
                Str.print("%s->%s [%d]\n", names.get(edges.value.a), names.get(edges.value.b), edges.value.distance);
            }

            if (!state.current.b) {
                final Seq<Direction> outgoingUnvisitedDirections = directions.where(direction -> {
                            final It next = state.current.go(direction);
                            if (next == null) return false;
                            if (next.b) return false;
                            return next.ch == '.' || switch (direction) {
                                case Up -> next.ch == '^';
                                case Down -> next.ch == 'v';
                                case Left -> next.ch == '<';
                                case Right -> next.ch == '>';
                                default -> false;
                            };
                        });
                if(!thisIsTheNextWaypoint) { // just continue in that one direction
                    outgoingUnvisitedDirections.map(state.current::go).map(next-> new State(state.start, next, state.distance+1)).each(queue::addLast);
                } else if(!grid.it(state.current.i).b) { // the waypoint was not visited yet
                    outgoingUnvisitedDirections.map(state.current::go).map(next-> new State(state.current, next, 1)).each(queue::addLast);
                }
            }
            state.current.set(state.current.ch, state.distance, true);


            Str.print("\n=====\n");
            grid.print(it -> {
                if (it.ch == '#') return "@W#@@";
                if (it.i == state.current.i) return "@R*@@";
                if (waypoints.has(it.i)) return "@G**" + names.get(it.i) + "**@@";
                if (queue.stream().anyMatch(s -> s.current.i == it.i)) return "@bO@@";
                if(it.d>0) return "@b:@@";
                return "@w" + it.ch +"@@";
            });
        }


        edges.print("\n", e-> names.get(e.a)+ "->"+names.get(e.b) + " [" + e.distance+"]");
        return 0L;
    }

    Seq<Integer> waypoints() {
        return grid.where(it -> it.ch == '.' && it.neighbours().where(i -> i.ch != '#').length >= 3).map(it -> it.i);
    }

    @Override
    long star2() {
        return 0L;
    }

    record State(It start, It current, int distance) {
    }

    record Edge(int a, int b, int distance) {
    }
}

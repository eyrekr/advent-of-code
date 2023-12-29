package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Seq;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * https://adventofcode.com/2023/day/23
 * 1)
 * 2)
 */
class D23 extends AoC {

    final Grid.Direction[] directions = new Grid.Direction[]{Grid.Direction.Down, Grid.Direction.Right, Grid.Direction.Up, Grid.Direction.Left};

    final Grid grid;

    D23(final String input) {
        super(input);
        this.grid = Grid.of(input);
    }

    @Override
    long star1() {
        final Grid.It start = grid.chFirst(ch -> ch == '.'), end = grid.chLast(ch -> ch == '.');
        start.set('*', 0, true);

        final Set<Integer> enqueued = new HashSet<>();
        enqueued.add(start.i);
        final LinkedList<Integer> queue = new LinkedList<>();
        queue.add(start.i);

        Seq<Edge> edges = Seq.empty();
        while (isNotEmpty(queue)) {
            final int source = queue.removeFirst();

            Grid.It it = grid.it(source);
            int distance = 0;
            do {
                it.set(it.ch, distance, true);
                if(it.i == end.i) { // end
                    edges = edges.addFirst(new Edge(source, it.i, distance));
                    break;
                }
                final var n4 = it.unvisitedNeighbours().where(q -> q.ch != '#');
                if (n4.length == 0) break; // dead end
                else if (n4.length == 1) { // continue
                    it = n4.value;
                    distance++;
                } else { // waypoint
                    edges = edges.addFirst(new Edge(source, it.i, distance));
                    if (!enqueued.contains(it.i)) queue.add(it.i);
                    break;
                }
            } while (true);
        }

        edges.print();
        return 0L;
    }

    boolean isWaypoint(final Grid.It it) {
        if (it.ch != '.') return false;
        int waysOut = 0;
        for (final char ch : it.neighbours4) {
            waysOut += switch (ch) {
                case '<', '>', 'v', '^', '.' -> 1;
                default -> 0;
            };
        }
        return waysOut >= 3;
    }

    @Override
    long star2() {
        return 0L;
    }

    record State(int i, Grid.Direction direction) {
    }

    record Edge(int a, int b, int distance) {
    }
}

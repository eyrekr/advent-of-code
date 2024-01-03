package com.github.eyrekr;

import com.github.eyrekr.graph.Gr;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.mutable.Grid.It;
import com.github.eyrekr.mutable.Grid.State;

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
            for (final It next : source.neighbours(neighbour -> neighbour.state != State.Closed && neighbour.symbolMatchesDirection)) {
                It current = next;
                int distance = 1;
                while (crossroads.noneMatch(current)) {
                    current.setState(State.Closed).set(current.direction.ch); // we cannot close crossroads, we would never re-enter them
                    current = current.neighbours(neighbour -> neighbour.i != source.i && neighbour.state != State.Closed).value;
                    distance++;
                }

                graph.addEdge(source.i, current.i, -distance);
            }
        }

        grid.print();
        // in directed acyclic graphs the MAX(path) = -MIN(-path)
        return -graph.minDistance_BellmanFordMoore(start.i, end.i);
    }

    @Override
    long star2() {
        final It start = grid.first('.'), end = grid.last('.');
        final Seq<It> crossroads = grid.collect(it -> !it.wall && it.neighbours().length >= 3).addFirst(end).addFirst(start);
        final Gr<Integer> graph = Gr.empty();

        for (final It source : crossroads) {
            if (source.i == end.i) continue;
            for (final It next : source.neighbours(neighbour -> neighbour.state != State.Closed && neighbour.symbolMatchesDirection)) {
                It current = next;
                int distance = 1;
                while (crossroads.noneMatch(current)) {
                    current.setState(State.Closed).set(current.direction.ch);
                    current = current.neighbours(neighbour -> neighbour.i != source.i && neighbour.state != State.Closed).value;
                    distance++;
                }

                graph.addEdge(source.i, current.i, distance).addEdge(current.i, source.i, distance); // bidirectional
            }
        }

        grid.print(it-> switch(it.ch){
            case '.' -> "@r*";
            case '#' -> "@W#";
            default-> "@C" + it.ch;
        });
        // in directed acyclic graphs the MAX(path) = -MIN(-path)
        return graph.minDistance_BellmanFordMoore(start.i, end.i);
    }

}

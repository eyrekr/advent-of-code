package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.EGrid.It;
import com.github.eyrekr.mutable.Hp;
import com.github.eyrekr.mutable.State;
import com.github.eyrekr.raster.Direction;

import java.util.function.Predicate;

class D16 extends Aoc {

    final Seq<Direction> directions = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
    final Predicate<It> unprocessed = it -> it.isNot(State.Closed) && it.isNot(Symbol.Wall);
    final EGrid grid;

    D16(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        final Hp<It> priorityQueue = Hp.empty();
        final It start = grid.where(Symbol.StartTile).first().setDirection(Direction.Right);
        priorityQueue.add(0L, start);
        while (priorityQueue.isNotEmpty()) priorityQueue
                .removeFirst()
                .where(unprocessed)
                .ifPresent(source -> directions
                        .map(direction -> source.clone().setDirection(direction))
                        .where(unprocessed)
                        .each(neighbour -> {
                            final long score =source.hasSameDirectionAs(neighbour) ? 1 : 1001;
                            if(score < neighbour.value()) neighbour.setValue(score);
                            priorityQueue.add(score, neighbour); // this here is suspicious!
                        }));
        return grid.where(Symbol.EndTile).first().value();
    }

    @Override
    public long star2() {
        return -1L;
    }

    interface Symbol {
        char Wall = '#';
        char Empty = '.';
        char StartTile = 'S';
        char EndTile = 'E';
    }
}
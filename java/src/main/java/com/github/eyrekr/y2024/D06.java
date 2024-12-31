package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;

class D06 extends Aoc {

    interface Symbol {
        char Up = '^';
        char Mark = 'X';
        char Wall = '#';
        char Empty = '.';
        char Obstacle = 'O';
    }

    final EGrid<?> grid;
    final EGrid<?>.It guard;

    D06(final String input) {
        this.grid = EGrid.fromString(input);
        guard = grid.scan(it -> it.is(Symbol.Up)).set(Direction.Up).it();
    }

    @Override
    public long star1() {
        guard.duplicate().goWhile(it -> {
            if (it.outside()) return false;
            while (it.la(Symbol.Wall)) it.turnRight();
            it.set(Symbol.Mark);
            return true;
        });
        return grid.scan(it -> it.is(Symbol.Mark)).count();
    }

    @Override
    public long star2() {
        guard.duplicate().goWhile(it -> {
            if (it.outside()) return false;
            while (it.la(Symbol.Wall)) it.turnRight();
            if (it.x != guard.x || it.y != guard.y) it.set(Symbol.Mark);
            return true;
        });

        final var guardPath = grid.scan(it -> it.is(Symbol.Mark)).collect();

        for (final var obstacle : guardPath) {
            obstacle.set(Symbol.Wall);

            final var visited = new HashSet<Location>();
            final var pathEnded = guard.duplicate().goWhile(it -> {
                if (it.outside()) return false;
                while (it.la(Symbol.Wall)) it.turnRight();

                final var location = new Location(it);
                if (visited.contains(location)) return false;
                visited.add(location);

                return true;
            });

            if (pathEnded.inside()) obstacle.set(Symbol.Obstacle);
            else obstacle.set(Symbol.Empty);
        }

        return grid.scan(it -> it.is(Symbol.Obstacle)).count();
    }

    record Location(int x, int y, Direction direction) {
        Location(final EGrid.It it) {
            this(it.x, it.y, it.direction);
        }
    }
}
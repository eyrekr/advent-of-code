package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;

class D06 extends Aoc {

    interface Symbol {
        char Guard = '^';
        char Mark = 'X';
        char Wall = '#';
        char Empty = '.';
        char Obstacle = 'O';
    }

    final EGrid grid;
    final EGrid.It guard;

    D06(final String input) {
        this.grid = EGrid.fromString(input);
        guard = grid.where(Symbol.Guard).it.setDirection(Direction.Up);
    }

    @Override
    public long star1() {
        guard.duplicate().goWhile(it -> {
            if (it.outside()) return false;
            while (it.isAhead(Symbol.Wall)) it.turnRight();
            it.setSymbol(Symbol.Mark);
            return true;
        });
        return grid.where(Symbol.Mark).count();
    }

    @Override
    public long star2() {
        guard.duplicate().goWhile(it -> {
            if (it.outside()) return false;
            while (it.isAhead(Symbol.Wall)) it.turnRight();
            if (it.x != guard.x || it.y != guard.y) it.setSymbol(Symbol.Mark);
            return true;
        });

        final var guardPath = grid.where(Symbol.Mark).collect();

        for (final var obstacle : guardPath) {
            obstacle.setSymbol(Symbol.Wall);

            final var visited = new HashSet<Location>();
            final var pathEnded = guard.duplicate().goWhile(it -> {
                if (it.outside()) return false;
                while (it.isAhead(Symbol.Wall)) it.turnRight();

                final var location = new Location(it);
                if (visited.contains(location)) return false;
                visited.add(location);

                return true;
            });

            if (pathEnded.inside()) obstacle.setSymbol(Symbol.Obstacle);
            else obstacle.setSymbol(Symbol.Empty);
        }

        return grid.where(Symbol.Obstacle).count();
    }

    record Location(int x, int y, int dx, int dy) {
        Location(final EGrid.It it) {
            this(it.x, it.y, it.dx, it.dy);
        }
    }
}
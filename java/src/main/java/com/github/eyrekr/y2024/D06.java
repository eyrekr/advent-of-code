package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Grid2;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;
import java.util.Set;

class D06 extends Aoc {

    interface Symbol {
        char Up = '^';
        char Track = 'X';
        char Wall = '#';
        char Empty = '.';
        char Obstacle = 'O';
    }

    final Grid2 grid;
    final Grid2.It guard;

    D06(final String input) {
        this.grid = Grid2.fromString(input);
        guard = grid.start().scanUntil(it -> it.is(Symbol.Up)).turn(Direction.Up);
    }

    @Override
    public long star1() {
        guard.goWhile(it -> {
            it.set(Symbol.Track);
            if (it.la(Symbol.Wall)) it.turnRight();
            return it.inside;
        });
        return grid.start().count(it -> it.is(Symbol.Track));
    }

    @Override
    public long star2() {
        record Point(int x, int y) {
        }
        final Set<Point> path = new HashSet<>();
        guard.deepCopy().goWhile(it -> {
            if (it.outside) return false;
            if (guard.x != it.x || guard.y != it.y) path.add(new Point(it.x, it.y));
            while (it.la(Symbol.Wall)) it.turnRight();
            return true;
        });

        long obstacles = 0;
        for (final Point p : path) {
            final var obstacle = grid.it(p.x, p.y);
            obstacle.set(Symbol.Wall);
            if (hasCycle()) obstacles++;
            obstacle.set(Symbol.Empty);
        }
        return obstacles;
    }

    private boolean hasCycle() {
        record Location(int x, int y, Direction direction) {
            static Location from(final Grid2.It it) {
                return new Location(it.x, it.y, it.direction);
            }
        }

        final Set<Location> visited = new HashSet<>();
        return guard.deepCopy().goUntil(it -> {
            if (it.outside) return true;

            final var l1 = Location.from(it);
            if (visited.contains(l1)) return true;
            else visited.add(l1);

            while (it.la(Symbol.Wall)) {
                it.turnRight();

                var l2 = Location.from(it);
                if (visited.contains(l2)) return true;
                else visited.add(l2);
            }

            return false;
        }).inside;
    }
}
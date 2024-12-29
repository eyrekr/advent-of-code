package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.Grid2;
import com.github.eyrekr.raster.Direction;

import java.util.HashSet;

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
        guard.duplicate().goWhile(it -> {
            if (it.outside) return false;
            while (it.la(Symbol.Wall)) it.turnRight();
            it.set(Symbol.Track);
            return true;
        });
        return grid.start().count(it -> it.is(Symbol.Track));
    }

    @Override
    public long star2() {
        star1();

        final var path = grid.start().reduce(
                Arr.<Grid2.It>empty(),
                (arr, it) -> it.is(Symbol.Track) ? arr.addLast(it.duplicate()) : arr);

        long result = 0;
        for (final var obstacle : path) {
            obstacle.set(Symbol.Wall);

            final var visited = new HashSet<Location>();
            final var end = guard.duplicate().goWhile(it -> {
                if (it.outside) return false;
                while (it.la(Symbol.Wall)) it.turnRight();

                final var location = Location.from(it);
                if (visited.contains(location)) return false;
                visited.add(location);

                return true;
            });

            if (end.inside) result++;

            obstacle.set(Symbol.Empty);
        }

        return result;
    }


    record Location(int x, int y, Direction direction) {
        static Location from(final Grid2.It it) {
            return new Location(it.x, it.y, it.direction);
        }
    }
}
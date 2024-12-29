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

    D06(final String input) {
        this.grid = Grid2.fromString(input);
    }

    @Override
    public long star1() {
        grid.start().scanUntil(it -> it.is(Symbol.Up))
                .turn(Direction.Up)
                .goWhile(it -> {
                    it.set(Symbol.Track);
                    if (it.la(Symbol.Wall)) it.turnRight();
                    return it.inside;
                });
        return grid.start().count(it -> it.is(Symbol.Track));
    }

    @Override
    public long star2() { // 1899 - 1965 ; 1901 to taky neni
        record Location(int x, int y, Direction direction) {
        }

        grid.start().scanUntil(it -> it.is(Symbol.Up))
                .turn(Direction.Up)
                .goWhile(it -> {
                    if (it.la(Symbol.Wall)) it.turnRight();
                    /*if (it.la(Symbol.Empty))*/
                    {
                        final Grid2.It obstacle = it.deepCopy().go();
                        final char symbol = obstacle.symbol();
                        obstacle.set(Symbol.Wall);

                        final Set<Location> visited = new HashSet<>();
                        final Grid2.It end = it.deepCopy().goUntil(forked -> {
                            if (forked.outside) return true;
                            if (forked.la(Symbol.Wall)) forked.turnRight();
                            final Location location = new Location(forked.x, forked.y, forked.direction);
                            final boolean knownLocation = visited.contains(location);
                            visited.add(location);
                            return knownLocation;
                        });

                        if (end.inside) obstacle.set(Symbol.Track);
                        else obstacle.set(symbol);
                    }

                    return it.inside;
                });

        return grid.print().start().count(it -> it.is(Symbol.Track));
    }
}
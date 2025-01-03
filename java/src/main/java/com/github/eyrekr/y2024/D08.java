package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.mutable.EGrid.It;

import java.util.HashMap;
import java.util.Map;

class D08 extends Aoc {

    interface Symbol {
        char Empty = '.';
        char Antinode = '#';
    }

    final EGrid<?> grid;
    final Map<Character, Arr<Antenna>> antennasByFrequency = new HashMap<>();

    D08(final String input) {
        grid = EGrid.fromString(input);
        grid.where(it -> !it.is(Symbol.Empty))
                .each(it -> antennasByFrequency.computeIfAbsent(it.ch(), unused -> Arr.empty())
                        .addLast(new Antenna(it)));
    }

    @Override
    public long star1() {
        antennasByFrequency.forEach((frequency, antennas) -> {
            for (final Antenna a : antennas)
                for (final Antenna b : antennas)
                    if (a != b) {
                        final int dx = a.x - b.x, dy = a.y - b.y, x = a.x + dx, y = a.y + dy;
                        final var it = grid.it(x, y);
                        it.setSymbol(Symbol.Antinode);
                    }
        });
        return grid.where(it -> it.is(Symbol.Antinode)).count();
    }

    @Override
    public long star2() {
        antennasByFrequency.forEach((frequency, antennas) -> {
            for (final Antenna a : antennas)
                for (final Antenna b : antennas)
                    if (a != b) {
                        final int dx = a.x - b.x, dy = a.y - b.y;
                        for (final var it = grid.it(a.x, a.y, dx, dy); it.inside(); it.go())
                            it.setSymbol(Symbol.Antinode);
                    }
        });
        return grid.where(it -> it.is(Symbol.Antinode)).count();
    }

    record Antenna(int x, int y) {
        Antenna(It it) {
            this(it.x, it.y);
        }
    }
}
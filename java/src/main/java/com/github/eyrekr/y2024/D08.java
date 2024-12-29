package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.mutable.Grid2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class D08 extends Aoc {

    interface Symbol {
        char Empty = '.';
        char Antinode = '#';
    }

    final Grid2 grid;

    D08(final String input) {
        grid = Grid2.fromString(input);
    }

    @Override
    public long star1() {
        final Map<Character, Arr<Antenna>> antennasByFrequency = new HashMap<>();
        grid.scan(it -> !it.is(Symbol.Empty))
                .each(it -> antennasByFrequency.computeIfAbsent(it.symbol(), unused -> Arr.empty())
                        .addLast(new Antenna(it)));
        antennasByFrequency.forEach((frequency, antennas) -> {
            for (final Antenna a : antennas)
                for (final Antenna b : antennas)
                    if (!Objects.equals(a, b)) {
                        final int dx = a.x - b.x, dy = a.y - b.y, x = a.x + dx, y = a.y + dy;
                        final var it = grid.it(x, y);
                        it.set(Symbol.Antinode);
                    }
        });
        return grid.print().scan(it -> it.is(Symbol.Antinode)).count();
    }

    record Antenna(int x, int y) {
        Antenna(Grid2.Sc it) {
            this(it.x, it.y);
        }
    }
}
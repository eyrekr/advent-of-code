package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.EGrid;
import com.github.eyrekr.raster.Direction;

class D07 extends Aoc {

    final EGrid grid;

    D07(final String input) {
        grid = EGrid.fromString(input);
    }

    @Override
    public long star1() {
        emitTachyonBeams();
        return grid.where(it -> it.is(Symbol.Splitter) && it.isAhead(Direction.Up, Symbol.Beam)).count();
    }

    @Override
    public long star2() {
        return -1L;
    }

    void emitTachyonBeams() {
        grid.all().each(it -> {
            switch (it.symbol()) {
                case Symbol.Start -> it.clone().go(Direction.Down).setSymbol(Symbol.Beam);
                case Symbol.Empty -> {
                    if (it.isAhead(Direction.Up, Symbol.Beam)) it.setSymbol(Symbol.Beam);
                }
                case Symbol.Splitter -> {
                    if (it.isAhead(Direction.Up, Symbol.Beam)) {
                        it.clone().go(Direction.Left).setSymbol(Symbol.Beam);
                        it.clone().go(Direction.Right).setSymbol(Symbol.Beam);
                    }
                }
            }
        });
    }

    interface Symbol {
        char Start = 'S';
        char Empty = '.';
        char Splitter = '^';
        char Beam = '|';
    }
}
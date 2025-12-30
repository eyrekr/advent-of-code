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
        emitTachyonBeams();
        return grid.where(EGrid.It::isBottomBorder).sum(EGrid.It::value);
    }

    void emitTachyonBeams() {
        grid.where(Symbol.Start).first().setValue(1);
        
        grid.all().each(it -> {
            final var above = it.clone().go(Direction.Up);
            if (above.isOneOf(Symbol.Beam, Symbol.Start)) {
                switch (it.symbol()) {
                    case Symbol.Empty, Symbol.Beam -> it.setSymbol(Symbol.Beam).incValue(above.value());
                    case Symbol.Splitter -> {
                        it.clone().go(Direction.Left).setSymbol(Symbol.Beam).incValue(above.value());
                        it.clone().go(Direction.Right).setSymbol(Symbol.Beam).incValue(above.value());
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
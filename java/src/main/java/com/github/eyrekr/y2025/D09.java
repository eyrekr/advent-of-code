package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import org.apache.commons.lang3.StringUtils;

class D09 extends Aoc {

    final Arr<P> redTiles;

    D09(final String input) {
        redTiles = Arr.ofLinesFromString(input).map(P::new);
    }

    @Override
    public long star1() {
        return -1L;
    }

    @Override
    public long star2() {
        return -1L;
    }

    static final class P {
        final long x, y;

        P(final String line) {
            final String[] p = StringUtils.split(line, ',');
            this.x = Long.parseLong(p[0]);
            this.y = Long.parseLong(p[1]);
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }
}
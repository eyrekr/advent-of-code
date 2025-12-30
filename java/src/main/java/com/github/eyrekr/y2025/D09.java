package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import org.apache.commons.lang3.StringUtils;

class D09 extends Aoc {

    final Arr<P> redTiles;

    D09(final String input) {
        redTiles = Arr.ofLinesFromString(input).map(P::of);//.sortedBy(P::x);
    }

    @Override
    public long star1() {
        return redTiles.prodUpperTriangleWith(redTiles, P::area).max(Long::compareTo).get();
    }

    @Override
    public long star2() {
        return -1L;
    }

    record P(long x, long y) {
        static P of(final String line) {
            final String[] p = StringUtils.split(line, ',');
            return new P(Long.parseLong(p[0]), Long.parseLong(p[1]));
        }

        long area(final P that) {
            final long width = Math.abs(this.x - that.x) + 1;
            final long height = Math.abs(this.y - that.y) + 1;
            return width * height;
        }
    }
}
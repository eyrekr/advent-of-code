package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import org.apache.commons.lang3.StringUtils;

class D09 extends Aoc {

    final Arr<P> redTiles;

    D09(final String input) {
        redTiles = Arr.ofLinesFromString(input).map(P::fromLine);
    }

    @Override
    public long star1() {
        return redTiles.prodUpperTriangleWith(redTiles, Rectangle::new).mapToLongs(Rectangle::area).max();
    }

    @Override
    public long star2() {
        return redTiles
                .prodUpperTriangleWith(redTiles, Rectangle::new)
                .where(rectangle -> rectangle.inside(redTiles))
                .mapToLongs(Rectangle::area)
                .max();
    }

    record P(long x, long y) {
        static P fromLine(final String line) {
            final String[] p = StringUtils.split(line, ',');
            return new P(Long.parseLong(p[0]), Long.parseLong(p[1]));
        }
    }

    record Rectangle(P p1, P p2) {
        long area() {
            final long width = Math.abs(p1.x - p2.x) + 1;
            final long height = Math.abs(p1.y - p2.y) + 1;
            return width * height;
        }

        boolean inside(final Arr<P> polygon) {
            return true;
        }
    }
}
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

    record Rectangle(long x1, long y1, long x2, long y2) {
        Rectangle(final P p1, final P p2) {
            this(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
        }

        long area() {
            return (x2 - x1 + 1) * (y2 - y1 + 1);
        }

        boolean inside(final Arr<P> polygon) {
            final boolean atLeastOnePointOfPolygonIsInsideRectangle = polygon.atLeastOneIs(this::inside);
            if (atLeastOnePointOfPolygonIsInsideRectangle) return false;



//            for (int i = 0; i <= polygon.length; i++) {
//                final P p1 = polygon.at(i), p2 = polygon.at(i + 1);
//                if(x1 < p1.x && p1.x < x2 && x1 < p2.x && p2.x < x2)
//            }
            return true;
        }

        boolean inside(final P p) {
            return x1 < p.x && p.x < x2 && y1 < p.y && p.y < y2;
        }
    }
}
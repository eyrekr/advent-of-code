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
    public long star2() { //4753531494 too high
        return redTiles
                .prodUpperTriangleWith(redTiles, Rectangle::new)
                .where(this::isRectangleInsidePolygon)
                .mapToLongs(Rectangle::area)
                .max();
    }

    boolean isRectangleInsidePolygon(final Rectangle rectangle) {
        return isPointInsidePolygon(new P(rectangle.x1, rectangle.y1))
                && isPointInsidePolygon(new P(rectangle.x1, rectangle.y2))
                && isPointInsidePolygon(new P(rectangle.x2, rectangle.y1))
                && isPointInsidePolygon(new P(rectangle.x2, rectangle.y2));
    }

    boolean isPointInsidePolygon(final P p) {
        int cuts = 0;
        for (int i = 0; i < redTiles.length; i++) {
            final P a = redTiles.at(i), b = redTiles.at(i + 1);
            if (isPointOnLine(p, a, b)) return true;
            if (isRayCuttingLine(p, a, b)) cuts++;
        }
        return cuts % 2 == 1;
    }

    boolean isPointOnLine(final P p, final P l1, final P l2) {
        if (p.equals(l1) || p.equals(l2)) return true;
        return p.y == l1.y && p.y == l2.y && Math.min(l1.x, l2.x) <= p.x && p.x <= Math.max(l1.x, l2.x)
                || p.x == l1.x && p.x == l2.x && Math.min(l1.y, l2.y) <= p.y && p.y <= Math.max(l1.y, l2.y);
    }

    boolean isRayCuttingLine(final P p, final P l1, final P l2) {
        return l1.y != l2.y && l1.x == l2.x && p.x <= l1.x && Math.min(l1.y, l2.y) <= p.y && p.y <= Math.max(l1.y, l2.y);
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
            final boolean atLeastOnePointOfPolygonIsInsideRectangle = polygon.atLeastOneIs(this::isThePointInside);
            if (atLeastOnePointOfPolygonIsInsideRectangle) return false;

            //at least one edge of the polygon cuts through the rectangle
            for (int i = 0; i <= polygon.length; i++)
                if (cuts(polygon.at(i), polygon.at(i + 1))) return false;


            return true;
        }

        boolean isThePointInside(final P p) {
            return x1 < p.x && p.x < x2 && y1 < p.y && p.y < y2;
        }

        boolean cuts(final P p1, final P p2) {
            final boolean horizontal = p1.y == p2.y;
            return horizontal
                    ? Math.min(p1.x, p2.x) <= x1 && x2 <= Math.max(p1.x, p2.x)
                    : Math.min(p1.y, p2.y) <= y1 && y2 <= Math.max(p1.y, p2.y);
        }
    }
}
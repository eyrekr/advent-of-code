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
                .where(rectangle -> rectangle.isInsidePolygon(redTiles))
                .mapToLongs(Rectangle::area)
                .max();
    }

    record P(long x, long y) {
        static P fromLine(final String line) {
            final String[] p = StringUtils.split(line, ',');
            return new P(Long.parseLong(p[0]), Long.parseLong(p[1]));
        }

        boolean isInsideRectangle(final Rectangle r) {
            return r.x1 < x && x < r.x2 && r.y1 < y && y < r.y2;
        }

        boolean isOnLine(final P a, final P b) {
            return a.x == b.x && x == a.x && Math.min(a.y, b.y) <= y && y <= Math.max(a.y, b.y)
                    ||
                    a.y == b.y && y == a.y && Math.min(a.x, b.x) <= x && x <= Math.max(a.x, b.x);
        }

        P plus(final P p) {
            return new P(x + p.x, y + p.y);
        }

        P plus(final long dx, final long dy) {
            return new P(x + dx, y + dy);
        }
    }

    record Rectangle(long x1, long y1, long x2, long y2) {
        Rectangle(final P p1, final P p2) {
            this(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
        }

        long area() {
            return (x2 - x1 + 1) * (y2 - y1 + 1);
        }

        // This method does not work in general! But for the special shape of the input polygon it does...
        boolean isInsidePolygon(final Arr<P> polygon) {
            final P center = center();
            int edges = 0; // how many edges does a ray from center -----> cut?
            for (int i = 0; i < polygon.length; i++) {
                final P a = polygon.at(i), b = polygon.at(i + 1);

                // if one point of the polygon is inside the rectangle -> false
                if (a.isInsideRectangle(this) || b.isInsideRectangle(this)) return false;

                // at least one edge of the polygon cuts directly through the whole rectangle -> false
                if (isCrosscutByLine(a, b)) return false;

                if (a.y != b.y && center.x <= a.x && Math.min(a.y, b.y) <= center.y && center.y <= Math.max(a.y, b.y))
                    edges++;
            }
            // center of the rectangle is not inside the polygon -> false
            return (edges % 2 != 0);
        }

        boolean isPointInside(final P p) {
            return x1 < p.x && p.x < x2 && y1 < p.y && p.y < y2;
        }

        boolean isCrosscutByLine(final P a, final P b) {
            return a.y == b.y && y1 < a.y && a.y < y2 && Math.min(a.x, b.x) <= x1 && x2 <= Math.max(a.x, b.x)
                    ||
                    a.x == b.x && x1 < a.x && a.x < x2 && Math.min(a.y, b.y) <= y1 && y2 <= Math.max(a.y, b.y);
        }

        P center() {
            return new P((x1 + x2) / 2, (y1 + y2) / 2);
        }
    }
}
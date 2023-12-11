package com.github.eyrekr.util;

/**
 * Computational geometry and computer graphics algorithms
 */
public final class Geo {

    /**
     * Using Bresenham's algorithm to calculate the shortest distance between two points.
     * https://www.baeldung.com/cs/bresenhams-line-algorithm
     *
     * @param a point A
     * @param b point B
     * @return Distance between A and B
     */
    public static long distance(final P a, final P b) {
        long steps = 0L;
        long x1 = b.x, x0 = a.x, y1 = b.y, y0 = a.y;

        final long dx = Math.abs(x1 - x0);
        final long sx = x0 < x1 ? 1 : -1;
        final long dy = -Math.abs(y1 - y0);
        final long sy = y0 < y1 ? 1 : -1;
        long error = dx + dy;

        while (true) {
            steps++;//plot

            if (x0 == x1 && y0 == y1) break;
            final long e2 = 2 * error;
            if (e2 >= dy) {
                if (x0 == x1) break;
                error = error + dy;
                x0 = x0 + sx;
            }
            if (e2 <= dx) {
                if (y0 == y1) break;
                error = error + dx;
                y0 = y0 + sy;
            }
        }
        return steps;
    }

    /**
     * Point in 2D space.
     */
    public static final class P {
        public final long x;
        public final long y;

        private P(final long x, final long y) {
            this.x = x;
            this.y = y;
        }

        public static P of(final long x, final long y) {
            return new P(x, y);
        }

        public long distance(final P that) {
            return Geo.distance(this, that);
        }
    }
}

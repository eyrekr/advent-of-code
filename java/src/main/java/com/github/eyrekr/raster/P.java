package com.github.eyrekr.raster;

public final class P {
    public static final P O = new P(0, 0);

    public final long x;
    public final long y;

    private P(final long x, final long y) {
        this.x = x;
        this.y = y;
    }

    public static P of(final long x, final long y) {
        return new P(x, y);
    }

    public P translate(final long dx, final long dy) {
        return new P(x + dx, y + dy);
    }

    /**
     * Using Bresenham's algorithm to calculate the shortest distance between two points.
     * https://www.baeldung.com/cs/bresenhams-line-algorithm
     */
    public long bresenhamDistance(final P that) {
        long steps = 0L;
        long x1 = that.x, x0 = this.x, y1 = that.y, y0 = this.y;

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

    public long manhattanDistance(final P that) {
        return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final P that && that.x == x && that.y == y;
    }

    @Override
    public int hashCode() {
        return (int) (x ^ y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}

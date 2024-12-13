package com.github.eyrekr.immutable;

/**
 * Interval.
 */
public final class Int {
    public long a, b;

    public Int(long a, long b) {
        this.a = a;
        this.b = b;
    }

    public boolean overlaps(final Int that) {
        return that.a <= this.b && this.a <= that.b;
    }

    public boolean notOverlaps(final Int that) {
        return this.b < that.a || that.b < this.a;
    }

    public boolean contains(final Int that) {
        return this.a <= that.a && that.b <= this.b;
    }

    public boolean notContains(final Int that) {
        return that.a < this.a || this.b < that.b;
    }

    public boolean contains(final long x) {
        return a <= x && x <= b;
    }

    public boolean notContains(final long x) {
        return x < a || b < x;
    }
}

package com.github.eyrekr.immutable;

public final class P {
    public static final P O = new P(0, 0, 0);

    public final long x;
    public final long y;
    public final long z;

    private P(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static P of(final long x, final long y) {
        return new P(x, y, 0);
    }

    public static P of(final long x, final long y, final long z) {
        return new P(x, y, z);
    }

    public P translate(final long dx, final long dy) {
        return new P(x + dx, y + dy, z);
    }

    public P translate(final long dx, final long dy, final long dz) {
        return new P(x + dx, y + dy, z + dz);
    }

    public double distance(final P that) {
        return Math.sqrt((this.x - that.x) * (this.x - that.x) + (this.y - that.y) * (this.y - that.y) + (this.z - that.z) * (this.z - that.z));
    }

    public long manhattanDistance(final P that) {
        return Math.abs(this.x - that.x) + Math.abs(this.y - that.y) + Math.abs(this.z - that.z);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final P that && that.x == x && that.y == y && that.z == z;
    }

    @Override
    public int hashCode() {
        return (int) (x ^ y ^ z);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }
}
package com.github.eyrekr.ilp;

import com.github.eyrekr.math.Algebra;

public final class Q implements Comparable<Q> {
    public static final Q Zero = of(0);
    public static final Q One = of(1);
    public static final Q Infinity = of(Long.MAX_VALUE);

    public final long a;
    public final long b;
    public final boolean zero;
    public final boolean negative;
    public final boolean positive;

    private Q(long a, long b, boolean zero, boolean negative, boolean positive) {
        this.a = a;
        this.b = b;
        this.zero = zero;
        this.negative = negative;
        this.positive = positive;
    }

    public static Q of(final long a) {
        return new Q(a, 1, a == 0, a < 0, a > 0);
    }

    public static Q of(final long a, final long b) {
        if (a == 0) return Zero;
        if (b == 0) throw new ArithmeticException("division by 0 not defined");
        if (b < 0) return of(-a, -b);

        final long gcd = Algebra.gcd(Math.abs(a), Math.abs(b));
        return new Q(a / gcd, b / gcd, a == 0, a * b < 0, a * b > 0);
    }

    public Q abs() {
        return negative ? neg() : this;
    }

    public Q neg() {
        return of(-a, b);
    }

    public Q add(final Q that) {
        return this.b == that.b
                ? of(this.a + that.a, this.b)
                : of(this.a * that.b + that.a * this.b, this.b * that.b);
    }

    public Q mul(final Q that) {
        return of(this.a * that.a, this.b * that.b);
    }


    public Q div(final Q that) {
        return mul(that.inv());
    }

    public Q inv() {
        return of(b, a);
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof Q that && this.a == that.a && this.b == that.b;
    }

    @Override
    public int hashCode() {
        return (int) (a ^ b);
    }

    @Override
    public String toString() {
        return b == 1 ? String.valueOf(a) : a + "/" + b;
    }

    @Override
    public int compareTo(final Q that) {
        return Long.compare(this.a * that.b, that.a * this.b);
    }

    public boolean is(final long a) {
        return this.a == a && this.b == 1;
    }

    public boolean is(final long a, final long b) {
        return this.a == a && this.b == b;
    }

    public boolean is(final Q that) {
        return this.equals(that);
    }

    public boolean lowerThan(final Q that) {
        return compareTo(that) < 0;
    }

    public boolean greaterThan(final Q that) {
        return compareTo(that) > 0;
    }
}

package com.github.eyrekr.math;

public final class Algebra {

    public static final long[] E10 = {
            1L,
            10L,
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };


    //region INTEGER
    public static int gcd(final int a, final int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int lcm(final int a, final int b) {
        return a / gcd(a, b) * b;
    }

    public static int log2(final int n) {
        int log2 = 0;
        while ((n >> log2) != 0) log2++;
        return log2;
    }

    public static int sgn(final int i) {
        return Integer.compare(i, 0);
    }
    //endregion

    //region LONG
    public static long gcd(final long a, final long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long lcm(final long a, final long b) {
        return a / gcd(a, b) * b;
    }

    public static int sgn(final long l) {
        return Long.compare(l, 0);
    }

    public static int log2(final long l) {
        int log2 = 0;
        while ((l >> log2) != 0) log2++;
        return log2;
    }

    public static int decimalDigits(final long n) {
        if (n < 0) return decimalDigits(-n);
        if (n < 10) return 1;
        if (n < 100) return 2;
        if (n < 1000) return 3;
        return decimalDigits(n / 1000) + 3;
    }
    //endregion
}

package com.github.eyrekr.math;

public final class Algebra {

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
    //endregion

    //region LONG
    public static long gcd(final long a, final long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long lcm(final long a, final long b) {
        return a / gcd(a, b) * b;
    }

    public static long sgn(final long l) {
        return l < 0 ? -1 : l == 0 ? 0 : +1;
    }
    //endregion
}

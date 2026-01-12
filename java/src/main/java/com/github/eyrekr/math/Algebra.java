package com.github.eyrekr.math;

import com.github.eyrekr.ilp.Q;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.output.Out;

import java.util.Arrays;

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

    public static int argmin(final int[] a) {
        int argmin = 0;
        for (int i = 0; i < a.length; i++) if (a[i] < a[argmin]) argmin = i;
        return argmin;
    }

    public static int[] map(final int[] a, final MapInt f) {
        for (int i = 0; i < a.length; i++) a[i] = f.apply(i, a[i]);
        return a;
    }

    public static boolean contains(final int[] a, final long x) {
        for (final long l : a) if (l == x) return true;
        return false;
    }

    @FunctionalInterface
    public interface MapInt {
        int apply(int i, int x);
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

    public static Longs allDivisors(final long n) {
        Longs divisors = Longs.of(1);
        for (int i = 2; i <= n / 2; i++) if (n % i == 0) divisors = divisors.addLast(i);
        return divisors.addLast(n);
    }


    public static long[][] gaussJordanEliminationMethod(final long[][] a, final long[] b) {
        final long[][] g = M.appendColumn(a, b); // augmented matrix
        final int m = g.length, n = g[0].length, l = Math.min(m, n - 1);
        final int[] pivot = new int[l]; // which row `i` is used as pivot for column `j` ; we'd generally use row `i` as pivot for column `i`, but the value there can be 0 and thus cannot be used -> we will be using the row that has the smallest absolute value in the pivot column
        // ^^ because of this, we cannot use this version to calculate inverted matrix, because that expects that for pivot row `i` we pick pivot column `i`
        Arrays.fill(pivot, -1);

        M.print(g);

        // Gauss
        for (int j = 0; j < l; j++) {
            final long[] c = M.column(g, j);
            final int p = argmin(map(c, (i, x) -> x == 0 || contains(pivot, i) ? Long.MAX_VALUE : Math.abs(x)));
            pivot[j] = p;
            for (int i = 0; i < m; i++) if (!contains(pivot, i)) reduceRowUsingPivot(g[i], g[p], j);

            Out.print("""
                                                
                            pivot column %2d
                            pivot row    %2d
                                                
                            """,
                    j,
                    p);
            M.print(g);
        }

        // Jordan
        for (int j = l - 1; j > -0; j--)
            for (int i = 0; i < j; i++) reduceRowUsingPivot(g[pivot[i]], g[pivot[j]], j);

        Out.print("""
                                
                After Jordan:
                                
                """);

        M.print(g);


        // Solution
        for (int i = 0; i < l; i++) {
            Out.print("x%d = %s    ",
                    i + 1,
                    Q.of(g[i][n - 1], g[i][pivot[i]]));
        }

        return M.shuffleRows(g, pivot);
    }


    private static long[] reduceRowUsingPivot(final long[] a, final long[] b, final int c) {
        final int l = a.length;
        if (b[c] == 0) throw new IllegalStateException("system unsolvable");
        if (a[c] == 0) return a;

        final long lcm = lcm(Math.abs(a[c]), Math.abs(b[c])), // use LCM so that we can calculate with longs everywhere
                ka = Math.abs(lcm / a[c]),
                kb = Math.abs(lcm / b[c]),
                op = -sgn(a[c] * b[c]);

        for (int i = 0; i < l; i++) a[i] = ka * a[i] + op * kb * b[i];

        long gcd = 0;
        for (final long x : a) if (x != 0) gcd = gcd == 0 ? x : gcd(gcd, Math.abs(x));
        // equation can be reduced (8x + 4y = 16 -> 2x + y = 4)
        if (gcd > 1) for (int i = 0; i < l; i++) a[i] /= gcd;

        return a;
    }

    public static int argmin(final long[] a) {
        int argmin = 0;
        for (int i = 0; i < a.length; i++) if (a[i] < a[argmin]) argmin = i;
        return argmin;
    }

    public static long[] map(final long[] a, final MapLong f) {
        for (int i = 0; i < a.length; i++) a[i] = f.apply(i, a[i]);
        return a;
    }

    public static boolean contains(final long[] a, final long x) {
        for (final long l : a) if (l == x) return true;
        return false;
    }


    @FunctionalInterface
    public interface MapLong {
        long apply(int i, long x);
    }
    //endregion

    public static void main(String[] args) {
        final long[][] a = new long[][]{
                {-3, -1, 2},
                {2, 1, -1},
                {-2, 1, 2}};

        final long[] b = new long[]{-11, 8, -3};
        final long[][] g = gaussJordanEliminationMethod(a, b);

        Out.print("""
                                
                                
                FINAL SOLUTION OF GAUSS-JORDAN ELIMINATION WITH ROWS RESHUFFLED
                                
                """);

        M.print(g);
    }
}

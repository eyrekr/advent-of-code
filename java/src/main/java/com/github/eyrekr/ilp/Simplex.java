package com.github.eyrekr.ilp;

import com.github.eyrekr.immutable.Opt;
import org.apache.commons.lang3.ArrayUtils;

public final class Simplex {

    public static Q maximize(final long[][] a, final long[] b, final long[] c) {
        return solve(tableau(a, b, c));
    }

    public static Q minimize(final long[][] a, final long[] b, final long[] c) {
        return maximize(transpose(a), c, b);
    }

    private static long[][] transpose(final long[][] a) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[n][m];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) t[j][i] = a[i][j];
        return t;
    }

    //   A E b
    //  -c 0 0
    private static Q[][] tableau(final long[][] a, final long[] b, final long[] c) {
        final int m = a.length, n = a[0].length, l = n + m;
        final Q[][] t = new Q[m + 1][l + 1];

        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) t[i][j] = Q.of(a[i][j]); // A
        for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) t[i][j + n] = i == j ? Q.One : Q.Zero; // E
        for (int i = 0; i < m; i++) t[i][l] = Q.of(b[i]); // b
        for (int j = 0; j < n; j++) t[m][j] = Q.of(-c[j]); // c
        for (int j = 0; j < n + 1; j++) t[m][j] = Q.Zero;

        return t;
    }

    /*
     *                variables        slack
     *                x0 x1 x2 x3 x4 │ x5 x6 x7 x8 x9 │ b
     *    base        ───────────────┼────────────────┼───
     *    x5           0  0  0  0  1 │ 1  0  0  0  0  │ 1
     *    x6           0  1  0  0  1 │ 0  1  0  0  0  │ 1
     *    x7           0  0  1  1  0 │ 0  0  1  0  0  │ 1
     *    x8           1  0  0  0  1 │ 0  0  0  1  0  │ 1
     *    x9           1  1  1  0  0 │ 0  0  0  0  1  │ 1
     *                ───────────────┼────────────────┼───
     *    c           -3 -5 -4 -7 -1 │ 0  0  0  0  0  │ 0
     */
    private static Q solve(final Q[][] t) {
        pivot(t).ifPresent()
    }

    private static Opt<P> pivot(final Q[][] t) {
        final int c = t.length - 1;
        final int[] jj = argmin(t[c], (i, value) -> value.negative ? value : Q.Infinity);
        if (jj.length == 0) return Opt.empty();
        final int j = jj[0];

        final int b = t[0].length - 1;
        final int[] ii = argmin(column(t, j), (i, value) -> value.positive ? t[i][b].div(value) : Q.Infinity);
        if (ii.length == 0) return Opt.empty();
        final int i = ii[0];

        return Opt.of(new P(i, j));
    }

    private static Q[] column(final Q[][] t, final int j) {
        final Q[] c = new Q[t.length];
        for (int i = 0; i < t.length; i++) c[i] = t[i][j];
        return c;
    }

    private static int[] argmin(final Q[] q, final Convert convert) {
        int[] args = new int[0];
        Q min = Q.Infinity;
        for (int i = 0; i < q.length; i++) {
            final Q value = convert.convert(i, q[i]);
            if (value.is(min)) args = ArrayUtils.add(args, i);
            else if (value.lowerThan(min)) {
                args = new int[]{i};
                min = value;
            }
        }
        return args;
    }

    @FunctionalInterface
    interface Convert {
        Q convert(int i, Q value);
    }

    record P(int i, int j) {
    }
}

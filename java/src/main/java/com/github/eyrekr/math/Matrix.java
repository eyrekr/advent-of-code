package com.github.eyrekr.math;

import com.github.eyrekr.annotation.ReturnsNewInstance;


/**
 * Operations for row-based matrices {@code A = m 🞨 n}
 * Naming conventions are as follows
 * <ol>
 *     <li>{@code m} - number of rows; {@code i} - iteration over rows</li>
 *     <li>{@code n} - number of columns; {@code j} - iteration over columns</li>
 * </ol>
 */
public final class Matrix {

    //region LONG
    @ReturnsNewInstance
    public static long[][] copy(final long[][] a) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m][n];
        for (int i = 0; i < m; i++) System.arraycopy(a[i], 0, t[i], 0, n);
        return t;
    }

    @ReturnsNewInstance
    public static long[][] transpose(final long[][] a) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[n][m];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) t[j][i] = a[i][j];
        return t;
    }

    @ReturnsNewInstance
    public static long[][] unit(final int n) {
        final long[][] t = new long[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) t[i][j] = i == j ? 1 : 0;
        return t;
    }

    @ReturnsNewInstance
    public static long[][] appendRow(final long[][] a, final long[] b) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m + 1][n];
        paste(a, t, 0, 0);
        System.arraycopy(b, 0, t[m], 0, n);
        return t;
    }

    @ReturnsNewInstance
    public static long[][] appendColumn(final long[][] a, final long[] b) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m][n + 1];
        paste(a, t, 0, 0);
        for (int i = 0; i < m; i++) a[i][n] = b[i];
        return t;
    }

    @ReturnsNewInstance
    public static long[][] appendMatrix(final long[][] a, final long[][] b) {
        final int m = a.length + b.length, n = a[0].length + b[0].length;
        final long[][] t = new long[m][n];
        paste(a, t, 0, 0);
        paste(b, t, 0, a[0].length);
        return t;
    }

    @ReturnsNewInstance
    public static long[] row(final long[][] a, final int i) {
        final int n = a[0].length;
        final long[] t = new long[n];
        System.arraycopy(a[i], 0, t, 0, n);
        return t;
    }

    @ReturnsNewInstance
    public static long[] column(final long[][] a, final int j) {
        final int m = a.length;
        final long[] t = new long[m];
        for (int i = 0; i < m; i++) t[i] = a[i][j];
        return t;
    }

    public static long[][] fill(final long[][] a, final long value) {
        final int m = a.length, n = a[0].length;
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) a[i][j] = value;
        return a;
    }

    private static long[][] paste(final long[][] src, final long[][] tgt, final int i0, final int j0) {
        final int m = Math.min(src.length, tgt.length), n = Math.min(src[0].length, tgt[0].length);
        for (int i = 0; i < m; i++) System.arraycopy(src[i], 0, tgt[i0 + i], j0, n);
        return tgt;
    }
    //endregion
}

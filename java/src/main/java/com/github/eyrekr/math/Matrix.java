package com.github.eyrekr.math;

public final class Matrix {

    //region LONG
    long[][] copy(final long[][] a) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m][n];
        for (int i = 0; i < m; i++) System.arraycopy(a[i], 0, t[i], 0, n);
        return t;
    }

    static long[][] transpose(final long[][] a) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[n][m];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) t[j][i] = a[i][j];
        return t;
    }

    long[][] fill(final long[][] a, final long value) {
        final int m = a.length, n = a[0].length;
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) a[i][j] = 0;
        return a;
    }

    long[][] unit(final int n) {
        final long[][] t = new long[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) t[i][j] = i == j ? 1 : 0;
        return t;
    }

    long[][] addRow(final long[][] a, final long[] b) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m + 1][n];
        paste(a, t);
        System.arraycopy(b, 0, t[m], 0, n);
        return t;
    }

    long[][] addColumn(final long[][] a, final long[] b) {
        final int m = a.length, n = a[0].length;
        final long[][] t = new long[m][n + 1];
        paste(a, t);
        for (int i = 0; i < m; i++) a[i][n] = b[i];
        return t;
    }

    long[][] addMatrix(final long[][] a, final long[][] b) {
        final int m = a.length + b.length, n = a[0].length + b[0].length;
        final long[][] t = new long[m][n];
        paste(a, t);
        paste(b, t, 0, a[0].length);
        return t;
    }

    long[][] paste(final long[][] src, final long[][] tgt) {
        return paste(src, tgt, 0, 0);
    }

    long[][] paste(final long[][] src, final long[][] tgt, final int i0, final int j0) {
        final int m = Math.min(src.length, tgt.length), n = Math.min(src[0].length, tgt[0].length);
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) tgt[i0 + i][j0 + j] = src[i][j];
        return tgt;
    }
    //endregion
}

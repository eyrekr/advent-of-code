package com.github.eyrekr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

record Grid(int m, int n, char[][] data) implements Iterable<Grid.It> {

    static final char C0 = '\0';

    static Grid from(final Path path) {
        try {
            return Grid.of(Files.readAllLines(path));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static Grid of(final List<String> lines) {
        final int n = lines.size();
        final int m = lines.get(0).length();
        final char[][] data = new char[m][n];
        for (int y = 0; y < n; y++) {
            final char[] line = lines.get(y).toCharArray();
            for (int x = 0; x < m; x++) {
                data[x][y] = line[x];
            }
        }
        return new Grid(m, n, data);
    }

    char at(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? data[x][y] : C0;
    }

    Grid transpose() {
        final char[][] transposed = new char[n][m];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                transposed[y][x] = data[x][y];
            }
        }
        return new Grid(n, m, transposed);
    }


    @Override
    public Iterator<It> iterator() {
        return new GridIterator();
    }

    static class It {
        final int i;
        final int x;
        final int y;
        final int m;
        final int n;
        final char ch;
        final char[] neighbours4;
        final char[] neighbours8;
        final int digit;
        final boolean first;
        final boolean last;
        final boolean firstOnLine;
        final boolean lastOnLine;

        private It(final int i, final int x, final int y, final int m, final int n, final char ch,
                   final char[] neighbours4, final char[] neighbours8, final int digit,
                   final boolean first, final boolean last, final boolean firstOnLine, final boolean lastOnLine) {
            this.i = i;
            this.x = x;
            this.y = y;
            this.m = m;
            this.n = n;
            this.ch = ch;
            this.neighbours4 = neighbours4;
            this.neighbours8 = neighbours8;
            this.digit = digit;
            this.first = first;
            this.last = last;
            this.firstOnLine = firstOnLine;
            this.lastOnLine = lastOnLine;
        }
    }

    private class GridIterator implements Iterator<It> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < m * n;
        }

        @Override
        public It next() {
            if (i >= m * n) {
                throw new IllegalStateException();
            }
            final int x = i % m;
            final int y = i / n;
            final char ch = data[x][y];
            final char[] ch4 = new char[]{at(x, y - 1), at(x - 1, y), at(x + 1, y), at(x, y + 1)};
            final char[] ch8 = new char[]{at(x - 1, y - 1), at(x, y - 1), at(x + 1, y - 1), at(x - 1, y), at(x + 1, y), at(x - 1, y + 1), at(x, y + 1), at(x + 1, y + 1)};
            final int digit = Character.isDigit(ch) ? Character.digit(ch, 10) : -1;
            final It it = new It(
                    i, x, y, m, n,
                    ch, ch4, ch8,
                    digit,
                    i == 0, i == m * n - 1, x == 0, x == m - 1);
            i++;
            return it;
        }
    }
}

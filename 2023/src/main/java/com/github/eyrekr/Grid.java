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

    record It(int x, int y, char ch, char[] neighbours4, char[] neighbours8, int digit,
              boolean first, boolean last, boolean firstOnLine, boolean lastOnLine) {
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
            final It it = new It(x, y, ch, ch4, ch8, "0123456789".indexOf(ch), i == 0, i == m * n - 1, x == 0, x == m - 1);
            i++;
            return it;
        }
    }
}

package com.github.eyrekr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

record Grid(int m, int n, char[][] data) implements Iterable<Grid.Item> {

    static Grid from(final Path path) {
        try {
            return from(Files.readAllLines(Path.of("src/main/resources/03.txt")));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static Grid from(final List<String> lines) {
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

    char at(int x, int y, char defaultValue) {
        return x >= 0 && x < m && y >= 0 && y < n ? data[x][y] : defaultValue;
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
    public Iterator<Item> iterator() {
        return new GridIterator();
    }

    record Item(int x, int y, char ch, int digit, boolean first, boolean last, boolean firstOnLine,
                boolean lastOnLine) {
    }

    private class GridIterator implements Iterator<Grid.Item> {
        private int i = -1;

        @Override
        public boolean hasNext() {
            return i < m * n;
        }

        @Override
        public Item next() {
            i++;
            if (i >= m * n) {
                throw new IllegalStateException();
            }
            final int x = i % m;
            final int y = i / n;
            final char ch = data[x][y];
            return new Item(x, y, ch, "0123456789".indexOf(ch), i == 0, i == m * n - 1, x == 0, x == m - 1);
        }
    }
}

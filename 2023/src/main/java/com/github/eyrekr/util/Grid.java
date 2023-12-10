package com.github.eyrekr.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Grid implements Iterable<Grid.It> {
    public static final char C0 = '\0';

    public final int m;
    public final int n;
    public final char[][] a;

    private Grid(final int m, final int n, final char[][] a) {
        this.m = m;
        this.n = n;
        this.a = a;
    }

    public static Grid from(final Path path) {
        try {
            return Grid.of(Files.readAllLines(path));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Grid of(final List<String> lines) {
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

    public static Grid of(final Seq<String> lines) {
        final int n = lines.length;
        final int m = lines.value.length();
        final char[][] data = new char[m][n];
        for (int y = 0; y < n; y++) {
            final char[] line = lines.at(y).toCharArray();
            for (int x = 0; x < m; x++) {
                data[x][y] = line[x];
            }
        }
        return new Grid(m, n, data);
    }

    public char at(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? a[x][y] : C0;
    }

    public It it(final int i) {
        final int x = i % m;
        final int y = (i / m) % n;
        final char ch = a[x][y];
        final char[] ch4 = new char[]{at(x, y - 1), at(x - 1, y), at(x + 1, y), at(x, y + 1)};
        final char[] ch8 = new char[]{at(x - 1, y - 1), at(x, y - 1), at(x + 1, y - 1), at(x - 1, y), at(x + 1, y), at(x - 1, y + 1), at(x, y + 1), at(x + 1, y + 1)};
        final int digit = Character.isDigit(ch) ? Character.digit(ch, 10) : -1;
        return new It(
                i, x, y, m, n,
                ch, ch4, ch8,
                digit,
                x == 0 && y == 0, x == m - 1 && y == n - 1, x == 0, x == m - 1);
    }

    public It it(final int x, final int y) {
        return it(y * m + x);
    }

    public Grid each(final Consumer<It> consumer) {
        for (int i = 0; i < m * n; i++) {
            consumer.accept(it(i));
        }
        return this;
    }

    public It first(final Predicate<It> predicate) {
        for (int i = 0; i < m * n; i++) {
            final It it = it(i);
            if (predicate.test(it)) {
                return it;
            }
        }
        return null;
    }

    public It last(final Predicate<It> predicate) {
        for (int i = m * n - 1; i >= 0; i--) {
            final It it = it(i);
            if (predicate.test(it)) {
                return it;
            }
        }
        return null;
    }

    public Seq<It> where(final Predicate<It> predicate) {
        Seq<It> seq = Seq.empty();
        for (int i = m * n - 1; i >= 0; i--) {
            final It it = it(i);
            if (predicate.test(it)) {
                seq = seq.prepend(it);
            }
        }
        return seq;
    }

    public Grid transpose() {
        final char[][] transposed = new char[n][m];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                transposed[y][x] = a[x][y];
            }
        }
        return new Grid(n, m, transposed);
    }

    public Grid map(final Function<It, Character> transform) {
        final Grid grid = new Grid(m, n, new char[m][n]);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                final Character ch = transform.apply(it(x, y));
                grid.a[x][y] = ch == null ? C0 : ch;
            }
        }
        return grid;
    }

    public Grid print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Str.print(""+at(x,y));
            }
            System.out.println();
        }
        return this;
    }

    @Override
    public Iterator<It> iterator() {
        return new GridIterator();
    }

    private class GridIterator implements Iterator<It> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < m * n - 1;
        }

        @Override
        public It next() {
            return it(i++);
        }
    }

    public final class It {
        public final int i;
        public final int x;
        public final int y;
        public final P p;
        public final int m;
        public final int n;
        public final char ch;
        public final char[] neighbours4;
        public final char[] neighbours8;
        public final int digit;
        public final boolean first;
        public final boolean last;
        public final boolean firstOnLine;
        public final boolean lastOnLine;

        private It(final int i, final int x, final int y, final int m, final int n, final char ch,
                   final char[] neighbours4, final char[] neighbours8, final int digit,
                   final boolean first, final boolean last, final boolean firstOnLine, final boolean lastOnLine) {
            this.i = i;
            this.x = x;
            this.p = new P(x, y);
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

        public It to(final D d) {
            return switch (d) {
                case U -> it(x, y - 1);
                case D -> it(x, y + 1);
                case L -> it(x - 1, y);
                case R -> it(x + 1, y);
            };
        }
    }

    public enum D {U, D, L, R}

    public final class P {
        public final int x;
        public final int y;

        public P(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public P to(final D d) {
            return switch (d) {
                case U -> new P(x, y - 1);
                case D -> new P(x, y + 1);
                case L -> new P(x - 1, y);
                case R -> new P(x + 1, y);
            };
        }
    }
}

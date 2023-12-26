package com.github.eyrekr.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Grid implements Iterable<Grid.It> {
    private static final Seq<Direction> ALL_DIRECTIONS = Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
    public static final char C0 = '\0';

    public final int m;
    public final int n;
    public final char[][] a;
    public final int[][] d; // distances, e.g. for Dijkstra traversal
    public final boolean[][] b; // flags, e.g. for visited/unvisited nodes

    private Grid(final int m, final int n, final char[][] a) {
        this.m = m;
        this.n = n;
        this.a = a;
        this.d = new int[m][n];
        this.b = new boolean[m][n];
    }

    public static Grid of(final int m, final int n) {
        return new Grid(m, n, new char[m][n]);
    }

    @Deprecated // use the Grid.of(String) version
    public static Grid from(final Path path) {
        try {
            return Grid.of(Files.readAllLines(path));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Grid of(final String input) {
        return Grid.of(Seq.ofLinesFromString(input));
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
        return it(i % m, (i / m) % n);
    }

    public It it(final int x, final int y) {
        if (x < 0 || y < 0 || x >= m || y >= n) throw new IllegalStateException();
        final char ch = a[x][y];
        final char[] ch4 = new char[]{at(x, y - 1), at(x - 1, y), at(x + 1, y), at(x, y + 1)};
        final char[] ch8 = new char[]{at(x - 1, y - 1), at(x, y - 1), at(x + 1, y - 1), at(x - 1, y), at(x + 1, y), at(x - 1, y + 1), at(x, y + 1), at(x + 1, y + 1)};
        final int digit = Character.isDigit(ch) ? Character.digit(ch, 10) : -1;
        return new It(y * m + x, x, y, m, n, ch, d[x][y], b[x][y], ch4, ch8, digit, x == 0 && y == 0, x == m - 1 && y == n - 1, x == 0, x == m - 1);
    }

    public Grid repeat(final int factor) {
        final Grid grid = Grid.of(m * factor, n * factor);
        for (int y = 0; y < n * factor; y++)
            for (int x = 0; x < m * factor; x++)
                grid.a[x][y] = a[x % m][y % n];
        return grid;
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

    public It chFirst(final Predicate<Character> predicate) {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                final char ch = a[x][y];
                if (predicate.test(ch)) {
                    return it(x, y);
                }
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

    public It chLast(final Predicate<Character> predicate) {
        for (int y = n - 1; y >= 0; y--) {
            for (int x = m - 1; x >= 0; x--) {
                final char ch = a[x][y];
                if (predicate.test(ch)) {
                    return it(x, y);
                }
            }
        }
        return null;
    }

    public Seq<It> where(final Predicate<It> predicate) {
        Seq<It> seq = Seq.empty();
        for (int i = m * n - 1; i >= 0; i--) {
            final It it = it(i);
            if (predicate.test(it)) {
                seq = seq.addFirst(it);
            }
        }
        return seq;
    }

    public Seq<It> chWhere(final Predicate<Character> predicate) {
        Seq<It> seq = Seq.empty();
        for (int y = n - 1; y >= 0; y--) {
            final char[] row = a[y];
            for (int x = m - 1; x >= 0; x--) {
                final char ch = row[x];
                if (predicate.test(ch)) {
                    seq = seq.addFirst(it(x, y));
                }
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

    public Grid rotateCW() { //clockwise
        final char[][] rotated = new char[n][m];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                rotated[m - 1 - y][x] = a[x][y];
            }
        }
        return new Grid(n, m, rotated);
    }

    public Grid rotateCCW() { //counter-clockwise
        final char[][] rotated = new char[n][m];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                rotated[y][n - 1 - x] = a[x][y];
            }
        }
        return new Grid(n, m, rotated);
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

    public Grid chMap(final Function<Character, Character> transform) {
        final Grid grid = new Grid(m, n, new char[m][n]);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                final Character ch = transform.apply(a[x][y]);
                grid.a[x][y] = ch == null ? C0 : ch;
            }
        }
        return grid;
    }

    public <R> R reduce(final R init, final BiFunction<? super R, It, ? extends R> reduce) {
        R acc = init;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                acc = reduce.apply(acc, it(x, y));
            }
        }
        return acc;
    }

    public <R> R chReduce(final R init, final BiFunction<? super R, Character, ? extends R> reduce) {
        R acc = init;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                acc = reduce.apply(acc, a[x][y]);
            }
        }
        return acc;
    }

    public int sum(final Function<It, Integer> transform) {
        return reduce(0, (acc, it) -> acc + transform.apply(it));
    }

    public int chSum(final Function<Character, Integer> transform) {
        return chReduce(0, (acc, ch) -> acc + transform.apply(ch));
    }

    public Seq<Column> columns() {
        Seq<Column> columns = Seq.empty();
        for (int x = m - 1; x >= 0; x--) {
            columns = columns.addLast(new Column(x, m, a[x]));
        }
        return columns;
    }

    public Grid print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Str.print("" + at(x, y));
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
            return i < m * n;
        }

        @Override
        public It next() {
            return it(i++);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final Grid that) {
            if (this.m != that.m && this.n != that.n) return false;
            for (int y = 0; y < n; y++) {
                for (int x = 0; x < m; x++) {
                    if (this.a[x][y] != that.a[x][y]) return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * m * n * chReduce(-1, (acc, ch) -> acc * ch);
    }

    @Override
    public String toString() {
        return reduce(new StringBuilder(), (builder, it) -> it.lastOnLine ? builder.append(it.ch).append('\n') : builder.append(it.ch)).toString();
    }

    public final class Column {
        public final int x;
        public final int m;
        public final char[] a;
        public final boolean first;
        public final boolean last;

        private Column(final int x, final int m, final char[] a) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.first = x == 0;
            this.last = x == m - 1;
        }
    }

    public final class It {
        public final int i;
        public final int x;
        public final int y;
        public final int m;
        public final int n;
        public final char ch;
        public final int d;
        public final boolean b;
        public final char[] neighbours4;
        public final char[] neighbours8;
        public final int digit;
        public final boolean first;
        public final boolean last;
        public final boolean firstOnLine;
        public final boolean lastOnLine;

        private It(final int i, final int x, final int y, final int m, final int n,
                   final char ch, final int d, final boolean b,
                   final char[] neighbours4, final char[] neighbours8,
                   final int digit,
                   final boolean first, final boolean last, final boolean firstOnLine, final boolean lastOnLine) {
            this.i = i;
            this.x = x;
            this.y = y;
            this.m = m;
            this.n = n;
            this.ch = ch;
            this.d = d;
            this.b = b;
            this.neighbours4 = neighbours4;
            this.neighbours8 = neighbours8;
            this.digit = digit;
            this.first = first;
            this.last = last;
            this.firstOnLine = firstOnLine;
            this.lastOnLine = lastOnLine;
        }

        public It go(final Direction direction) {
            return it(x + direction.dx, y + direction.dy);
        }

        public Optional<It> tryToGo(final Direction direction) { // no passing through walls
            return switch (direction) {
                case Up -> y > 0 ? Optional.of(it(x, y - 1)) : Optional.empty();
                case Down -> y < n - 1 ? Optional.of(it(x, y + 1)) : Optional.empty();
                case Left -> x > 0 ? Optional.of(it(x - 1, y)) : Optional.empty();
                case Right -> x < m - 1 ? Optional.of(it(x + 1, y)) : Optional.empty();
            };
        }

        public It set(final char ch, final int d, final boolean b) {
            Grid.this.a[x][y] = ch;
            Grid.this.d[x][y] = d;
            Grid.this.b[x][y] = b;
            return it(x, y);
        }
    }

    public enum Direction {
        Up(0, -1), Down(0, +1), Left(-1, 0), Right(+1, 0);
        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}

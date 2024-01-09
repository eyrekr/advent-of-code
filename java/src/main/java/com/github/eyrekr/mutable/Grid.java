package com.github.eyrekr.mutable;

import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Grid implements Iterable<Grid.It> {
    public static final char C0 = '\0';

    public final int m;
    public final int n;
    public final char[][] a;
    public final int[][] d;
    public final State[][] state;
    public final boolean maze;

    private Grid(final int m, final int n, final boolean maze) {
        this.m = m;
        this.n = n;
        this.a = new char[m][n];
        this.d = new int[m][n];
        this.state = new State[m][n];
        this.maze = maze;

        for (int i = 0; i < m * n; i++) state[i % m][i / m] = State.Unseen;
    }

    public static Grid of(final int m, final int n) {
        return new Grid(m, n, false);
    }

    public static Grid of(final String input) {
        return Grid.of(input, false);
    }

    public static Grid of(final String input, final boolean maze) {
        final String[] lines = input.split("\n");
        final int n = lines.length, m = lines[0].length();
        final Grid grid = new Grid(m, n, maze);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                grid.a[x][y] = lines[y].charAt(x);
        return grid;
    }

    public char at(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? a[x][y] : C0;
    }

    public It it(final int i) {
        return it(i % m, (i / m) % n, Direction.None);
    }

    public It it(final int x, final int y) {
        return it(x, y, Direction.None);
    }

    public It it(final int x, final int y, final Direction direction) {
        if (x < 0 || y < 0 || x >= m || y >= n) throw new IndexOutOfBoundsException();
        final char ch = a[x][y];
        final char[] ch4 = new char[]{at(x, y - 1), at(x - 1, y), at(x + 1, y), at(x, y + 1)};
        final char[] ch8 = new char[]{at(x - 1, y - 1), at(x, y - 1), at(x + 1, y - 1), at(x - 1, y), at(x + 1, y), at(x - 1, y + 1), at(x, y + 1), at(x + 1, y + 1)};
        final int digit = Character.isDigit(ch) ? Character.digit(ch, 10) : -1;
        return new It(
                this,
                y * m + x, x, y, m, n, ch, d[x][y], state[x][y],
                ch4, ch8, digit, direction,
                x == 0 && y == 0, x == m - 1 && y == n - 1, x == 0, x == m - 1,
                ch == '#');
    }

    public Grid repeat(final int factor) {
        final Grid grid = Grid.of(m * factor, n * factor);
        for (int y = 0; y < n * factor; y++)
            for (int x = 0; x < m * factor; x++) {
                grid.a[x][y] = a[x % m][y % n];
                grid.d[x][y] = d[x % m][y % n];
                grid.state[x][y] = state[x % m][y % n];
            }
        return grid;
    }

    public Grid each(final Consumer<It> consumer) {
        for (int i = 0; i < m * n; i++) consumer.accept(it(i));
        return this;
    }

    public It first(final Predicate<It> predicate) {
        for (int i = 0; i < m * n; i++) {
            final It it = it(i);
            if (predicate.test(it)) return it;
        }
        return null;
    }

    public It first(final char ch) {
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                if (a[x][y] == ch) return it(x, y, Direction.None);
        return null;
    }

    public It last(final Predicate<It> predicate) {
        for (int i = m * n - 1; i >= 0; i--) {
            final It it = it(i);
            if (predicate.test(it)) return it;
        }
        return null;
    }

    public It last(final char ch) {
        for (int y = n - 1; y >= 0; y--)
            for (int x = m - 1; x >= 0; x--)
                if (a[x][y] == ch) return it(x, y, Direction.None);
        return null;
    }

    public Seq<It> collect(final Predicate<It> predicate) {
        Seq<It> seq = Seq.empty();
        for (int i = m * n - 1; i >= 0; i--) {
            final It it = it(i);
            if (predicate.test(it)) seq = seq.addFirst(it);
        }
        return seq.reverse();
    }

    public Seq<It> collect(final char ch) {
        Seq<It> seq = Seq.empty();
        for (int y = n - 1; y >= 0; y--)
            for (int x = m - 1; x >= 0; x--)
                if (a[x][y] == ch) seq = seq.addFirst(it(x, y, Direction.None));
        return seq.reverse();
    }

    public Grid transpose() {
        final Grid transposed = new Grid(n, m, maze);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                transposed.a[y][x] = a[x][y];
                transposed.d[y][x] = d[x][y];
                transposed.state[y][x] = state[x][y];
            }
        return transposed;
    }

    public Grid rotateCW() { //clockwise
        final Grid rotated = new Grid(n, m, maze);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                rotated.a[m - 1 - y][x] = a[x][y];
                rotated.d[m - 1 - y][x] = d[x][y];
                rotated.state[m - 1 - y][x] = state[x][y];
            }
        return rotated;
    }

    public Grid rotateCCW() { //counter-clockwise
        final Grid rotated = new Grid(n, m, maze);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                rotated.a[y][n - 1 - x] = a[x][y];
                rotated.d[y][n - 1 - x] = d[x][y];
                rotated.state[y][n - 1 - x] = state[x][y];
            }
        return rotated;
    }

    public Grid replace(final Function<Character, Character> transform) {
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                final Character ch = transform.apply(a[x][y]);
                a[x][y] = ch == null ? C0 : ch;
            }
        return this;
    }

    public <R> R reduce(final R init, final BiFunction<? super R, It, ? extends R> reduce) {
        R acc = init;
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                acc = reduce.apply(acc, it(x, y, Direction.None));
        return acc;
    }

    public Grid print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Out.print("" + at(x, y));
            }
            Out.print("\n");
        }
        return this;
    }

    public Grid print(final Function<It, String> transform) {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Out.print(transform.apply(it(x, y, Direction.None)));
            }
            Out.print("\n");
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
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                builder.append(a[x][y]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final Grid that && that.m == this.m && that.n == this.n) {
            for (int y = 0; y < n; y++)
                for (int x = 0; x < m; x++)
                    if (a[x][y] != that.a[x][y]) return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 31 * m * n;
        for (int i = 0; i < m * n; i++) code *= a[i % m][i / m];
        return code;
    }

    public static final class It {
        public final Grid grid;
        public final int i;
        public final int x;
        public final int y;
        public final int m;
        public final int n;
        public final char ch;
        public final int d;
        public final State state;
        public final char[] neighbours4;
        public final char[] neighbours8;
        public final int digit;
        public final Direction direction;
        public final boolean symbolMatchesDirection;
        public final boolean first;
        public final boolean last;
        public final boolean firstOnLine;
        public final boolean lastOnLine;
        public final boolean wall;

        private It(final Grid grid,
                   final int i, final int x, final int y, final int m, final int n,
                   final char ch, final int d, final State state,
                   final char[] neighbours4, final char[] neighbours8,
                   final int digit, final Direction direction,
                   final boolean first, final boolean last, final boolean firstOnLine, final boolean lastOnLine,
                   final boolean wall) {
            this.grid = grid;
            this.i = i;
            this.x = x;
            this.y = y;
            this.m = m;
            this.n = n;
            this.ch = ch;
            this.d = d;
            this.state = state;
            this.neighbours4 = neighbours4;
            this.neighbours8 = neighbours8;
            this.digit = digit;
            this.direction = direction;
            this.symbolMatchesDirection = switch (ch) {
                case '^' -> direction == Direction.Up;
                case 'v' -> direction == Direction.Down;
                case '>' -> direction == Direction.Right;
                case '<' -> direction == Direction.Down;
                default -> true;
            };
            this.first = first;
            this.last = last;
            this.firstOnLine = firstOnLine;
            this.lastOnLine = lastOnLine;
            this.wall = wall;
        }

        public It go(final Direction direction) {
            return grid.it(x + direction.dx, y + direction.dy, direction);
        }

        public Optional<It> tryToGo(final Direction direction) { // no passing through walls
            return switch (direction) {
                case Up -> y > 0 ? Optional.of(grid.it(x, y - 1, direction)) : Optional.empty();
                case Down -> y < n - 1 ? Optional.of(grid.it(x, y + 1, direction)) : Optional.empty();
                case Left -> x > 0 ? Optional.of(grid.it(x - 1, y, direction)) : Optional.empty();
                case Right -> x < m - 1 ? Optional.of(grid.it(x + 1, y, direction)) : Optional.empty();
                case None -> Optional.of(this);
            };
        }

        public Seq<It> neighbours() {
            return Seq.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right)
                    .where(direction -> direction.dx + x >= 0 && direction.dx + x < m && direction.dy + y >= 0 && direction.dy + y < n)
                    .map(this::go)
                    .where(neighbour -> !grid.maze || !neighbour.wall);
        }

        public Seq<It> neighbours(final Predicate<It> predicate) {
            return neighbours().where(predicate);
        }

        public It set(final char ch) {
            grid.a[x][y] = ch;
            return grid.it(x, y, direction);
        }

        public It setDistance(final int distance) {
            grid.d[x][y] = distance;
            return grid.it(x, y, direction);
        }

        public It setState(final State state) {
            grid.state[x][y] = state;
            return grid.it(x, y, direction);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof It that && that.grid == this.grid && that.i == this.i;
        }

        @Override
        public int hashCode() {
            return Objects.hash(grid, i);
        }
    }

    public enum State {Unseen, Open, Closed}

}

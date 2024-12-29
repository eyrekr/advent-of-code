package com.github.eyrekr.mutable;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Grid2 {

    public static final char VOID = '\0';

    public final int m;
    public final int n;
    public final char[][] a;
    public final long[][] d;
    public final State[][] state;

    private Grid2(final int m, final int n) {
        this.m = m;
        this.n = n;
        this.a = new char[m][n];
        this.d = new long[m][n];
        this.state = new State[m][n];
    }

    private Grid2(final Grid2 source) {
        this.m = source.m;
        this.n = source.n;
        this.a = new char[m][n];
        this.d = new long[m][n];
        this.state = new State[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                a[x][y] = source.a[x][y];
                d[x][y] = source.d[x][y];
                state[x][y] = source.state[x][y];
            }
    }

    public static Grid2 empty(final int m, final int n) {
        return new Grid2(m, n);
    }

    public static Grid2 fromString(final String input) {
        final String[] lines = StringUtils.split(input, "\n");
        final int n = lines.length, m = lines[0].length();
        final Grid2 grid = new Grid2(m, n);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                grid.a[x][y] = lines[y].charAt(x);
        return grid;
    }

    public Grid2 duplicate() {
        return new Grid2(this);
    }

    public char at(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? a[x][y] : VOID;
    }

    public It it(int x, int y) {
        return new It(x, y);
    }

    public It start() {
        return new It(0, 0).turn(Direction.RightDown);
    }

    public It center() {
        return new It(m / 2, n / 2);
    }

    public Grid2 print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Out.print("" + a[x][y]);
            }
            Out.print("\n");
        }
        return this;
    }

    public final class It {
        public int x;
        public int y;
        public Direction direction = Direction.None;
        public boolean inside;
        public boolean outside;
        public int i; // index

        private It(final int x, final int y) {
            goTo(x, y);
        }

        private It(final It it) {
            this.x = it.x;
            this.y = it.y;
            this.direction = it.direction;
            this.inside = it.inside;
            this.outside = it.outside;
            this.i = it.i;
        }

        public It duplicate() {
            return new It(this);
        }

        public It goTo(final int x, final int y) {
            this.x = x;
            this.y = y;
            this.inside = x >= 0 && x < m && y >= 0 && y < n;
            this.outside = !inside;
            this.i = y * m + x;
            return this;
        }

        public char symbol() {
            return at(x, y);
        }

        public boolean is(final char ch) {
            return inside && a[x][y] == ch;
        }

        public boolean is(final long d) {
            return inside && Grid2.this.d[x][y] == d;
        }

        public boolean is(final State state) {
            return inside && Grid2.this.state[x][y] == state;
        }

        public char la() {
            return at(x + direction.dx, y + direction.dy);
        }

        public boolean la(final char ch) {
            return at(x + direction.dx, y + direction.dy) == ch;
        }

        public char la(final Direction direction) {
            return at(x + direction.dx, y + direction.dy);
        }

        public It go() {
            if (direction == Direction.None) throw new IllegalStateException("no direction");
            return goTo(x + direction.dx, y + direction.dy);
        }

        public It goWhile(final Predicate<It> condition) {
            while (condition.test(this)) go();
            return this;
        }

        public It goUntil(final Predicate<It> condition) {
            while (!condition.test(this)) go();
            return this;
        }

        public It scan() {
            return switch (direction) {
                case Right, RightUp, RightDown -> x < m - 1 ? goTo(x + direction.dx, y) : goTo(0, y + direction.dy);
                case Left, LeftUp, LeftDown -> x > 0 ? goTo(x + direction.dx, y) : goTo(m - 1, y + direction.dy);
                case Up, UpLeft, UpRight -> y > 0 ? goTo(x, y + direction.dy) : goTo(x + direction.dx, n - 1);
                case Down, DownLeft, DownRight -> y < n - 1 ? goTo(x, y + direction.dy) : goTo(x + direction.dx, 0);
                case None -> throw new IllegalStateException("no direction");
            };
        }

        public It scanWhile(final Predicate<It> condition) {
            while (condition.test(this)) scan();
            return this;
        }

        public It scanUntil(final Predicate<It> condition) {
            while (!condition.test(this)) scan();
            return this;
        }

        public <R> R reduce(final R initialValue, final BiFunction<R, It, R> reducer) {
            R value = initialValue;
            while (inside) {
                value = reducer.apply(value, this);
                scan();
            }
            return value;
        }

        public Arr<It> collect(final Predicate<It> condition) {
            final Arr<It> array = Arr.empty();
            while (inside) {
                if (condition.test(this)) array.addLast(duplicate());
                scan();
            }
            return array;
        }

        public long count(final Predicate<It> condition) {
            long value = 0L;
            while (inside) {
                if (condition.test(this)) value++;
                scan();
            }
            return value;
        }

        public It set(final char ch) {
            if (outside) return this;
            Grid2.this.a[x][y] = ch;
            return this;
        }

        public It set(final long d) {
            if (outside) return this;
            Grid2.this.d[x][y] = d;
            return this;
        }

        public It set(final State state) {
            if (outside) return this;
            Grid2.this.state[x][y] = state;
            return this;
        }

        public It turn(final Direction direction) {
            this.direction = direction;
            return this;
        }

        public It turnRight() {
            this.direction = direction.turn90DegreesRight();
            return this;
        }

        public It turnLeft() {
            this.direction = direction.turn90DegreesLeft();
            return this;
        }
    }
}

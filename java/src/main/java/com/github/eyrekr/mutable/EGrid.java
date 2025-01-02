package com.github.eyrekr.mutable;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EGrid<E> {

    public static final char Void = '\0';

    public final int m;
    public final int n;
    public final char[][] a;
    public final long[][] d;
    public final State[][] state;
    public final Object[][] e;

    private EGrid(final int m, final int n) {
        this.m = m;
        this.n = n;
        this.a = new char[m][n];
        this.d = new long[m][n];
        this.state = new State[m][n];
        this.e = new Object[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                a[x][y] = Void;
                d[x][y] = 0L;
                state[x][y] = State.Unseen;
                e[x][y] = null;
            }
    }

    private EGrid(final EGrid<? extends E> grid) {
        this.m = grid.m;
        this.n = grid.n;
        this.a = new char[m][n];
        this.d = new long[m][n];
        this.state = new State[m][n];
        this.e = new Object[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                a[x][y] = grid.a[x][y];
                d[x][y] = grid.d[x][y];
                state[x][y] = grid.state[x][y];
                e[x][y] = grid.e[x][y];
            }
    }

    public static <T> EGrid<T> empty(final int m, final int n) {
        return new EGrid<>(m, n);
    }

    public static <T> EGrid<T> fromString(final String input) {
        final String[] lines = StringUtils.split(input, "\n");
        final int n = lines.length, m = lines[0].length();
        final EGrid<T> grid = new EGrid<>(m, n);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                grid.a[x][y] = lines[y].charAt(x);
        return grid;
    }

    public EGrid<E> duplicate() {
        return new EGrid<>(this);
    }

    public char at(final int x, final int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? a[x][y] : Void;
    }

    public It it() {
        return new It(0, 0, Direction.None);
    }

    public It it(final int x, final int y) {
        return new It(x, y, Direction.None);
    }

    public It it(final int x, final int y, final Direction direction) {
        return new It(x, y, direction);
    }

    public It it(final int x, final int y, final int dx, final int dy) {
        return new It(x, y, dx, dy);
    }

    public Sc scan() {
        return new Sc(0, 0, Direction.RightDown, unused -> true);
    }

    public Sc scan(final Predicate<Sc> predicate) {
        return new Sc(0, 0, Direction.RightDown, predicate);
    }

    public Sc scan(final int x, final int y, final Direction direction, final Predicate<Sc> predicate) {
        return new Sc(x, y, direction, predicate);
    }

    public EGrid<E> print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                Out.print("" + a[x][y]);
            }
            Out.print("\n");
        }
        return this;
    }

    public final class Sc {

        public int x;
        public int y;
        public Direction direction;
        public Predicate<Sc> predicate;

        private Sc(final int x, final int y, final Direction direction, final Predicate<Sc> predicate) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.predicate = predicate;
            first();
        }

        private Sc(final Sc other) {
            this.x = other.x;
            this.y = other.y;
            this.direction = other.direction;
            this.predicate = other.predicate;
            first();
        }

        public Sc first() {
            if (!predicate.test(this)) next();
            return this;
        }

        public It it() {
            return new It(x, y, direction);
        }

        public Sc duplicate() {
            return new Sc(this);
        }

        public boolean inside() {
            return x >= 0 && x < m && y >= 0 && y < n;
        }

        public boolean outside() {
            return !inside();
        }

        public int id() {
            return y * m + x;
        }

        public char symbol() {
            return inside() ? a[x][y] : Void;
        }

        public boolean is(final char ch) {
            return inside() && a[x][y] == ch;
        }

        public boolean isOneOf(final char... chars) {
            return inside() && ArrayUtils.contains(chars, a[x][y]);
        }

        public boolean is(final long d) {
            return inside() && EGrid.this.d[x][y] == d;
        }

        public boolean is(final State state) {
            return inside() && EGrid.this.state[x][y] == state;
        }

        public Sc set(final char ch) {
            if (inside()) EGrid.this.a[x][y] = ch;
            return this;
        }

        public Sc set(final long d) {
            if (inside()) EGrid.this.d[x][y] = d;
            return this;
        }

        public Sc set(final State state) {
            if (inside()) EGrid.this.state[x][y] = state;
            return this;
        }

        public Sc set(final Direction direction) {
            this.direction = direction;
            return this;
        }

        public E load() {
            return inside() ? (E) EGrid.this.e[x][y] : null;
        }

        public Sc store(final E e) {
            if (inside()) EGrid.this.e[x][y] = e;
            return this;
        }

        public Sc to(final int x, final int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Sc next() {
            do {
                final Sc next = switch (direction) {
                    case Right, RightDown, RightUp -> x < m - 1 ? to(x + direction.dx, y) : to(0, y + direction.dy);
                    case Left, LeftDown, LeftUp -> x > 0 ? to(x + direction.dx, y) : to(m - 1, y + direction.dy);
                    case Up, UpLeft, UpRight -> y > 0 ? to(x, y + direction.dy) : to(x + direction.dx, n - 1);
                    case Down, DownLeft, DownRight -> y < n - 1 ? to(x, y + direction.dy) : to(x + direction.dx, 0);
                    case None -> throw new IllegalStateException("no direction");
                };
            } while (inside() && !predicate.test(this));
            return this;
        }

        public Sc each(final Consumer<Sc> consumer) {
            for (first(); inside(); next()) consumer.accept(this);
            return this;
        }

        public <R> R reduce(final R initialValue, final BiFunction<R, Sc, R> reducer) {
            R value = initialValue;
            for (first(); inside(); next()) value = reducer.apply(value, this);
            return value;
        }

        public Arr<Sc> collect() {
            final Arr<Sc> array = Arr.empty();
            for (first(); inside(); next()) array.addLast(duplicate());
            return array;
        }

        public long count() {
            long value = 0L;
            for (first(); inside(); next()) value++;
            return value;
        }
    }

    public final class It {

        public int x;
        public int y;
        public int dx;
        public int dy;

        private It(final int x, final int y, final int dx, final int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        private It(final int x, final int y, final Direction direction) {
            this.x = x;
            this.y = y;
            this.dx = direction.dx;
            this.dy = direction.dy;
        }

        private It(final It it) {
            this.x = it.x;
            this.y = it.y;
            this.dx = it.dx;
            this.dy = it.dy;
        }

        public It duplicate() {
            return new It(this);
        }

        public boolean inside() {
            return x >= 0 && x < m && y >= 0 && y < n;
        }

        public boolean outside() {
            return !inside();
        }

        public int id() {
            return y * m + x;
        }

        public boolean is(final char ch) {
            return inside() && EGrid.this.a[x][y] == ch;
        }

        public boolean is(final long d) {
            return inside() && EGrid.this.d[x][y] == d;
        }

        public boolean is(final State state) {
            return inside() && EGrid.this.state[x][y] == state;
        }

        public char la() {
            return at(x + dx, y + dy);
        }

        public boolean la(final char ch) {
            return at(x + dx, y + dy) == ch;
        }

        public char la(final Direction direction) {
            return at(x + direction.dx, y + direction.dy);
        }

        public char la(final int dx, final int dy) {
            return at(x + dx, y + dy);
        }

        public It set(final char ch) {
            if (inside()) EGrid.this.a[x][y] = ch;
            return this;
        }

        public It set(final long d) {
            if (inside()) EGrid.this.d[x][y] = d;
            return this;
        }

        public It set(final State state) {
            if (inside()) EGrid.this.state[x][y] = state;
            return this;
        }

        public It set(final Direction direction) {
            this.dx = direction.dx;
            this.dy = direction.dy;
            return this;
        }

        public E load() {
            return inside() ? (E) EGrid.this.e[x][y] : null;
        }

        public It store(final E e) {
            if (inside()) EGrid.this.e[x][y] = e;
            return this;
        }

        public It to(final int x, final int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public It by(final int dx, final int dy) {
            this.dx = dx;
            this.dy = dy;
            return this;
        }

        public It go() {
            if (dx == 0 && dy == 0) throw new IllegalStateException("no direction");
            this.x += dx;
            this.y += dy;
            return this;
        }

        public It goWhile(final Predicate<It> condition) {
            while (condition.test(this)) go();
            return this;
        }

        public It goUntil(final Predicate<It> condition) {
            while (!condition.test(this)) go();
            return this;
        }

        public It turnRight() {
            final int rdx = -dy, rdy = dx;
            this.dx = rdx;
            this.dy = rdy;
            return this;
        }

        public It turnLeft() {
            final int ldx = dy, ldy = -dx;
            this.dx = ldx;
            this.dy = ldy;
            return this;
        }
    }
}

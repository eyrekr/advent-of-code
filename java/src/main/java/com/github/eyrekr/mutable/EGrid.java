package com.github.eyrekr.mutable;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EGrid<E> {

    public static final char Void = '\0';

    public final int m;
    public final int n;
    public final char[][] symbol;
    public final long[][] value;
    public final State[][] state;
    public final Object[][] context;

    private EGrid(final int m, final int n) {
        this.m = m;
        this.n = n;
        this.symbol = new char[m][n];
        this.value = new long[m][n];
        this.state = new State[m][n];
        this.context = new Object[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                symbol[x][y] = Void;
                value[x][y] = 0L;
                state[x][y] = State.Unseen;
                context[x][y] = null;
            }
    }

    private EGrid(final EGrid<? extends E> grid) {
        this.m = grid.m;
        this.n = grid.n;
        this.symbol = new char[m][n];
        this.value = new long[m][n];
        this.state = new State[m][n];
        this.context = new Object[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                symbol[x][y] = grid.symbol[x][y];
                value[x][y] = grid.value[x][y];
                state[x][y] = grid.state[x][y];
                context[x][y] = grid.context[x][y];
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
                grid.symbol[x][y] = lines[y].charAt(x);
        return grid;
    }

    public EGrid<E> duplicate() {
        return new EGrid<>(this);
    }

    public char at(final int x, final int y) {
        return x >= 0 && x < m && y >= 0 && y < n ? symbol[x][y] : Void;
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

    public Filter all() {
        return new Filter(0, 0, Direction.RightDown, unused -> true);
    }

    public Filter where(final Predicate<It> predicate) {
        return new Filter(0, 0, Direction.RightDown, predicate);
    }

    public Filter where(final int x, final int y, final Direction direction, final Predicate<It> predicate) {
        return new Filter(x, y, direction, predicate);
    }

    public EGrid<E> print() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) Out.print("" + symbol[x][y]);
            Out.print("\n");
        }
        return this;
    }

    public EGrid<E> print(final Function<It, String> formatter) {
        final It it = new It(0, 0, Direction.None);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) Out.print(formatter.apply(it.to(x, y)));
            Out.print("\n");
        }
        return this;
    }

    public final class Filter {

        private final Direction direction;
        private final Predicate<It> predicate;

        private int x;
        private int y;
        public It it;

        private Filter(final int x, final int y, final Direction direction, final Predicate<It> predicate) {
            this.direction = direction;
            this.predicate = predicate;
            to(x, y).first();
        }

        private Filter to(final int x, final int y) {
            this.x = x;
            this.y = y;
            it.x = x;
            it.y = y;
            return this;
        }

        private boolean inside() {
            return x >= 0 && x < m && y >= 0 && y < n;
        }

        private Filter first() {
            if (!predicate.test(it)) next();
            return this;
        }

        private Filter next() {
            do {
                final Filter next = switch (direction) {
                    case Right, RightDown, RightUp -> x < m - 1 ? to(x + direction.dx, y) : to(0, y + direction.dy);
                    case Left, LeftDown, LeftUp -> x > 0 ? to(x + direction.dx, y) : to(m - 1, y + direction.dy);
                    case Up, UpLeft, UpRight -> y > 0 ? to(x, y + direction.dy) : to(x + direction.dx, n - 1);
                    case Down, DownLeft, DownRight -> y < n - 1 ? to(x, y + direction.dy) : to(x + direction.dx, 0);
                    case None -> throw new IllegalStateException("no direction");
                };
            } while (inside() && !predicate.test(it));
            return this;
        }

        public Filter each(final Consumer<It> consumer) {
            for (first(); inside(); next()) consumer.accept(it);
            return this;
        }

        public <R> R reduce(final R initialValue, final BiFunction<R, It, R> reducer) {
            R value = initialValue;
            for (first(); inside(); next()) value = reducer.apply(value, it);
            return value;
        }

        public Arr<It> collect() {
            final Arr<It> array = Arr.empty();
            for (first(); inside(); next()) array.addLast(it.duplicate());
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

        public char ch() {
            return inside() ? EGrid.this.symbol[x][y] : Void;
        }

        public char symbol() {
            return ch();
        }

        public long d() {
            return inside() ? EGrid.this.value[x][y] : 0L;
        }

        public State state() {
            return inside() ? EGrid.this.state[x][y] : null;
        }

        public boolean is(final char ch) {
            return inside() && EGrid.this.symbol[x][y] == ch;
        }

        public boolean is(final long d) {
            return inside() && EGrid.this.value[x][y] == d;
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
            if (inside()) EGrid.this.symbol[x][y] = ch;
            return this;
        }

        public It set(final long d) {
            if (inside()) EGrid.this.value[x][y] = d;
            return this;
        }

        public It inc() {
            if (inside()) EGrid.this.value[x][y] = EGrid.this.value[x][y] + 1;
            return this;
        }

        public It inc(final long d) {
            if (inside()) EGrid.this.value[x][y] = EGrid.this.value[x][y] + d;
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
            return inside() ? (E) EGrid.this.context[x][y] : null;
        }

        public It let(final E e) {
            if (inside()) EGrid.this.context[x][y] = e;
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

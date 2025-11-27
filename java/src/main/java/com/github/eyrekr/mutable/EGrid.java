package com.github.eyrekr.mutable;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EGrid {

    public static final char Void = '\0';

    public final int m;
    public final int n;
    public final char[][] symbol;
    public final long[][] value;
    public final State[][] state;

    private EGrid(final int m, final int n) {
        this.m = m;
        this.n = n;
        this.symbol = new char[m][n];
        this.value = new long[m][n];
        this.state = new State[m][n];

        reset();
    }

    private EGrid(final EGrid grid) {
        this.m = grid.m;
        this.n = grid.n;
        this.symbol = new char[m][n];
        this.value = new long[m][n];
        this.state = new State[m][n];

        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                symbol[x][y] = grid.symbol[x][y];
                value[x][y] = grid.value[x][y];
                state[x][y] = grid.state[x][y];
            }
    }

    public static EGrid empty(final int m, final int n) {
        return new EGrid(m, n);
    }

    public static EGrid fromString(final String input) {
        final String[] lines = StringUtils.split(input, "\n");
        final int n = lines.length, m = lines[0].length();
        final EGrid grid = new EGrid(m, n);
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++)
                grid.symbol[x][y] = lines[y].charAt(x);
        return grid;
    }

    public EGrid duplicate() {
        return new EGrid(this);
    }

    public EGrid reset() {
        for (int y = 0; y < n; y++)
            for (int x = 0; x < m; x++) {
                symbol[x][y] = Void;
                value[x][y] = 0L;
                state[x][y] = State.Unseen;
            }
        return this;
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

    public Filter where(final char ch) {
        return new Filter(0, 0, Direction.RightDown, it -> it.is(ch));
    }

    public Filter where(final State state) {
        return new Filter(0, 0, Direction.RightDown, it -> it.is(state));
    }

    public Filter where(final Predicate<It> predicate) {
        return new Filter(0, 0, Direction.RightDown, predicate);
    }

    public Filter where(final int x, final int y, final Direction direction, final Predicate<It> predicate) {
        return new Filter(x, y, direction, predicate);
    }

    public EGrid print() {
        Out.print(toString());
        return this;
    }

    public EGrid print(final Function<It, String> formatter) {
        Out.print(toString(formatter));
        return this;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) sb.append(symbol[x][y]);
            sb.append('\n');
        }
        return sb.toString();
    }

    public String toString(final Function<It, String> formatter) {
        final var sb = new StringBuilder();
        final var it = new It(0, 0, Direction.None);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) sb.append(formatter.apply(it.to(x, y)));
            sb.append('\n');
        }
        return sb.toString();
    }

    public final class Filter {

        private final Direction direction;
        private final Predicate<It> predicate;
        public final It it;

        private int x;
        private int y;

        private Filter(final int x, final int y, final Direction direction, final Predicate<It> predicate) {
            this.direction = direction;
            this.predicate = predicate;
            this.it = new It(x, y, direction);
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

        public It first() {
            if (!predicate.test(it)) next();
            return it;
        }

        public It next() {
            do {
                final Filter next = switch (direction) {
                    case Right, RightDown, RightUp -> x < m - 1 ? to(x + direction.dx, y) : to(0, y + direction.dy);
                    case Left, LeftDown, LeftUp -> x > 0 ? to(x + direction.dx, y) : to(m - 1, y + direction.dy);
                    case Up, UpLeft, UpRight -> y > 0 ? to(x, y + direction.dy) : to(x + direction.dx, n - 1);
                    case Down, DownLeft, DownRight -> y < n - 1 ? to(x, y + direction.dy) : to(x + direction.dx, 0);
                    case None -> throw new IllegalStateException("no direction");
                };
            } while (inside() && !predicate.test(it));
            return it;
        }

        public Filter each(final Consumer<It> action) {
            for (first(); inside(); next()) action.accept(it);
            return this;
        }

        public <R> R reduce(final R initialValue, final BiFunction<R, It, R> reducer) {
            R value = initialValue;
            for (first(); inside(); next()) value = reducer.apply(value, it);
            return value;
        }

        public long sum(final Function<It, Long> function) {
            long value = 0L;
            for (first(); inside(); next()) value += function.apply(it);
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

        public boolean debug = false;
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
            return at(x, y);
        }

        public char symbol() {
            return at(x, y);
        }

        public long value() {
            return inside() ? value[x][y] : 0L;
        }

        public State state() {
            return inside() ? state[x][y] : null;
        }

        public boolean is(final char ch) {
            return at(x, y) == ch;
        }

        public boolean isOneOf(final char ch, final char... chars) {
            final char symbol = at(x, y);
            if (symbol == ch) return true;
            for (final char c : chars) if (symbol == c) return true;
            return false;
        }

        public boolean isNot(final char ch) {
            return at(x, y) != ch;
        }

        public boolean is(final long d) {
            return value() == d;
        }

        public boolean isNot(final long d) {
            return value() != d;
        }

        public boolean is(final State s) {
            return state() == s;
        }

        public boolean isNot(final State s) {
            return state() != s;
        }

        public boolean isAhead(final char ch) {
            return at(x + dx, y + dy) == ch;
        }

        public boolean isAhead(final Direction direction, final char ch) {
            return at(x + direction.dx, y + direction.dy) == ch;
        }

        public boolean isAheadOneOf(final char... chars) {
            final char symbol = at(x + dx, y + dy);
            for (final char ch : chars) if (symbol == ch) return true;
            return false;
        }

        public int findDistanceToFirstAhead(final char... chars) {
            int k = 0;
            while (true) {
                k++;
                final char la = la(k);
                if (la == Void) return -1;
                for (final char ch : chars) if (ch == la) return k;
            }
        }

        public char findFirstAhead(final char... chars) {
            int k = 0;
            while (true) {
                k++;
                final char la = la(k);
                if (la == Void) return Void;
                for (final char ch : chars) if (ch == la) return ch;
            }
        }

        public int manhattanDistanceTo(final It it) {
            return Math.abs(x - it.x) + Math.abs(y - it.y);
        }

        public int chebyshevDistanceTo(final It it) {
            return Math.max(Math.abs(x - it.x), Math.abs(y - it.y));
        }

        public char la() {
            return at(x + dx, y + dy);
        }

        public char la(final int steps) {
            return at(x + steps * dx, y + steps * dy);
        }

        public char la(final Direction direction) {
            return at(x + direction.dx, y + direction.dy);
        }

        public char la(final Direction direction, final int steps) {
            return at(x + steps * direction.dx, y + steps * direction.dy);
        }

        public char la(final int dx, final int dy) {
            return at(x + dx, y + dy);
        }

        public It setSymbol(final char ch) {
            if (inside()) symbol[x][y] = ch;
            return this;
        }

        public It setValue(final long d) {
            if (inside()) value[x][y] = d;
            return this;
        }

        public It incValue() {
            if (inside()) value[x][y] = value[x][y] + 1;
            return this;
        }

        public It incValue(final long d) {
            if (inside()) value[x][y] = value[x][y] + d;
            return this;
        }

        public It setState(final State s) {
            if (inside()) state[x][y] = s;
            return this;
        }

        public It setDirection(final Direction direction) {
            this.dx = direction.dx;
            this.dy = direction.dy;
            return this;
        }

        public It setDirection(final int dx, final int dy) {
            this.dx = dx;
            this.dy = dy;
            return this;
        }

        public It to(final int x, final int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public It go() {
            guard();
            this.x += dx;
            this.y += dy;
            return this;
        }

        public It go(final Direction direction) {
            guard();
            this.x += direction.dx;
            this.y += direction.dy;
            return this;
        }

        public It go(final int steps) {
            guard();
            this.x += steps * dx;
            this.y += steps * dy;
            return this;
        }

        public It go(final int dx, final int dy) {
            guard();
            this.x += dx;
            this.y += dy;
            return this;
        }

        public It goBack() {
            guard();
            this.x -= dx;
            this.y -= dy;
            return this;
        }

        public It goWhile(final Predicate<It> condition) {
            while (condition.test(this)) go();
            return this;
        }

        public It goWhile(final char ch, final char... chars) {
            return goWhile(it -> it.isOneOf(ch, chars));
        }

        public It goUntil(final Predicate<It> condition) {
            while (!condition.test(this)) go();
            return this;
        }

        public It goUntil(final char ch, final char... chars) {
            return goUntil(it -> it.isOneOf(ch, chars));
        }

        public It doWhile(final Consumer<It> action, final Predicate<It> condition) {
            while (condition.test(this)) {
                action.accept(this);
                go();
            }
            return this;
        }

        public It doWhile(final Consumer<It> action, final char ch, final char... chars) {
            return doWhile(action, it -> it.isOneOf(ch, chars));
        }

        public It doUntil(final Consumer<It> action, final Predicate<It> condition) {
            while (!condition.test(this)) {
                action.accept(this);
                go();
            }
            return this;
        }

        public It doUntil(final Consumer<It> action, final char ch, final char... chars) {
            return doUntil(action, it -> it.isOneOf(ch, chars));
        }

        public It turnRight() {
            guard();
            final int t = dx;
            this.dx = -dy;
            this.dy = t;
            return this;
        }

        public It turnLeft() {
            guard();
            final int t = -dx;
            this.dx = dy;
            this.dy = t;
            return this;
        }

        public It turnAround() {
            guard();
            this.dx = -dx;
            this.dy = -dy;
            return this;
        }

        public It guard() {
            if (debug && dx == 0 && dy == 0) throw new DirectionNotSpecifiedException();
            return this;
        }

        public It bfs() {
            return bfs(Arr.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right));
        }

        public It bfs(final Arr<Direction> directions) {
            final Arr<It> queue = Arr.of(this);
            while (queue.isNotEmpty()) {
                final It it = queue.removeFirst();
                if (it.is(State.Closed)) continue; // closed in the meantime, there might be more paths to one spot
                it.setState(State.Closed);
                directions
                        .map(direction -> it.duplicate().go(direction))
                        .where(It::inside)
                        .where(neighbour -> neighbour.isNot(State.Closed))
                        .each(neighbour -> neighbour.setValue(it.value() + 1))
                        .each(queue::addLast);
            }
            return this;
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof final EGrid.It that) && x == that.x && y == that.y && dx == that.dx && dy == that.dy;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, dx, dy);
        }
    }

    public class DirectionNotSpecifiedException extends IllegalStateException {
    }
}

package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

class D06 extends Aoc {

    final Arr<String> lines;
    final Arr<Operation> operations;
    final Arr<Longs> rows;

    D06(final String input) {
        lines = Arr.ofLinesFromString(input);
        rows = lines.removeLast().map(Longs::fromString);

        final String lastLine = StringUtils.remove(lines.at(-1), ' ');
        operations = Arr.ofCharactersFromString(lastLine).map(Operation::fromSymbol);
    }

    @Override
    public long star1() {
        return Longs.range(0, operations.length)
                .map(i -> rows.mapToLongs(row -> row.at(i)).reduce(operations.at(i)))
                .sum();
    }

    @Override
    public long star2() {
        final String lastLine = lines.at(-1);
        final Arr<Integer> positions = Arr.ofCharactersFromString(lastLine).argsWhere(ch -> ch == '+' || ch == '*');
        final Longs numbers = numbersByColumns(lines.removeLast());
        return positions.mapWith(
                        positions.removeFirst().addLast(lastLine.length()),
                        (i, j) -> numbers.between(i, j - 2).reduce(operations.at(i)))
                .reduce(Long::sum)
                .get();
    }

    Longs numbersByColumns(final Arr<String> lines) {
        final int n = lines.at(0).length();
        return Longs.range(0, n)
                .map(column -> lines.reduce(
                        0L,
                        (value, line) -> {
                            final char ch = line.charAt((int) column);
                            return Character.isDigit(ch) ? value * 10 + Character.digit(ch, 10) : value;
                        }));
    }

    enum Operation implements Longs.LongLongToLong {
        Sum, Prod;

        static Operation fromSymbol(final char symbol) {
            return switch (symbol) {
                case '+' -> Sum;
                case '*' -> Prod;
                default -> throw new UnsupportedOperationException("Unsupported operation: " + symbol);
            };
        }

        @Override
        public long apply(final long left, final long right) {
            return switch (this) {
                case Sum -> left + right;
                case Prod -> left * right;
            };
        }
    }
}
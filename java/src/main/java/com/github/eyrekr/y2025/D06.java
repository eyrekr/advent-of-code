package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

class D06 extends Aoc {

    final Arr<String> lines;
    final int n;

    D06(final String input) {
        lines = Arr.ofLinesFromString(input);
        n = lines.map(String::length).max(Integer::compare).get();
    }

    @Override
    public long star1() {
        final String lastLineWithOperatorsOnly = StringUtils.remove(lines.at(-1), ' ');
        final Arr<Operation> operations = Arr.ofCharactersFromString(lastLineWithOperatorsOnly).map(Operation::fromSymbol);
        final Arr<Longs> rows = lines.removeLast().map(Longs::fromString);

        return Longs.range(0, operations.length)
                .map(i -> rows.mapToLongs(row -> row.at(i)).reduce(operations.at(i)))
                .sum();
    }

    @Override
    public long star2() {
        final String lastLine = lines.at(-1);
        final Arr<Integer> positions = Arr.ofCharactersFromString(lastLine).argsWhere(ch -> ch == '+' || ch == '*');
        final Longs numbersInColumns = numbersInColumns(lines.removeLast());
        return positions.mapWith(
                        positions.removeFirst().addLast(n + 1),
                        (i, j) -> numbersInColumns.between(i, j - 2).reduce(Operation.fromSymbol(lastLine.charAt(i))))
                .reduce(Long::sum)
                .get();
    }

    Longs numbersInColumns(final Arr<String> lines) {
        return Longs.range(0, n)
                .map(column -> lines.reduce(
                        0L,
                        (value, line) -> {
                            final char ch = column < line.length() ? line.charAt((int) column) : ' ';
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
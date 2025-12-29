package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

class D06 extends Aoc {

    final Arr<Character> operations;
    final Arr<Longs> rows;

    D06(final String input) {
        final Arr<String> lines = Arr.ofLinesFromString(input);
        rows = lines.removeLast().map(Longs::fromString);

        final String lastLine = StringUtils.remove(lines.at(-1), ' ');
        operations = Arr.ofCharactersFromString(lastLine);
    }

    @Override
    public long star1() {
        long result = 0;
        for (int i = 0; i < operations.length; i++) {
            int position = i;
            final Longs column = rows.mapToLongs(row -> row.at(position));
            result += switch (operations.at(i)) {
                case '+' -> column.sum();
                case '*' -> column.prod();
                default -> throw new UnsupportedOperationException("Unsupported operation: " + operations.at(i));
            };
        }
        return result;
    }

    @Override
    public long star2() {
        return -1L;
    }

}
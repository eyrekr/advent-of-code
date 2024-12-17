package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

class D05 extends Aoc {

    final Set<Long> order;
    final Arr.Partition<Longs> pages;

    D05(final String input) {
        final var block = StringUtils.splitByWholeSeparator(input, "\n\n");
        order = Arr.ofLinesFromString(block[0]).map(D05::code).toSet();
        pages = Arr.ofLinesFromString(block[1])
                .map(Longs::fromString)
                .partitionBy(numbers -> numbers.prodUpperTriangleWith(numbers, (a, b) -> code(b, a)).noneIs(order::contains));
    }

    @Override
    public long star1() {
        return pages.matching()
                .map(numbers -> numbers.at(numbers.length / 2))
                .reduce(0L, Long::sum);
    }

    static long code(final String line) { // 75|47
        final var number = StringUtils.split(line, '|');
        return code(Long.parseLong(number[0]), Long.parseLong(number[1]));
    }

    static long code(final long a, final long b) {
        return (a << 32) | b;
    }
}
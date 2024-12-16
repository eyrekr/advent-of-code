package com.github.eyrekr.y2024;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2024/day/3
 * 1) 174336360
 * 2) 88802350
 */
class D03 {

    final Pattern pattern = Pattern.compile("(mul\\(\\d{1,3},\\d{1,3}\\))", Pattern.MULTILINE);
    final String input;

    D03(final String input) {
        this.input = input;
    }

    long star1() {
        return Arr.fromMatcher(pattern.matcher(input))
                .map(instruction -> Longs.fromString(instruction).prod())
                .reduce(0L, Long::sum);
    }

    long star2() {
        final var relevantInput = Arr.fromArray(StringUtils.splitByWholeSeparator(input, "do()"))
                .map(line -> StringUtils.splitByWholeSeparator(line, "don't()")[0])
                .toString("");

        return Arr.fromMatcher(pattern.matcher(relevantInput))
                .map(instruction -> Longs.fromString(instruction).prod())
                .reduce(0L, Long::sum);
    }

}
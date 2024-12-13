package com.github.eyrekr.y2024;

import com.github.eyrekr.immutable.Seq;

/**
 * https://adventofcode.com/2024/day/1
 * 1) 1834060
 * 2) 21607792
 */
class D01 {
    final Seq<Long> leftList;
    final Seq<Long> rightList;

    D01(final String input) {
        final var numbers = Seq.ofNumbersFromString(input);
        leftList = numbers.contextWhere(it -> it.length % 2 == 0);
        rightList = numbers.contextWhere(it -> it.length % 2 == 1);
    }

    long star1() {
        return leftList.sorted().reduceWith(
                rightList.sorted(),
                0L,
                (sum, leftValue, rightValue) -> sum + Math.abs(leftValue - rightValue));
    }

    long star2() {
        final var histogram = rightList.frequency();
        return leftList.reduce(
                0L,
                (sum, value) -> sum + value * histogram.getOrDefault(value, 0));
    }

}
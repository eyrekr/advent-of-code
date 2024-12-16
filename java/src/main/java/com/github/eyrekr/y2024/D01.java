package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;

class D01 extends Aoc {

    final Seq<Long> leftList;
    final Seq<Long> rightList;

    D01(final String input) {
        final var numbers = Seq.ofNumbersFromString(input);
        leftList = numbers.contextWhere(it -> it.length % 2 == 0);
        rightList = numbers.contextWhere(it -> it.length % 2 == 1);
    }

    @Override
    public long star1() {
        return leftList.sorted().reduceWith(
                rightList.sorted(),
                0L,
                (sum, leftValue, rightValue) -> sum + Math.abs(leftValue - rightValue));
    }

    @Override
    public long star2() {
        final var histogram = rightList.frequency();
        return leftList.reduce(
                0L,
                (sum, value) -> sum + value * histogram.getOrDefault(value, 0));
    }

}
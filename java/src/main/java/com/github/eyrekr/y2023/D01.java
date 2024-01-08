package com.github.eyrekr.y2023;

import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/1
 * 1) 54331
 * 2) 54518
 */
class D01 {
    final Seq<String> lines;

    D01(final String input) {
        lines = Seq.ofLinesFromString(input);
    }

    long star1() {
        return lines.map(D01::toNumber).reduce(0L, Long::sum);
    }

    long star2() {
        return lines.map(D01::translate).map(D01::toNumber).reduce(0L, Long::sum);
    }

    static String translate(final String input) {
        String text = input;

        //mixed
        text = StringUtils.replace(text, "oneight", "18");
        text = StringUtils.replace(text, "twone", "21");
        text = StringUtils.replace(text, "threeight", "38");
        text = StringUtils.replace(text, "fiveight", "58");
        text = StringUtils.replace(text, "sevenine", "79");
        text = StringUtils.replace(text, "eightwo", "82");
        text = StringUtils.replace(text, "eighthree", "83");
        text = StringUtils.replace(text, "nineight", "98");

        //simple
        text = StringUtils.replace(text, "one", "1");
        text = StringUtils.replace(text, "two", "2");
        text = StringUtils.replace(text, "three", "3");
        text = StringUtils.replace(text, "four", "4");
        text = StringUtils.replace(text, "five", "5");
        text = StringUtils.replace(text, "six", "6");
        text = StringUtils.replace(text, "seven", "7");
        text = StringUtils.replace(text, "eight", "8");
        text = StringUtils.replace(text, "nine", "9");

        return text;
    }

    static int toNumber(final String input) {
        int first = -1, last = -1;
        for (final char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                last = Character.digit(ch, 10);
            }
            if (first < 0) first = last;
        }
        return first * 10 + last;
    }
}
package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/1
 * 1) 54331
 * 2) 54518
 */
class D01 extends AoC {


    public D01(String input) {
        super(input);
    }

    @Override
    long star1() {
        return lines.map(text-> StringUtils.removeAll(text, "[a-zA-Z_]")).map(D01::toNumber).reduce(0L, Long::sum);
    }


    @Override
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

        return StringUtils.removeAll(text, "[a-zA-Z_]");
    }

    static int toNumber(final String input) {
        return (input.charAt(0) - '0') * 10 + (input.charAt(input.length() - 1) - '0');
    }
}
package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/1">...</a>
 * 1) 54331
 * 2) 54518
 */
public class Day1 {

    public static void main(final String... args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/resources/01.txt"));
        final var sum = lines.stream().map(Day1::translate).mapToInt(Day1::toNumber).sum();
        System.out.println(sum);
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
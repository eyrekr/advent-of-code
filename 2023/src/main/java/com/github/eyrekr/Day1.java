package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/1">...</a>
 * <p>
 * 1) 54331
 * 2) 54518
 */
public class Day1 {

    public static void main(String... args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/01.txt"));

        long sum = 0;
        for (String line : lines) {
            String translated = translate(line);
            int value = Integer.parseInt("" + translated.charAt(0) + translated.charAt(translated.length() - 1));
            sum += value;
            System.out.printf("%-60s%20s%4d\n", line, translated, value);
        }

        System.out.printf("SUM = %d\n", sum);
    }

    static String translate(String input) {
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
}
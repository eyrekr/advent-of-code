package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 * <a href="https://adventofcode.com/2023/day/1">...</a>
 * <p>
 * 1) 54331
 * 2) 54518
 */
public class Day1 {

    static final LinkedHashMap<String, String> REPLACEMENTS;

    static {
        REPLACEMENTS = new LinkedHashMap<>();
        //mixed
        REPLACEMENTS.put("oneight", "18");
        REPLACEMENTS.put("twone", "21");
        REPLACEMENTS.put("threeight", "38");
        REPLACEMENTS.put("fiveight", "58");
        REPLACEMENTS.put("sevenine", "79");
        REPLACEMENTS.put("eightwo", "82");
        REPLACEMENTS.put("eighthree", "83");
        REPLACEMENTS.put("nineight", "98");
        //simple
        REPLACEMENTS.put("one", "1");
        REPLACEMENTS.put("two", "2");
        REPLACEMENTS.put("three", "3");
        REPLACEMENTS.put("four", "4");
        REPLACEMENTS.put("five", "5");
        REPLACEMENTS.put("six", "6");
        REPLACEMENTS.put("seven", "7");
        REPLACEMENTS.put("eight", "8");
        REPLACEMENTS.put("nine", "9");
    }

    public static void main(String... args) throws Exception {
        String input = Files.readString(Path.of("src/main/resources/01.txt"));

        long sum = 0;
        for (String line : StringUtils.split(input)) {
            String translated = translate(line);
            int value = Integer.parseInt("" + translated.charAt(0) + translated.charAt(translated.length() - 1));
            sum += value;
            System.out.printf("%-60s%20s%4d\n", line, translated, value);
        }

        System.out.printf("SUM = %d\n", sum);
    }

    static String translate(String input) {
        String text = input;
        for (var entry : REPLACEMENTS.entrySet()) {
            text = StringUtils.replace(text, entry.getKey(), entry.getValue());
        }
        return StringUtils.removeAll(text, "[a-z]");
    }
}
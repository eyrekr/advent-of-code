package com.github.eyrekr;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

public class Day1 {

    public static void main(String[] args) throws Exception {
        final String input = Files.readString(Path.of("src/main/resources/01.txt"));
        System.out.printf("SUM = %d\n", sumWithTranslation(input) /*54518*/);
    }

    static long sumWithTranslation(final String input) {
        long sum = 0;
        for (final String line : StringUtils.split(input)) {
            final int number = extractNumber(line);
            //sum += number;
            final R control = T.translate(line);
            sum += control.value;
            System.out.printf("%-60s%10d%20d%10d%s\n", line, number, control.whole, control.value, control.value != number ? " !! " : "");
        }
        return sum;
    }

    static int extractNumber(final String line) {
        var matcher1 = PATTERN.matcher(line);
        matcher1.find();
        var d1 = VALUES.get(matcher1.group());

        var matcher2 = PATTERN_INVERTED.matcher(StringUtils.reverse(line));
        matcher2.find();
        var d2 = VALUES.get(StringUtils.reverse(matcher2.group()));

        return d1 * 10 + d2;
    }


    record T(int from, char over, int to, int emit) {

        private static final T RESET = new T(0, '*', 0);

        private T(final int from, final char over, final int to) {
            this(from, over, to, Integer.MIN_VALUE);
        }

        private static T find(final int from, final char over) {
            for (final var t : TABLE) {
                if (t.from == from && t.over == over) {
                    return t;
                }
            }
            //reset to state 0 and continue from there
            for (final var t : TABLE) {
                if (t.from == 0 && t.over == over) {
                    return t;
                }
            }
            // no such transition
            return RESET;
        }

        static R translate(final String input) {
            int state = 0;
            int number = 0;
            int hi = 0;
            int lo = 0;
            for (final char ch : input.toCharArray()) {
                final var t = find(state, ch);
                if (t.emit >= 0) {
                    number = number * 10 + t.emit;
                    if (hi == 0) {
                        hi = t.emit;
                        lo = t.emit;
                    } else {
                        lo = t.emit;
                    }
                }
                state = t.to;
            }
            return new R(number, hi * 10 + lo);
        }
    }

    record R(int whole, int value) {
    }

    static final T[] TABLE = new T[]{
            new T(0, '1', 0, 1),
            new T(0, '2', 0, 2),
            new T(0, '3', 0, 3),
            new T(0, '4', 0, 4),
            new T(0, '5', 0, 5),
            new T(0, '6', 0, 6),
            new T(0, '7', 0, 7),
            new T(0, '8', 0, 8),
            new T(0, '9', 0, 9),
            new T(0, 'o', 1),
            new T(0, 't', 4),
            new T(0, 's', 16),
            new T(0, 'e', 9),
            new T(0, 'f', 10),
            new T(0, 'n', 22),
            new T(1, 'n', 2),
            new T(2, 'e', 9, 1),
            new T(2, 'i', 23),
            new T(4, 'w', 5),
            new T(4, 'h', 6),
            new T(5, 'o', 1, 2),
            new T(6, 'r', 7),
            new T(7, 'e', 8),
            new T(8, 'e', 9, 3),
            new T(8, 'i', 25),
            new T(9, 'i', 25),
            new T(10, 'i', 14),
            new T(10, 'o', 11),
            new T(11, 'u', 12),
            new T(11, 'n', 2),
            new T(12, 'r', 0, 4),
            new T(14, 'v', 15),
            new T(15, 'e', 9, 5),
            new T(16, 'i', 17),
            new T(16, 'e', 19),
            new T(17, 'x', 0, 6),
            new T(19, 'v', 20),
            new T(19, 'i', 25),
            new T(20, 'e', 21),
            new T(21, 'n', 22, 7),
            new T(21, 'i', 25),
            new T(22, 'i', 23),
            new T(23, 'n', 24),
            new T(24, 'i', 23),
            new T(24, 'e', 9, 9),
            new T(25, 'g', 26),
            new T(26, 'h', 27),
            new T(27, 't', 4, 8),
    };

    static final Pattern PATTERN = Pattern.compile("(one|two|three|four|five|six|seven|eight|nine|\\d)");
    static final Pattern PATTERN_INVERTED = Pattern.compile("(eno|owt|eerht|ruof|evif|xis|neves|thgie|enin|\\d)");

    static final Map<String, Integer> VALUES = ImmutableMap.<String, Integer>builder()
            .put("one", 1)
            .put("two", 2)
            .put("three", 3)
            .put("four", 4)
            .put("five", 5)
            .put("six", 6)
            .put("seven", 7)
            .put("eight", 8)
            .put("nine", 9)
            .put("1", 1)
            .put("2", 2)
            .put("3", 3)
            .put("4", 4)
            .put("5", 5)
            .put("6", 6)
            .put("7", 7)
            .put("8", 8)
            .put("9", 9)
            .build();

}

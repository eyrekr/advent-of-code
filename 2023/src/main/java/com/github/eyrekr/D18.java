package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/18
 * 1) 28911
 * 2)
 */
class D18 extends AoC {

    final Seq<I> instructions;

    D18(final String input) {
        super(input);
        this.instructions = lines.map(I::of);
    }

    long star1() {
        return area(instructions);
    }

    long star2() {
        return area(instructions.map(I::convert));
    }

    long area(final Seq<I> instructions) {
        var p = new P(0, 0);
        long area = 0;
        long circumference = 0;
        for (final var i : instructions) {
            switch (i.d) {
                case 'U' -> {
                    p = new P(p.x, p.y - i.l);
                }
                case 'D' -> {
                    circumference += i.l;
                    p = new P(p.x, p.y + i.l);
                }
                case 'R' -> {
                    area -= p.y * i.l;
                    p = new P(p.x + i.l, p.y);
                }
                case 'L' -> {
                    area += p.y * i.l;
                    circumference += i.l;
                    p = new P(p.x - i.l, p.y);
                }
            }
        }
        return area + circumference + 1;
    }

    record I(char d, long l, String color) {
        static I of(final String input) {
            final var data = StringUtils.split(input, " ()#");
            return new I(data[0].charAt(0), Long.parseLong(data[1]), data[2]);
        }

        I convert() {
            // 0 means R, 1 means D, 2 means L, and 3 means U.
            return new I(
                    switch (color.charAt(5)) {
                        case '0' -> 'R';
                        case '1' -> 'D';
                        case '2' -> 'L';
                        case '3' -> 'U';
                        default -> throw new IllegalStateException(color);
                    },
                    Long.parseLong(StringUtils.substring(color, 0, 5), 16),
                    color);
        }
    }

    record P(long x, long y) {
    }
}
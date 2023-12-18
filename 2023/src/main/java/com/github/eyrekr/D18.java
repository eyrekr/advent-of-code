package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/18
 * 1) 28911
 * 2) 77366737561114
 */
class D18 extends AoC {

    final Seq<Step> steps;

    D18(final String input) {
        super(input);
        this.steps = lines.map(Step::of);
    }

    long star1() {
        return area(steps);
    }

    long star2() {
        return area(steps.map(Step::convert));
    }

    long area(final Seq<Step> steps) {
        var p = new P(0, 0);
        long area = 0;
        long circumference = 0;
        for (final var step : steps) {
            switch (step.direction) {
                case 'U' -> {
                    p = new P(p.x, p.y - step.distance);
                }
                case 'D' -> {
                    circumference += step.distance;
                    p = new P(p.x, p.y + step.distance);
                }
                case 'R' -> {
                    area -= p.y * step.distance;
                    p = new P(p.x + step.distance, p.y);
                }
                case 'L' -> {
                    area += p.y * step.distance;
                    circumference += step.distance;
                    p = new P(p.x - step.distance, p.y);
                }
            }
        }
        return area + circumference + 1;
    }

    record Step(char direction, long distance, String color) {
        static Step of(final String input) {
            final var data = StringUtils.split(input, " ()#");
            return new Step(data[0].charAt(0), Long.parseLong(data[1]), data[2]);
        }

        Step convert() {
            // 0 means R, 1 means D, 2 means L, and 3 means U.
            return new Step(
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
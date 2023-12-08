package com.github.eyrekr;

import com.github.eyrekr.util.Mth;
import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * https://adventofcode.com/2023/day/8
 * 1) 19241
 * 2) 9606140307013
 */
class D08 extends AoC<D08.In> {

    public static void main(String[] args) {
        new D08().run(In::from);
    }

    record In(String directions, Map<String, Step> map) {
        static In from(Seq<String> lines) {
            return new In(lines.value, lines.tail.tail.map(Step::from).toMap(Step::source));
        }

        String next(final String from, final long step) {
            final int i = (int) (step % directions.length());
            return Objects.equals(directions.charAt(i), 'L') ? map.get(from).left : map.get(from).right;
        }
    }

    record Step(String source, String left, String right) {
        static Step from(final String input) {
            final String[] data = StringUtils.split(input, " =(),");
            return new Step(data[0], data[1], data[2]);
        }
    }

    long star1() {
        return steps("AAA", "ZZZ");
    }

    long star2() {
        return Seq.fromIterable(in.map.keySet())
                .where(key -> key.endsWith("A"))
                .map(start -> steps(start, "Z"))
                .reduce(1L, Mth::lcm);
    }

    long steps(final String start, final String end) {
        String current = start;
        long step = 0;
        while (!current.endsWith(end)) {
            current = in.next(current, step);
            step++;
        }
        return step;
    }

}

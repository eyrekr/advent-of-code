package com.github.eyrekr;

import com.github.eyrekr.util.Mth;
import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 1) 19241
 * 2) 9606140307013
 */
class Day8 extends AoC<Day8.In> {

    public static void main(String[] args) {
        new Day8().run(8, In::from);
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

    long star1(final In in) {
        return steps(in, "AAA", "ZZZ");
    }

    long star2(final In in) {
        return Seq.fromIterable(in.map.keySet())
                .where(key -> key.endsWith("A"))
                .map(start -> steps(in, start, "Z"))
                .reduce(1L, Mth::lcm);
    }

    static long steps(final In in, final String start, final String end) {
        String current = start;
        long step = 0;
        while (!current.endsWith(end)) {
            current = in.next(current, step);
            step++;
        }
        return step;
    }

}

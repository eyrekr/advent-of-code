package com.github.eyrekr;

import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * https://adventofcode.com/2023/day/8
 * 1) 19241
 * 2) 9606140307013
 */
class D08 extends AoC {

    final Seq<Character> directions;
    final Map<String, Step> map;

    D08(final String input) {
        super(input);
        this.directions = Seq.ofCharactersFromString(lines.value);
        this.map = lines.tail.tail
                .map(line -> StringUtils.split(line, " =(),"))
                .toMap(array -> array[0], array -> new Step(array[1], array[2]));
    }

    long star1() {
        return steps("AAA", "ZZZ");
    }

    long star2() {
        return Seq.fromIterable(map.keySet()).where(key -> key.endsWith("A")).map(start -> steps(start, "Z")).reduce(1L, Algebra::lcm);
    }

    long steps(final String start, final String end) {
        String position = start;
        final var direction = directions.loopingIterator();
        while (!position.endsWith(end)) {
            position = direction.next() == 'L' ? map.get(position).left : map.get(position).right;
        }
        return direction.steps;
    }

    record Step(String left, String right) {
    }
}

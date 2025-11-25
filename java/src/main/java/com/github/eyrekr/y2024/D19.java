package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class D19 extends Aoc {

    final Arr<String> towels;
    final Arr<String> designs;
    private final Map<String, Long> memory = new HashMap<>();

    D19(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        towels = Arr.ofWordsFromString(blocks[0]);
        designs = Arr.ofLinesFromString(blocks[1]);
    }

    @Override
    public long star1() {
        return designs.map(this::calculateArrangementCount).countWhere(value -> value > 0);
    }


    @Override
    public long star2() {
        return designs.map(this::calculateArrangementCount).reduce(Long::sum).getOrElse(-1L);
    }

    long calculateArrangementCount(final String design) {
        if (memory.containsKey(design)) return memory.get(design);

        final long arrangementCount = towels
                .map(towel -> {
                    if (Objects.equals(design, towel)) return 1L;
                    if (design.startsWith(towel)) return calculateArrangementCount(design.substring(towel.length()));
                    return 0L;
                })
                .reduce(Long::sum)
                .getOrElse(0L);

        memory.put(design, arrangementCount);
        return arrangementCount;
    }

}
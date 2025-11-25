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
    final Map<String, Long> waysToComposeDesign;

    D19(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        towels = Arr.ofWordsFromString(blocks[0]);
        designs = Arr.ofLinesFromString(blocks[1]);

        waysToComposeDesign = new HashMap<>();
    }

    @Override
    public long star1() {
        return designs.map(this::countWaysToComposeDesign).countWhere(value -> value > 0);
    }


    @Override
    public long star2() {
        return designs.map(this::countWaysToComposeDesign).reduce(Long::sum).getOrElse(-1L);
    }

    long countWaysToComposeDesign(final String design) {
        final Long n = waysToComposeDesign.getOrDefault(design, -1L);
        if (n >= 0) return n;

        long k = towels
                .map(towel -> {
                    if (Objects.equals(design, towel)) return 1L;
                    if (design.startsWith(towel)) return countWaysToComposeDesign(design.substring(towel.length()));
                    return 0L;
                })
                .reduce(Long::sum)
                .getOrElse(0L);
        waysToComposeDesign.put(design, k);
        return k;
    }
}
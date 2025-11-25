package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.common.MemoizedFunction;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

class D19 extends Aoc {

    final Arr<String> towels;
    final Arr<String> designs;
    final MemoizedFunction<String, Long> waysToComposeDesign;

    D19(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        towels = Arr.ofWordsFromString(blocks[0]);
        designs = Arr.ofLinesFromString(blocks[1]);

        waysToComposeDesign = new MemoizedFunction<>() {
            @Override
            protected Long calculate(String design) {
                return towels
                        .map(towel -> {
                            if (Objects.equals(design, towel)) return 1L;
                            if (design.startsWith(towel)) return get(design.substring(towel.length()));
                            return 0L;
                        })
                        .reduce(Long::sum)
                        .getOrElse(0L);
            }
        };
    }

    @Override
    public long star1() {
        return designs.map(waysToComposeDesign::get).countWhere(value -> value > 0);
    }


    @Override
    public long star2() {
        return designs.map(waysToComposeDesign::get).reduce(Long::sum).getOrElse(-1L);
    }

}
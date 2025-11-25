package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

class D19 extends Aoc {

    final Arr<String> patterns;
    final Arr<String> words;

    D19(final String input) {
        final var blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        patterns = Arr.ofWordsFromString(blocks[0]).sortedBy(String::length);
        words = Arr.ofLinesFromString(blocks[1]);
    }

    @Override
    public long star1() {
        return words.countWhere(this::isDesignPossible);
    }

    private boolean isDesignPossible(final String word) {
        if (StringUtils.isBlank(word)) return true;
        for (final String pattern : patterns)
            if (word.startsWith(pattern)) {
                final boolean solved = isDesignPossible(word.substring(pattern.length()));
                if (solved) {
                    Out.print("-", pattern);
                    return true;
                }
            }
        return false;
    }

    @Override
    public long star2() {
        return -1;
    }

}
package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

class D02 extends Aoc {


    final Seq<Int> ranges;

    D02(final String input) {
        ranges = Seq.ofLinesFromString(input, ",").map(Int::fromString);
    }

    @Override
    public long star1() {
        ranges.print("\n");
        return -1L;
    }

    @Override
    public long star2() {
        return -1L;
    }

}
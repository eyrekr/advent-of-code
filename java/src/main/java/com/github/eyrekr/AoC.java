package com.github.eyrekr;

import com.github.eyrekr.immutable.Seq;

class AoC {

    final String input;
    final Seq<String> lines;

    AoC(final String input) {
        this.input = input;
        this.lines = Seq.ofLinesFromString(input);
    }

    long star1() {
        return 0L;
    }

    long star2() {
        return 0L;
    }
}
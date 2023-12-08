package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

abstract class AoC {

    final String input;
    final Seq<String> lines;

    AoC(final String input) {
        this.input = input;
        this.lines = Seq.ofLinesFromString(input);
    }

    abstract long star1();

    abstract long star2();

}
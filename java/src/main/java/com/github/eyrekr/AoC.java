package com.github.eyrekr;

import com.github.eyrekr.immutable.Seq;

public class AoC {

    protected final String input;
    protected final Seq<String> lines;

    protected AoC(final String input) {
        this.input = input;
        this.lines = Seq.ofLinesFromString(input);
    }
}
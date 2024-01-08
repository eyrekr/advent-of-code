package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;

public class D01 {

    final Seq<String> blocks;

    D01(final String input) {
        blocks = Seq.fromArray(input.split("\n\n"));
    }

    long star1() {
        return blocks.map(Longs::fromString).toLongs(Longs::sum).max();
    }

    long star2() {
        return blocks.map(Longs::fromString).toLongs(Longs::sum).sorted().last(3).sum();
    }
}

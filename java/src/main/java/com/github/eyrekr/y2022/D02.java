package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Seq;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class D02 {

    final Seq<String> lines;

    D02(final String input) {
        lines = Seq.ofLinesFromString(input);
    }

    long star1() {
        // A|X=rock  B|Y=paper  C|Z=scissors
        final Map<String, Integer> score = ImmutableMap.<String, Integer>builder()
                .put("A X", 1 + 3)
                .put("A Y", 2 + 6)
                .put("A Z", 3 + 0)
                .put("B X", 1 + 0)
                .put("B Y", 2 + 3)
                .put("B Z", 3 + 6)
                .put("C X", 1 + 6)
                .put("C Y", 2 + 0)
                .put("C Z", 3 + 3)
                .build();
        return lines.toLongs(score::get).sum();
    }

    long star2() {
        // X=lose  Y=draw  Z=win
        final Map<String, Integer> score = ImmutableMap.<String, Integer>builder()
                .put("A X", 3 + 0)
                .put("A Y", 1 + 3)
                .put("A Z", 2 + 6)
                .put("B X", 1 + 0)
                .put("B Y", 2 + 3)
                .put("B Z", 3 + 6)
                .put("C X", 2 + 0)
                .put("C Y", 3 + 3)
                .put("C Z", 1 + 6)
                .build();
        return lines.toLongs(score::get).sum();
    }
}

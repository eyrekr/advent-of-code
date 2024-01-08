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
        final Map<String, Round> rounds = ImmutableMap.<String, Round>builder()
                .put("A X", new Round(Hand.Rock, Hand.Rock, 1 + 3))
                .put("A Y", new Round(Hand.Rock, Hand.Paper, 2 + 6))
                .put("A Z", new Round(Hand.Rock, Hand.Scissors, 3 + 0))
                .put("B X", new Round(Hand.Paper, Hand.Rock, 1 + 0))
                .put("B Y", new Round(Hand.Paper, Hand.Paper, 2 + 3))
                .put("B Z", new Round(Hand.Paper, Hand.Scissors, 3 + 6))
                .put("C X", new Round(Hand.Scissors, Hand.Rock, 1 + 6))
                .put("C Y", new Round(Hand.Scissors, Hand.Paper, 2 + 0))
                .put("C Z", new Round(Hand.Scissors, Hand.Scissors, 3 + 3))
                .build();
        return lines.map(rounds::get).toLongs(Round::score).sum();
    }

    long star2() {
        // X=lose  Y=draw  Z=win
        final Map<String, Round> rounds = ImmutableMap.<String, Round>builder()
                .put("A X", new Round(Hand.Rock, Hand.Scissors, 3 + 0))
                .put("A Y", new Round(Hand.Rock, Hand.Rock, 1 + 3))
                .put("A Z", new Round(Hand.Rock, Hand.Paper, 2 + 6))
                .put("B X", new Round(Hand.Paper, Hand.Rock, 1 + 0))
                .put("B Y", new Round(Hand.Paper, Hand.Paper, 2 + 3))
                .put("B Z", new Round(Hand.Paper, Hand.Scissors, 3 + 6))
                .put("C X", new Round(Hand.Scissors, Hand.Paper, 2 + 0))
                .put("C Y", new Round(Hand.Scissors, Hand.Scissors, 3 + 3))
                .put("C Z", new Round(Hand.Scissors, Hand.Rock, 1 + 6))
                .build();
        return lines.map(rounds::get).toLongs(Round::score).sum();
    }

    record Round(Hand his, Hand yours, int score) {
    }

    enum Hand {Rock, Paper, Scissors}
}

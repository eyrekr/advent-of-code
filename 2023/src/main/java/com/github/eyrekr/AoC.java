package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AoC<I,S1,S2> {

    public final void run(final int day) {
        try {
            final String input = Files.readString(Path.of(String.format("src/main/resources/%02d.txt", day)));
            final Seq<String> lines = Seq.fromArray(input.split("\n"));
            final I parsedInput = parse(lines);
            final S1 star1 = star1(parsedInput);
            final S2 star2 = star2(parsedInput);
            System.out.println("1) " + star1);
            System.out.println("2) " + star2);
        } catch(final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    abstract I parse(final Seq<String> lines);
    abstract S1 star1(final I input);
    abstract S2 star2(final I input);
}

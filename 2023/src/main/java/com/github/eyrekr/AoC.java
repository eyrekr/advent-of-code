package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

abstract class AoC<IN> {

    void run(final int day, final Function<Seq<String>, IN> parse) {
        try {
            final String input = Files.readString(Path.of(String.format("src/main/resources/%02d.txt", day)));
            final Seq<String> lines = Seq.fromArray(input.split("\n"));
            final IN in = parse.apply(lines);
            System.out.println("1) " + star1(in));
            System.out.println("2) " + star2(in));

        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    abstract long star1(final IN in);

    abstract long star2(final IN in);

}
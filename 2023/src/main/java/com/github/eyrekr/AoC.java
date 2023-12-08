package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

abstract class AoC<IN> {
    IN in;

    void run(final Function<Seq<String>, IN> parse) {
        try {
            final String input = Files.readString(Path.of(String.format("src/main/resources/%s.txt", getClass().getSimpleName())));
            final Seq<String> lines = Seq.fromArray(input.split("\n"));
            this.in = parse.apply(lines);

            print("1) %d\n", star1());
            print("2) %d\n", star2());
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    abstract long star1();

    abstract long star2();

    static void print(final String format, final Object... args) {
        System.out.printf(format, args);
    }

}
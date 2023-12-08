package com.github.eyrekr;

import com.github.eyrekr.util.Seq;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

abstract class AoC<T> {

    void run(final int day, final Function<Seq<String>, T> parse) {
        try {
            final var input = Files.readString(Path.of(String.format("src/main/resources/%02d.txt", day)));
            final var lines = Seq.fromArray(input.split("\n"));
            final var parsed = parse.apply(lines);
            System.out.println("1) " + star1(parsed));
            System.out.println("2) " + star2(parsed));

        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    abstract long star1(final T in);

    abstract long star2(final T in);

}
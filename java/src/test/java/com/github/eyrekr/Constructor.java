package com.github.eyrekr;

@FunctionalInterface
public interface Constructor {
    Aoc aoc(String input);

    Constructor undefined = unused -> {
        throw new IllegalStateException();
    };
}

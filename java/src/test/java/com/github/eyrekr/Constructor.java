package com.github.eyrekr;

@FunctionalInterface
public interface Constructor {
    Aoc aoc(String input);

    static Constructor undefined = unused -> {
        throw new IllegalStateException();
    };
}

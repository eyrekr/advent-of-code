package com.github.eyrekr;

@FunctionalInterface
public interface Constructor {
    Aoc aoc(String input);

    Constructor Undefined = unused -> {
        throw new IllegalStateException();
    };
}

package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D01Test extends AocTest {

    static final String sample = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    D01Test() {
        super(D01::new,
                new Star(sample, 11L, 1834060L),
                new Star(sample, 31L, 21607792L));
    }
}

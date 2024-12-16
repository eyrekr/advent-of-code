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
        super(builderFor(D01Test.class)
                .constructor(D01::new)
                .sampleInput(sample)
                .star1(11L, 1834060L)
                .star2(31L, 21607792L));
    }
}

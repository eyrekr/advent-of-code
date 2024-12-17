package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D04Test extends AocTest {

    static final String sample = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """;

    D04Test() {
        super(builderFor(D04Test.class)
                .constructor(D04::new)
                .sampleInput(sample)
                .star1(18L, 2573L));
    }
}

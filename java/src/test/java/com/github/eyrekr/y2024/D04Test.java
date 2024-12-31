package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D04Test extends AocTest {

    static final String sample1 = """
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
    static final String sample2 = """
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
            """;

    D04Test() {
        super(D04::new,
                new Star(sample1, 18L, 2573L),
                new Star(sample2, 9L, 1850L));
    }
}

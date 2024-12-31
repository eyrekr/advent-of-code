package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D04Test extends AocTest {

    D04Test() {
        constructor(D04::new);
        star1("""
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
                        """,
                18L, 2573L);
        star2("""
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
                        """,
                9L, 1850L);
    }
}

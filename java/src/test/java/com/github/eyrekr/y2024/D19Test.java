package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D19Test extends AocTest {

    D19Test() {
        constructor(D19::new);
        sample("""
                r, wr, b, g, bwu, rb, gb, br
                                                                                      
                brwrr
                bggr
                gbbr
                rrbgbr
                ubwu
                bwurrg
                brgr
                bbrgwb
                """);

        star1(6L, -1L);
        star2(-1L, -1L);
    }

}

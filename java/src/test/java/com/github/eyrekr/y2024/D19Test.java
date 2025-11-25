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

        star1(6L, 300L);
        star2(16L, 624802218898092L);
    }

}

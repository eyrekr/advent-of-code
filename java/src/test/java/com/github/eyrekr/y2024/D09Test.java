package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D09Test extends AocTest {

    D09Test() {
        super(builderFor(D09Test.class)
                .constructor(D09::new)
                .sampleInput("2333133121414131402")
                .star1(1928L, 6344673854800L)
                .star2(2858L, -1L));
    }
}

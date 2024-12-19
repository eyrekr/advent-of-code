package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D08Test extends AocTest {

    D08Test() {
        super(builderFor(D08Test.class)
                .constructor(D08::new)
                .sampleInput("")
                .star1(-1L, -1L)
                .star2(-1L, -1L));
    }
}

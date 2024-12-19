package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D10Test extends AocTest {

    D10Test() {
        super(builderFor(D10Test.class)
                .constructor(D10::new)
                .sampleInput("")
                .star1(-1L, -1L)
                .star2(-1L, -1L));
    }
}

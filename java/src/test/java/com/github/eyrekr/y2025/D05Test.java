package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D05Test extends AocTest {

    D05Test() {
        constructor(D05::new);
        sample("""
                3-5
                10-14
                16-20
                12-18
                                
                1
                5
                8
                11
                17
                32
                """);
        star1(3L, 868L);
        star2(14L, 354143734113772L);
    }
}

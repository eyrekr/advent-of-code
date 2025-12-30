package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D09Test extends AocTest {

    D09Test() {
        constructor(D09::new);
        sample("""
                7,1
                11,1
                11,7
                9,7
                9,5
                2,5
                2,3
                7,3
                """);
        star1(50L, -1L);
        star2(-1L, -1L);
    }
}

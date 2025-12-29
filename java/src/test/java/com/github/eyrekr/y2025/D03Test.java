package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D03Test extends AocTest {

    D03Test() {
        constructor(D03::new);
        sample("""
                987654321111111
                811111111111119
                234234234234278
                818181911112111
                """);
        star1(357L, 17430L);
        star2(-1L, -1L);
    }
}

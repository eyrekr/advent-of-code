package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D01Test extends AocTest {

    D01Test() {
        constructor(com.github.eyrekr.y2025.D01::new);
        sample("""
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82
                """);
        star1(3L, 1011L);
        star2(-1L, -1L);
    }
}

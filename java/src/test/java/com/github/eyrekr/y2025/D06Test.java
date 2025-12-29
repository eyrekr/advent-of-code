package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D06Test extends AocTest {

    D06Test() {
        constructor(D06::new);
        sample("""
                123 328  51 64
                 45 64  387 23
                  6 98  215 314
                *   +   *   +
                """);
        star1(4277556L, 4449991244405L);
        star2(-1L, -1L);
    }
}

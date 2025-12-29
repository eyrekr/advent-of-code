package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D04Test extends AocTest {

    D04Test() {
        constructor(D04::new);
        sample("""
                ..@@.@@@@.
                @@@.@.@.@@
                @@@@@.@.@@
                @.@@@@..@.
                @@.@@@@.@@
                .@@@@@@@.@
                .@.@.@.@@@
                @.@@@.@@@@
                .@@@@@@@@.
                @.@.@@@.@.
                """);
        star1(13L, 1367L);
        star2(43L, 9144L);
    }
}

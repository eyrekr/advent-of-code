package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D08Test extends AocTest {

    static final String sample = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """;

    D08Test() {
        super(builderFor(D08Test.class)
                .constructor(D08::new)
                .sampleInput(sample)
                .star1(14L, 376L)
                .star2(34L, 1352L));
    }
}

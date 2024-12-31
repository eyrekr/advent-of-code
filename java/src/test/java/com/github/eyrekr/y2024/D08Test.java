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
        super(D08::new,
                new Star(sample, 14L, 376L),
                new Star(sample, 34L, 1352L));
    }
}

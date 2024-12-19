package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D06Test extends AocTest {

    static final String sample = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """;

    D06Test() {
        super(builderFor(D06Test.class)
                .constructor(D06::new)
                .sampleInput(sample)
                .star1(41L, 5444L)
                .star2(-1L, -1L));
    }
}

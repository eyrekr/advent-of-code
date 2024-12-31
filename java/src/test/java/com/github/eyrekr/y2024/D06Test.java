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
        super(D06::new,
                new Star(sample, 41L, 5444L),
                new Star(sample, 6L, 1946L));
    }
}

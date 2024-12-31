package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D06Test extends AocTest {

    D06Test() {
        constructor(D06::new);
        sample("""
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
                """);
        star1(41L, 5444L);
        star2(6L, 1946L);
    }
}

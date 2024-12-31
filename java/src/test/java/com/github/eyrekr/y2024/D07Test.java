package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D07Test extends AocTest {

    static final String sample = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;

    D07Test() {
        super(D07::new,
                new Star(sample, 3749L, 20281182715321L),
                new Star(sample, 11387L, 159490400628354L));
    }
}

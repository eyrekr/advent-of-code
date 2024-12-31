package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D02Test extends AocTest {

    static final String sample = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;

    D02Test() {
        super(D02::new,
                new Star(sample, 2L, 483L),
                new Star(sample, 4L, 528L));
    }
}

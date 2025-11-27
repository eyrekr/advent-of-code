package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D18Test extends AocTest {

    D18Test() {
        constructor(D18::new);

        star1("""
                        5,4
                        4,2
                        4,5
                        3,0
                        2,1
                        6,3
                        2,4
                        1,5
                        0,6
                        3,3
                        2,6
                        5,1
                        """,
                22L,
                324L);
        star2("""
                        5,4
                        4,2
                        4,5
                        3,0
                        2,1
                        6,3
                        2,4
                        1,5
                        0,6
                        3,3
                        2,6
                        5,1
                        1,2
                        5,5
                        2,5
                        6,5
                        1,4
                        0,4
                        6,4
                        1,1
                        6,1
                        1,0
                        0,5
                        1,6
                        2,0
                        """,
                21L,
                2987L);
    }

}

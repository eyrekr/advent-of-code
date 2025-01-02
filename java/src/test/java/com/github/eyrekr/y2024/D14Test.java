package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D14Test extends AocTest {

    D14Test() {
        constructor(D14::new);
        star1("""
                        p=0,4 v=3,-3
                        p=6,3 v=-1,-3
                        p=10,3 v=-1,2
                        p=2,0 v=2,-1
                        p=0,0 v=1,3
                        p=3,0 v=-2,-2
                        p=7,6 v=-1,-3
                        p=3,0 v=-1,-2
                        p=9,3 v=2,3
                        p=7,3 v=-1,2
                        p=2,4 v=2,-3
                        p=9,5 v=-3,-3
                        """,
                21L, 231782040L);
        star2(null, 0L, 0L);
    }
}

package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D11Test extends AocTest {

    D11Test() {
        constructor(D11::new);
        star1("""
                        aaa: you hhh
                        you: bbb ccc
                        bbb: ddd eee
                        ccc: ddd eee fff
                        ddd: ggg
                        eee: out
                        fff: out
                        ggg: out
                        hhh: ccc fff iii
                        iii: out
                        """,
                5L,
                733L);
        star2("""
                        svr: aaa bbb
                        aaa: fft
                        fft: ccc
                        bbb: tty
                        tty: ccc
                        ccc: ddd eee
                        ddd: hub
                        hub: fff
                        eee: dac
                        dac: fff
                        fff: ggg hhh
                        ggg: out
                        hhh: out
                        """,
                -1L,
                -1L);
    }
}

package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D02Test extends AocTest {

    D02Test() {
        constructor(D02::new);
        sample("""
                11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
                """);
        star1(1227775554L, 23560874270L);
        star2(4174379265L, -1L);
    }
}

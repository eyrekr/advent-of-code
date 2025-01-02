package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D12Test extends AocTest {

    D12Test() {
        constructor(D12::new);
        sample("""
                RRRRIICCFF
                RRRRIICCCF
                VVRRRCCFFF
                VVRCCCJFFF
                VVVVCJJCFE
                VVIVCCJJEE
                VVIIICJJEE
                MIIIIIJJEE
                MIIISIJEEE
                MMMISSJEEE
                """);
        star1(1930L, 1465112L);
        star2(1206L, 893790L);
    }
}

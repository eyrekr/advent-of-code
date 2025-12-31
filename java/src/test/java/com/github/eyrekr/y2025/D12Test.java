package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;

class D12Test extends AocTest {

    D12Test() {
        constructor(D12::new);
        sample("""
                0:
                ###
                ##.
                ##.
                
                1:
                ###
                ##.
                .##
                
                2:
                .##
                ###
                ##.
                
                3:
                ##.
                ###
                ##.
                
                4:
                ###
                #..
                ###
                
                5:
                ###
                .#.
                ###
                
                4x4: 0 0 0 0 2 0
                12x5: 1 0 1 0 2 2
                12x5: 1 0 1 0 3 2
                """);
        star1(2L, -1L);
        star2(-1L, -1L);
    }

}

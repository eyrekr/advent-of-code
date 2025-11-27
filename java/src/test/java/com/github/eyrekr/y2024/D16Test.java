package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D16Test extends AocTest {

    D16Test() {
        constructor(D16::new);
        sample("""
                ###############
                #.......#....E#
                #.#.###.#.###.#
                #.....#.#...#.#
                #.###.#####.#.#
                #.#.#.......#.#
                #.#.#####.###.#
                #...........#.#
                ###.#.#####.#.#
                #...#.....#.#.#
                #.#.#.###.#.#.#
                #.....#...#.#.#
                #.###.#.#.#.#.#
                #S..#.....#...#
                ###############
                """);

        star1(-1L, -1L);
        star2(-1L, -1L);
    }
}

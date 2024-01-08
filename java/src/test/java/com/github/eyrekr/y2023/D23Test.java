package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D23Test {
    final String sample = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """;
    final String input = Out.testResource("2023/D23.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D23(sample).star1()).isEqualTo(94L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D23(sample).star2()).isEqualTo(154L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D23(input).star1()).isEqualTo(2070L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D23(input).star2()).isEqualTo(6498L);
    }

}

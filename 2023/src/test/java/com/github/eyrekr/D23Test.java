package com.github.eyrekr;

import com.github.eyrekr.util.Str;
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
    final String input = Str.testResource("D23.txt");

    @Test
    void sampleStar1() {
        assertThat(new D23(sample).star1()).isEqualTo(94L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D23(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D23(input).star1()).isEqualTo(0L);
    }

    @Test
    void star2() {
        assertThat(new D23(input).star2()).isEqualTo(0L);
    }

}

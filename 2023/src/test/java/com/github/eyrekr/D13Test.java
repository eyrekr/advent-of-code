package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D13Test {
    final String sample = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                        
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """;
    final String input = Out.testResource("D13.txt");

    @Test
    void sampleStar1() {
        assertThat(new D13(sample).star1()).isEqualTo(405L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D13(sample).star2()).isEqualTo(400L);
    }

    @Test
    void star1() {
        assertThat(new D13(input).star1()).isEqualTo(30535L);
    }

    @Test
    void star2() {
        assertThat(new D13(input).star2()).isEqualTo(30844L);
    }

}

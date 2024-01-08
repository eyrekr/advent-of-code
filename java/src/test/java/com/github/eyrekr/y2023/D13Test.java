package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
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
    final String input = Out.testResource("2023/D13.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D13(sample).star1()).isEqualTo(405L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D13(sample).star2()).isEqualTo(400L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D13(input).star1()).isEqualTo(30535L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D13(input).star2()).isEqualTo(30844L);
    }

}

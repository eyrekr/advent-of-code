package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D18Test {
    final String sample = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """;

    final String input = Out.testResource("D18.txt");

    @Test
    void sampleStar1() {
        assertThat(new D18(sample).star1()).isEqualTo(62L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D18(sample).star2()).isEqualTo(952_408_144_115L);
    }

    @Test
    void star1() {
        assertThat(new D18(input).star1()).isEqualTo(28911L);
    }

    @Test
    void star2() {
        assertThat(new D18(input).star2()).isEqualTo(77366737561114L);
    }

}

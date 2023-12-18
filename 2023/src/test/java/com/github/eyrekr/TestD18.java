package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD18 {
    final String SAMPLE = """
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

    @Test
    void sampleStar1() {
        assertThat(new D18(SAMPLE).star1()).isEqualTo(62L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D18(SAMPLE).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D18(Str.testResource("D18.txt")).star1()).isEqualTo(28911L);
    }

    @Test
    void star2() {
        assertThat(new D18(Str.testResource("D18.txt")).star2()).isEqualTo(0L);
    }

}

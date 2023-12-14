package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD14 {
    final String SAMPLE = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;

    @Test
    void sampleStar1() {
        assertThat(new D14(SAMPLE).star1()).isEqualTo(136L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D14(SAMPLE).star2()).isEqualTo(64L);
    }

    @Test
    void star1() {
        assertThat(new D14(Str.testResource("D14.txt")).star1()).isEqualTo(106186L);
    }

    @Test
    void star2() {
        assertThat(new D14(Str.testResource("D14.txt")).star2()).isEqualTo(0L);
    }

}

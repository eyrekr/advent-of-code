package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D21Test {
    final String sample = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """;
    final String input = Out.testResource("D21.txt");

    @Test
    void sampleStar1() {
        assertThat(new D21(sample, 6L).star1()).isEqualTo(16L);
    }

    @Test
    void star1() {
        assertThat(new D21(input, 64L).star1()).isEqualTo(3598L);
    }

    @Test
    void star2() {
        assertThat(new D21(input, 26501365L).star2()).isEqualTo(601441063166538L);
    }

}

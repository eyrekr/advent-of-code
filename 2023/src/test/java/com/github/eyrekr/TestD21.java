package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD21 {
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
    final String input = Str.testResource("D21.txt");

    @Test
    void sampleStar1() {
        assertThat(new D21(sample).star1(6)).isEqualTo(16L);
    }

    @Test
    void sampleStar2_After6Steps() {
        assertThat(new D21(sample).star2(6)).isEqualTo(16L);
    }

    @Test
    void sampleStar2_After16Steps() {
        assertThat(new D21(sample).star2(16)).isEqualTo(50L);
    }

    @Test
    void sampleStar2_After50Steps() {
        assertThat(new D21(sample).star2(50)).isEqualTo(1594L);
    }

    @Test
    void sampleStar2_After100Steps() {
        assertThat(new D21(sample).star2(100)).isEqualTo(6536L);
    }

    @Test
    void sampleStar2_After500Steps() {
        assertThat(new D21(sample).star2(500)).isEqualTo(167004L);
    }

    @Test
    void sampleStar2_After1000Steps() {
        assertThat(new D21(sample).star2(1000)).isEqualTo(668697L);
    }

    @Test
    void sampleStar2_After5000Steps() {
        assertThat(new D21(sample).star2(5000)).isEqualTo(16733044L);
    }

    @Test
    void star1() {
        assertThat(new D21(input).star1(64)).isEqualTo(3598L);
    }

    @Test
    void star2() {
        assertThat(new D21(input).star2(26501365)).isEqualTo(0L);
    }

}

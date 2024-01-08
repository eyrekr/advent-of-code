package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D24Test {
    final String sample = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
            """;
    final String input = Out.testResource("2023/D24.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D24(sample, 7, 27).star1()).isEqualTo(2L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D24(sample, 0, 0).star2()).isEqualTo(920630818300104L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D24(input, 200_000_000_000_000L, 400_000_000_000_000L).star1()).isEqualTo(11098L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D24(input, 0, 0).star2()).isEqualTo(920630818300104L);
    }

}

package com.github.eyrekr.y2024;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D01Test {

    final String sample = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    final String input = Out.testResource("2024/D01.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D01(sample).star1()).isEqualTo(11L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D01(sample).star2()).isEqualTo(31L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D01(input).star1()).isEqualTo(1834060L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D01(input).star2()).isEqualTo(21607792L);
    }
}

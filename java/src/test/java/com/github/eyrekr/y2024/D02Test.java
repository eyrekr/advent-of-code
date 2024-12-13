package com.github.eyrekr.y2024;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D02Test {

    final String sample = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    final String input = Out.testResource("2024/D02.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D02(sample).star1()).isEqualTo(-1L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D02(sample).star2()).isEqualTo(-1L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D02(input).star1()).isEqualTo(-1);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D02(input).star2()).isEqualTo(-1L);
    }
}

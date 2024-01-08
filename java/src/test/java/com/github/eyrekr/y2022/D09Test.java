package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D09Test {

    final String sample = """
            """;

    final String input = Out.testResource("2022/D09.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D09(sample).star1()).isEqualTo(0L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D09(input).star1()).isEqualTo(0L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D09(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D09(input).star2()).isEqualTo(0L);
    }
}

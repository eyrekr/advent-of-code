package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D02Test {

    final String sample = """
            A Y
            B X
            C Z
            """;
    final String input = Out.testResource("2022/D02.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D02(sample).star1()).isEqualTo(15L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D02(sample).star2()).isEqualTo(12L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D02(input).star1()).isEqualTo(10994L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D02(input).star2()).isEqualTo(12526L);
    }
}

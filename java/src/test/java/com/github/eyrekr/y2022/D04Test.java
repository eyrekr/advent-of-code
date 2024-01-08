package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D04Test {

    final String sample = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
            """;
    final String input = Out.testResource("2022/D04.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D04(sample).star1()).isEqualTo(2L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D04(input).star1()).isEqualTo(305L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D04(sample).star2()).isEqualTo(4L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D04(input).star2()).isEqualTo(811L);
    }
}

package com.github.eyrekr.y2024;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D02Test {

    final String sample = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;

    final String input = Out.testResource("2024/D02.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D02(sample).star1()).isEqualTo(2L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D02(input).star1()).isEqualTo(483L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D02(sample).star2()).isEqualTo(4L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D02(input).star2()).isEqualTo(-1L);
    }
}

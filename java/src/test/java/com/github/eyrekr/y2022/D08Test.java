package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D08Test {

    final String sample = """
            """;

    final String input = Out.testResource("2022/D08.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D08(sample).star1()).isEqualTo(0L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D08(input).star1()).isEqualTo(0L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D08(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D08(input).star2()).isEqualTo(0L);
    }
}

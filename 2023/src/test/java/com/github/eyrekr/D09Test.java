package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D09Test {
    final String sample = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;
    final String input = Out.testResource("D09.txt");

    @Test
    void sampleStar1() {
        assertThat(new D09(sample).star1()).isEqualTo(114L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D09(sample).star2()).isEqualTo(2L);
    }

    @Test
    void star1() {
        Out.print("%d\n", new D09(input).star1());
    }

    @Test
    void star2() {
        Out.print("%d\n", new D09(input).star2());
    }
}

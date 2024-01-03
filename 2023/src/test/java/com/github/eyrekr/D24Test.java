package com.github.eyrekr;

import com.github.eyrekr.output.Out;
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
    final String input = Out.testResource("D24.txt");

    @Test
    void sampleStar1() {
        assertThat(new D24(sample).star1()).isEqualTo(2L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D24(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D24(input).star1()).isEqualTo(0L);
    }

    @Test
    void star2() {
        assertThat(new D24(input).star2()).isEqualTo(0L);
    }

}

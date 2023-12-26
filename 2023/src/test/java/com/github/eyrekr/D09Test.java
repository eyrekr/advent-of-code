package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D09Test {
    final String SAMPLE = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    @Test
    void sampleStar1() {
        assertThat(new D09(SAMPLE).star1()).isEqualTo(114L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D09(SAMPLE).star2()).isEqualTo(2L);
    }

    @Test
    void star1() {
        Str.print("%d\n", new D09(Str.testResource("D09.txt")).star1());
    }

    @Test
    void star2() {
        Str.print("%d\n", new D09(Str.testResource("D09.txt")).star2());
    }
}

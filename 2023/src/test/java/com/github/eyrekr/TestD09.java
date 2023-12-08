package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD09 {
    final String SAMPLE = """
                        
            """;

    @Test
    void sampleStar1() {
        assertThat(new D09(SAMPLE).star1()).isEqualTo(0L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D09(SAMPLE).star2()).isEqualTo(0L);
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

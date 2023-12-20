package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD20 {
    final String SAMPLE_A = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """;
    final String SAMPLE_B = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
            """;

    @Test
    void sampleStar1A() {
        assertThat(new D20(SAMPLE_A).star1()).isEqualTo(32_000_000L);
    }

    @Test
    void sampleStar1B() {
        assertThat(new D20(SAMPLE_B).star1()).isEqualTo(11_687_500L);
    }

    @Test
    void star1() {
        assertThat(new D20(Str.testResource("D20.txt")).star1()).isEqualTo(899848294L);
    }

    @Test
    void star2() {
        assertThat(new D20(Str.testResource("D20.txt")).star2()).isEqualTo(247454898168563L);
    }

}

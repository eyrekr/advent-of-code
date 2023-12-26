package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD20 {
    final String sampleA = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """;
    final String sampleB = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
            """;

    final String input = Str.testResource("D20.txt");

    @Test
    void sampleStar1A() {
        assertThat(new D20(sampleA).star1()).isEqualTo(32_000_000L);
    }

    @Test
    void sampleStar1B() {
        assertThat(new D20(sampleB).star1()).isEqualTo(11_687_500L);
    }

    @Test
    void star1() {
        assertThat(new D20(input).star1()).isEqualTo(899848294L);
    }

    @Test
    void star2() {
        assertThat(new D20(input).star2()).isEqualTo(247454898168563L);
    }

}

package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D09Test {

    final String sample1 = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
            """;
    final String sample2 = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
            """;

    final String input = Out.testResource("2022/D09.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D09(sample1).star1()).isEqualTo(13L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D09(input).star1()).isEqualTo(6087L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D09(sample2).star2()).isEqualTo(36L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D09(input).star2()).isEqualTo(2493L);
    }
}

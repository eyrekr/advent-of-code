package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D03Test {

    final String sample = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
            """;
    final String input = Out.testResource("2022/D03.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D03(sample).star1()).isEqualTo(157L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D03(input).star1()).isEqualTo(8105L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D03(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D03(input).star2()).isEqualTo(0L);
    }
}

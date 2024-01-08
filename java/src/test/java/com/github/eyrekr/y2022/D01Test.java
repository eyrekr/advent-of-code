package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D01Test {

    final String sample = """
            1000
            2000
            3000
                        
            4000
                        
            5000
            6000
                        
            7000
            8000
            9000
                        
            10000
            """;
    final String input = Out.testResource("2022/D01.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D01(sample).star1()).isEqualTo(24000L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D01(input).star1()).isEqualTo(69528L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D01(sample).star2()).isEqualTo(45000L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D01(input).star2()).isEqualTo(206152L);
    }
}

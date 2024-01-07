package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D08Test {
    final String sample1 = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;
    final String sample2 = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """;
    final String input = Out.testResource("D08.txt");

    @Test
    void sampleStar1() {
        assertThat(new D08(sample1).star1()).isEqualTo(2L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D08(sample2).star2()).isEqualTo(6L);
    }

    @Test
    void star1() {
        assertThat(new D08(input).star1()).isEqualTo(19241L);
    }

    @Test
    void star2() {
        assertThat(new D08(input).star2()).isEqualTo(9606140307013L);
    }
}

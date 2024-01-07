package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D07Test {
    final String sample = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;
    final String input = Out.testResource("D07.txt");


    @Test
    void sampleStar1() {
        assertThat(new D07(sample).star1()).isEqualTo(6440L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D07(sample).star2()).isEqualTo(5905L);
    }

    @Test
    void star1() {
        assertThat(new D07(input).star1()).isEqualTo(248559379L);
    }

    @Test
    void star2() {
        assertThat(new D07(input).star2()).isEqualTo(249631254L);
    }
}

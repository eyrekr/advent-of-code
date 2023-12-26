package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D01Test {

    final String sample1 = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;
    final String sample2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;

    final String input = Str.testResource("D01.txt");

    @Test
    void sampleStar1() {
        assertThat(new D01(sample1).star1()).isEqualTo(142L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D01(sample2).star2()).isEqualTo(281L);
    }

    @Test
    void star1() {
        assertThat(new D01(input).star1()).isEqualTo(54331L);
    }

    @Test
    void star2() {
        assertThat(new D01(input).star2()).isEqualTo(54518L);
    }
}

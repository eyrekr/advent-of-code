package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D03Test {

    final String sample = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;

    final String input = Str.testResource("D03.txt");

    @Test
    void sampleStar1() {
        assertThat(new D03(sample).star1()).isEqualTo(4361L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D03(sample).star2()).isEqualTo(467835L);
    }

    @Test
    void star1() {
        assertThat(new D03(input).star1()).isEqualTo(514969L);
    }

    @Test
    void star2() {
        assertThat(new D03(input).star2()).isEqualTo(78915902L);
    }
}

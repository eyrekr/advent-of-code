package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D22Test {
    final String sample = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;
    final String input = Str.testResource("D22.txt");

    @Test
    void sampleStar1() {
        assertThat(new D22(sample).star1()).isEqualTo(5L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D22(sample).star2()).isEqualTo(7L);
    }

    @Test
    void star1() {
        assertThat(new D22(input).star1()).isEqualTo(418L);
    }

    @Test
    void star2() {
        assertThat(new D22(input).star2()).isEqualTo(70702L);
    }

}

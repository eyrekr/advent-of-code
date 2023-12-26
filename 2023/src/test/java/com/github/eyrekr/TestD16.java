package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD16 {
    final String sample = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """;
    final String input = Str.testResource("D16.txt");

    @Test
    void sampleStar1() {
        assertThat(new D16(sample).star1()).isEqualTo(46L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D16(sample).star2()).isEqualTo(51L);
    }

    @Test
    void star1() {
        assertThat(new D16(input).star1()).isEqualTo(7517L);
    }

    @Test
    void star2() {
        assertThat(new D16(input).star2()).isEqualTo(7741L);
    }

}

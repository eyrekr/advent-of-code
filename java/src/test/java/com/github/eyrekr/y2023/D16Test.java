package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.y2023.D16;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D16Test {
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
    final String input = Out.testResource("D16.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D16(sample).star1()).isEqualTo(46L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D16(sample).star2()).isEqualTo(51L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D16(input).star1()).isEqualTo(7517L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D16(input).star2()).isEqualTo(7741L);
    }

}

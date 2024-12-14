package com.github.eyrekr.y2024;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D03Test {

    final String sample = """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """;

    final String input = Out.testResource("2024/D03.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D03(sample).star1()).isEqualTo(161L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D03(input).star1()).isEqualTo(174336360L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D03(sample).star2()).isEqualTo(-1L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D03(input).star2()).isEqualTo(-1L);
    }
}

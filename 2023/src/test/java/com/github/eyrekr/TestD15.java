package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD15 {
    final String SAMPLE = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    @Test
    void sampleStar1() {
        assertThat(new D15(SAMPLE).star1()).isEqualTo(1320L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D15(SAMPLE).star2()).isEqualTo(145L);
    }

    @Test
    void star1() {
        assertThat(new D15(Str.testResource("D15.txt")).star1()).isEqualTo(515495L);
    }

    @Test
    void star2() {
        assertThat(new D15(Str.testResource("D15.txt")).star2()).isEqualTo(229349L);
    }

}

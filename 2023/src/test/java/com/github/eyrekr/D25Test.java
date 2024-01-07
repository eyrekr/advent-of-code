package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D25Test {
    final String sample = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
            """;
    final String input = Out.testResource("D25.txt");

    @Test
    void sampleStar1() {
        assertThat(new D25(sample).star1()).isEqualTo(54L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D25(sample).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D25(input).star1()).isEqualTo(0L);
    }

    @Test
    void star2() {
        assertThat(new D25(input).star2()).isEqualTo(0L);
    }

}

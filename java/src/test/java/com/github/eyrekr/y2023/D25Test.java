package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.y2023.D25;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(new D25(sample).star1()).isEqualTo(54L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D25(input).star1()).isEqualTo(613870L);
    }

}

package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD19 {
    final String SAMPLE = """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
            
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """;

    @Test
    void sampleStar1() {
        assertThat(new D19(SAMPLE).star1()).isEqualTo(19_114L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D19(SAMPLE).star2()).isEqualTo(167409079868000L);
    }

    @Test
    void star1() {
        assertThat(new D19(Str.testResource("D19.txt")).star1()).isEqualTo(350_678L);
    }

    @Test
    void star2() {
        assertThat(new D19(Str.testResource("D19.txt")).star2()).isEqualTo(124831893423809L);
    }

}
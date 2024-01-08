package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.y2023.D19;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D19Test {
    final String sample = """
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
    final String input = Out.testResource("D19.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D19(sample).star1()).isEqualTo(19_114L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D19(sample).star2()).isEqualTo(167409079868000L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D19(input).star1()).isEqualTo(350_678L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D19(input).star2()).isEqualTo(124831893423809L);
    }

}

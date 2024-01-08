package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class D06Test {

    final String input = Out.testResource("2022/D06.txt");

    @ParameterizedTest
    @MethodSource("dataForSampleStar1")
    void sampleStar1(final String line, final long expected) {
        Assertions.assertThat(new D06(line).star1()).isEqualTo(expected);
    }

    static Stream<Arguments> dataForSampleStar1() {
        return Stream.of(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7L),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5L),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6L),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10L),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11L));
    }

    @Test
    void star1() {
        Assertions.assertThat(new D06(input).star1()).isEqualTo(0L);
    }

//    @Test
//    void sampleStar2() {
//        Assertions.assertThat(new D06(sample).star2()).isEqualTo(0L);
//    }

    @Test
    void star2() {
        Assertions.assertThat(new D06(input).star2()).isEqualTo(0L);
    }
}

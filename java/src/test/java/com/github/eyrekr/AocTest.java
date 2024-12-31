package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AocTest {

    private final Pattern pattern = Pattern.compile("y(\\d{4})\\.D(\\d{2})");

    final String year;
    final String day;
    final String input;

    private Constructor constructor = Constructor.Undefined;
    private Star star1 = new Star();
    private Star star2 = new Star();

    protected AocTest() {
        final var matcher = pattern.matcher(getClass().getName());
        if (matcher.find()) {
            year = matcher.group(1);
            day = matcher.group(2);
            input = Out.testResource(String.format("%s/D%s.txt", year, day));
        } else throw new IllegalStateException(getClass().getName());
    }

    protected AocTest constructor(final Constructor constructor) {
        this.constructor = constructor;
        return this;
    }

    protected AocTest sample(final String sampleForBothStars) {
        star1.sample = sampleForBothStars;
        star2.sample = sampleForBothStars;
        return this;
    }

    protected AocTest star1(final String sample, final long sampleAnswer, final long answer) {
        star1.sample = sample;
        star1.sampleAnswer = sampleAnswer;
        star1.answer = answer;
        return this;
    }

    protected AocTest star1(final long sampleAnswer, final long answer) {
        star1.sampleAnswer = sampleAnswer;
        star1.answer = answer;
        return this;
    }

    protected AocTest star2(final String sample, final long sampleAnswer, final long answer) {
        star2.sample = sample;
        star2.sampleAnswer = sampleAnswer;
        star2.answer = answer;
        return this;
    }

    protected AocTest star2(final long sampleAnswer, final long answer) {
        star2.sampleAnswer = sampleAnswer;
        star2.answer = answer;
        return this;
    }

    @Test
    @Order(1)
    void star1() {
        final long sampleAnswer = constructor.aoc(star1.sample).star1();

        final long t0 = System.nanoTime();
        final long answer = constructor.aoc(input).star1();
        final String duration = format(System.nanoTime() - t0);

        Out.print("%s Star 1  sample:%d  answer:%d   %s\n", day, sampleAnswer, answer, duration);

        assertThat(sampleAnswer).isEqualTo(star1.sampleAnswer);
        assertThat(answer).isEqualTo(star1.answer);
    }

    @Test
    @Order(2)
    void star2() {
        final long sampleAnswer = constructor.aoc(star2.sample).star2();

        final long t0 = System.nanoTime();
        final long answer = constructor.aoc(input).star2();
        final String duration = format(System.nanoTime() - t0);

        Out.print("%s Star 2  sample:%d  answer:%d   %s\n", day, sampleAnswer, answer, duration);

        assertThat(sampleAnswer).isEqualTo(star2.sampleAnswer);
        assertThat(answer).isEqualTo(star2.answer);
    }


    private static String format(final long nanos) {
        if (nanos / 1_000 == 0) return nanos + "ns";
        if (nanos / 1_000_000 == 0) return (nanos / 1_000) + "µs";
        if (nanos / 1_000_000_000 == 0) return (nanos / 1_000_000) + "ms";
        return (nanos / 1_000_000_000) + "s";
    }

    private static class Star {
        String sample = "";
        long sampleAnswer = -1L;
        long answer = -1L;
    }
}

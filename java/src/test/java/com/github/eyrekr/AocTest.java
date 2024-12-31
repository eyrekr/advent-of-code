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
    private final Star star1 = new Star();
    private final Star star2 = new Star();

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
        report(star1, sampleAnswer, answer, System.nanoTime() - t0);

        assertThat(sampleAnswer).isEqualTo(star1.sampleAnswer);
        assertThat(answer).isEqualTo(star1.answer);
    }

    @Test
    @Order(2)
    void star2() {
        final long sampleAnswer = constructor.aoc(star2.sample).star2();

        final long t0 = System.nanoTime();
        final long answer = constructor.aoc(input).star2();
        report(star2, sampleAnswer, answer, System.nanoTime() - t0);

        assertThat(sampleAnswer).isEqualTo(star2.sampleAnswer);
        assertThat(answer).isEqualTo(star2.answer);
    }

    private void report(final Star star, final long sampleAnswer, final long answer, final long duration) {
        final boolean sampleAnswerCorrect = star.sampleAnswer == sampleAnswer;
        final boolean answerCorrect = star.answer == answer;
        Out.print("""
                %-3s  %s%s  %s%16d  %s%16d  @c%8s@@
                """,
                day,
                sampleAnswerCorrect && answerCorrect ? "@Y" : "@K",
                star == star1 ? "★ " : "★★",
                sampleAnswerCorrect ? "@G" : "@R",
                sampleAnswer,
                answerCorrect ? "@G" : "@R",
                answer,
                format(duration));
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

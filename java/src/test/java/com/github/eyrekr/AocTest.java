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

    private static final Pattern pattern = Pattern.compile("y(\\d{4})\\.D(\\d{2})");

    final Star[] stars;
    final Constructor constructor;
    final String input;

    protected AocTest(final Constructor constructor, final Star... stars) {
        this.constructor = constructor;
        this.stars = stars;
        final var matcher = pattern.matcher(getClass().getName());
        if (matcher.find())
            this.input = Out.testResource(String.format("%s/D%s.txt", matcher.group(1), matcher.group(2)));
        else throw new IllegalStateException(getClass().getName());
    }

    @Test
    @Order(1)
    void star1() {
        if (stars.length > 0) {
            final long testOutput = constructor.aoc(stars[0].testInput).star1();

            final long t0 = System.nanoTime();
            final long output = constructor.aoc(input).star1();
            final String duration = format(System.nanoTime() - t0);

            Out.print("%s Star 1  test:%d  answer:%d   %s\n", getClass().getSimpleName().substring(0,3), testOutput, output, duration);

            assertThat(testOutput).isEqualTo(stars[0].testOutput);
            assertThat(output).isEqualTo(stars[0].output);
        }
    }

    @Test
    @Order(2)
    void star2() {
        if (stars.length > 1) {
            final long testOutput = constructor.aoc(stars[1].testInput).star2();

            final long t0 = System.nanoTime();
            final long output = constructor.aoc(input).star2();
            final String duration = format(System.nanoTime() - t0);

            Out.print("%s Star 2  test:%d  answer:%d   %s\n", getClass().getSimpleName().substring(0,3), testOutput, output, duration);

            assertThat(testOutput).isEqualTo(stars[1].testOutput);
            assertThat(output).isEqualTo(stars[1].output);
        }
    }


    private static String format(final long nanos) {
        if (nanos / 1_000 == 0) return nanos + "ns";
        if (nanos / 1_000_000 == 0) return (nanos / 1_000) + "µs";
        if (nanos / 1_000_000_000 == 0) return (nanos / 1_000_000) + "ms";
        return (nanos / 1_000_000_000) + "s";
    }

    public record Star(String testInput, long testOutput, long output) {
    }
}

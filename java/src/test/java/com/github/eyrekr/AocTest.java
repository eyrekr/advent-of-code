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

    final Constructor constructor;
    final Star star1, star2;
    final String input;

    protected AocTest(final Constructor constructor, final Star star1, final Star star2) {
        this.constructor = constructor;
        this.star1 = star1;
        this.star2 = star2;
        final var matcher = pattern.matcher(getClass().getName());
        if (matcher.find())
            this.input = Out.testResource(String.format("%s/D%s.txt", matcher.group(1), matcher.group(2)));
        else throw new IllegalStateException(getClass().getName());
    }

    protected AocTest(final Constructor constructor, final String testInput, final Star star1, final Star star2) {
        this(
                constructor,
                star1.testInput == null ? new Star(testInput, star1.testOutput, star1.output) : star1,
                star2.testInput == null ? new Star(testInput, star2.testOutput, star2.output) : star2);
    }

    @Test
    @Order(1)
    void star1() {
        final long testOutput = constructor.aoc(star1.testInput).star1();

        final long t0 = System.nanoTime();
        final long output = constructor.aoc(input).star1();
        final String duration = format(System.nanoTime() - t0);

        Out.print("%s Star 1  test:%d  answer:%d   %s\n", getClass().getSimpleName().substring(0, 3), testOutput, output, duration);

        assertThat(testOutput).isEqualTo(star1.testOutput);
        assertThat(output).isEqualTo(star1.output);
    }

    @Test
    @Order(2)
    void star2() {
        final long testOutput = constructor.aoc(star2.testInput).star2();

        final long t0 = System.nanoTime();
        final long output = constructor.aoc(input).star2();
        final String duration = format(System.nanoTime() - t0);

        Out.print("%s Star 2  test:%d  answer:%d   %s\n", getClass().getSimpleName().substring(0, 3), testOutput, output, duration);

        assertThat(testOutput).isEqualTo(star2.testOutput);
        assertThat(output).isEqualTo(star2.output);
    }


    private static String format(final long nanos) {
        if (nanos / 1_000 == 0) return nanos + "ns";
        if (nanos / 1_000_000 == 0) return (nanos / 1_000) + "µs";
        if (nanos / 1_000_000_000 == 0) return (nanos / 1_000_000) + "ms";
        return (nanos / 1_000_000_000) + "s";
    }

    public record Star(String testInput, long testOutput, long output) {
        public Star(final long testOutput, final long output) {
            this(null, testOutput, output);
        }

        public Star(final long testOutput) {
            this(null, testOutput, -1L);
        }
    }
}

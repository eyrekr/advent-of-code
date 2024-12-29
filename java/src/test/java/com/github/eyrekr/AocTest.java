package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.function.Function;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AocTest {

    private static final Pattern pattern = Pattern.compile("y(\\d{4})\\.D(\\d{2})");

    final Setup setup;

    protected AocTest(final Setup setup) {
        this.setup = setup;
    }

    protected AocTest(final Builder builder) {
        this.setup = builder.build();
    }

    //region TESTS
    @Test
    @Order(1)
    void star1_sample() {
        test(setup.star1.sample, Aoc::star1);
    }

    @Test
    @Order(2)
    void star1() {
        test(setup.star1.real, Aoc::star1);
    }

    @Test
    @Order(3)
    void star2_sample() {
        test(setup.star2.sample, Aoc::star2);
    }

    @Test
    @Order(4)
    void star2() {
        test(setup.star2.real, Aoc::star2);
    }

    private void test(final Data data, final Function<Aoc, Long> star) {
        final var t0 = System.nanoTime();
        final var aoc = setup.constructor.aoc(data.input);
        final var t1 = System.nanoTime();
        final var output = star.apply(aoc);
        final var t2 = System.nanoTime();
        if (data.output == output) {
            Out.print(
                    """
                            @c%s@@ + @c%s@@ = @c%s@@
                            solution: @g%,d@@
                            """,
                    nanosToHumanReadableFormat(t1 - t0),
                    nanosToHumanReadableFormat(t2 - t1),
                    nanosToHumanReadableFormat(t2 - t0),
                    output);
        } else {
            Out.print(
                    """
                            @c%s@@ + @c%s@@ = @c%s@@
                            solution: @r%,d@@   expected: @g%,d@@
                            """,
                    nanosToHumanReadableFormat(t1 - t0),
                    nanosToHumanReadableFormat(t2 - t1),
                    nanosToHumanReadableFormat(t2 - t0),
                    output,
                    data.output);
        }
        Assertions.assertThat(output).isEqualTo(data.output);
    }
    //endregion


    //region STATIC HELPER METHODS
    private String nanosToHumanReadableFormat(final long nanos) {
        if (nanos / 1_000 == 0) return nanos + "ns";
        if (nanos / 1_000_000 == 0) return (nanos / 1_000) + "µs";
        if (nanos / 1_000_000_000 == 0) return (nanos / 1_000_000) + "ms";
        return (nanos / 1_000_000_000) + "s";
    }

    public static Builder builderFor(final Class<?> tClass) {
        return new Builder(tClass);
    }

    private static String readInputForClass(final Class<?> tClass) {
        final var matcher = pattern.matcher(tClass.getName());
        if (matcher.find()) return Out.testResource(String.format("%s/D%s.txt", matcher.group(1), matcher.group(2)));
        throw new IllegalStateException(tClass.getName());
    }
    //endregion


    //region BUILDER
    protected static class Builder {

        Star[] stars = new Star[]{new Star(), new Star()};
        Star star = null;
        Constructor constructor = Constructor.undefined;

        private Builder(final Class<?> tClass) {
            input(readInputForClass(tClass));
        }

        public Builder all() {
            this.star = null;
            return this;
        }

        public Builder all(final String sampleInput) {
            this.star = null;
            return sampleInput(sampleInput);
        }

        public Builder star1() {
            this.star = stars[0];
            return this;
        }

        public Builder star1(final long sampleOutput, final long solution) {
            return star1().sampleOutput(sampleOutput).solution(solution);
        }

        public Builder star1(final String sampleInput, final long sampleOutput, final long solution) {
            return star1().sampleInput(sampleInput).sampleOutput(sampleOutput).solution(solution);
        }

        public Builder star2() {
            this.star = stars[1];
            return this;
        }

        public Builder star2(final long sampleOutput, final long solution) {
            return star2().sampleOutput(sampleOutput).solution(solution);
        }

        public Builder star2(final String sampleInput, final long sampleOutput, final long solution) {
            return star2().sampleInput(sampleInput).sampleOutput(sampleOutput).solution(solution);
        }

        public Builder sampleInput(final String value) {
            if (star == null) for (final var star : stars) star.sampleInput = value;
            else star.sampleInput = value;
            return this;
        }

        public Builder sampleOutput(final long value) {
            if (star == null) for (final var star : stars) star.sampleOutput = value;
            else star.sampleOutput = value;
            return this;
        }

        public Builder input(final String value) {
            if (star == null) for (final var star : stars) star.input = value;
            else star.input = value;
            return this;
        }

        public Builder output(final long value) {
            if (star == null) for (final var star : stars) star.output = value;
            else star.output = value;
            return this;
        }

        public Builder solution(final long value) {
            return output(value);
        }

        public Builder constructor(final Constructor constructor) {
            this.constructor = constructor;
            return this;
        }

        public Setup build() {
            return new Setup(
                    new AocTest.Star(
                            new Data(stars[0].sampleInput, stars[0].sampleOutput),
                            new Data(stars[0].input, stars[0].output)),
                    new AocTest.Star(
                            new Data(stars[1].sampleInput, stars[1].sampleOutput),
                            new Data(stars[1].input, stars[1].output)),
                    constructor);
        }

        static class Star {
            String sampleInput = "";
            long sampleOutput = -1L;
            String input = "";
            long output = -1L;
        }
    }
    //endregion


    //region SETUP
    record Setup(Star star1, Star star2, Constructor constructor) {
    }

    record Star(Data sample, Data real) {
    }

    record Data(String input, long output) {
    }
    //endregion
}

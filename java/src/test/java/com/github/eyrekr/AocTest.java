package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

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
    void star1_sample() {
        final var aoc = setup.constructor.aoc(setup.star1.sample.input);
        Assertions.assertThat(aoc.star1()).isEqualTo(setup.star1.sample.output);
    }

    @Test
    void star1() {
        final var aoc = setup.constructor.aoc(setup.star1.real.input);
        Assertions.assertThat(aoc.star1()).isEqualTo(setup.star1.real.output);
    }

    @Test
    void star2_sample() {
        final var aoc = setup.constructor.aoc(setup.star2.sample.input);
        Assertions.assertThat(aoc.star2()).isEqualTo(setup.star2.sample.output);

    }

    @Test
    void star2() {
        final var aoc = setup.constructor.aoc(setup.star2.real.input);
        Assertions.assertThat(aoc.star2()).isEqualTo(setup.star2.real.output);
    }
    //endregion


    //region STATIC HELPER METHODS
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

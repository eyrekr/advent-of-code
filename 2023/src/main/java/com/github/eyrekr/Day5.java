package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Day5 {

    static final String SAMPLE = """
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4
            """;

    static final long STEP = 100;

    public static void main(String[] args) throws Exception {
        List<Long> seeds = List.of();
        Map<Type, List<Range>> table = new HashMap<>();
        List<Range> ranges = List.of();
        final List<String> lines = Files.readAllLines(Path.of("src/main/resources/05.txt"));

        for (final String line : lines) {
            if (line.isBlank()) {
                continue;
            } else if (line.startsWith("seeds")) {
                seeds = Arrays.stream(StringUtils.split(StringUtils.substringAfter(line, ":")))
                        .mapToLong(Long::parseLong)
                        .boxed()
                        .toList();
            } else if (line.endsWith("map:")) {
                final Type type = Type.from(StringUtils.split(line)[0]);
                ranges = new ArrayList<>();
                table.put(type, ranges);
            } else {
                final Range range = Range.from(line);
                ranges.add(range);
            }
        }

        // part I
        System.out.println(seeds.stream()
                .map(seed -> new State("seed", seed))
                .map(state -> state.location(table))
                .mapToLong(location -> location.value)
                .min()
                .getAsLong());

        // part II = 148041808
        final var executor = Executors.newFixedThreadPool(6);
        int i = 0;

        System.out.printf("\n%2s   %-12s  %-12s\n", "#", "SEED", "RANGE");
        final List<Future<SeedLocation>> futures = new ArrayList<>();
        while (i < seeds.size()) {
            final long seed = seeds.get(i);
            final long range = seeds.get(i + 1);
            final int jobNumber = i / 2;

            futures.add(executor.submit(() -> run(seed, range, STEP, table, jobNumber)));

            i += 2;
        }


        final SeedLocation around = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (final Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .min(Comparator.comparingLong(element -> element.location))
                .get();
        executor.shutdown();

        System.out.printf("AROUND SEED %d  LOCATION ~ %d\n", around.seed, around.location);

        final SeedLocation min = run(around.seed - STEP, 2 * STEP, 1, table, 10);
        System.out.printf("SEED %d  MIN LOCATION = %d\n", min.seed, min.location); // 148041808
    }

    record SeedLocation(long seed, long location) {
    }

    static SeedLocation run(final long seed, final long range, final long step, final Map<Type, List<Range>> table, final int jobNumber) {
        SeedLocation min = new SeedLocation(seed, Long.MAX_VALUE);
        final long t0 = System.currentTimeMillis();
        for (long j = 0; j < range; j += step) {
            final long location = new State("seed", seed + j).location(table).value;
            if (location < min.location) {
                min = new SeedLocation(seed + j, location);
            }
        }
        final long t = System.currentTimeMillis() - t0;
        System.out.printf(
                "%2d %12d  %12d   [%5.1fs]  %3dK/ms\n",
                jobNumber, min.seed, min.location, t / 1000.0, t > 0 ? range / 1000 / t : 0);
        return min;
    }

    record Type(String source, String destination) {
        static Type from(final String input) {
            final String[] sourceToDestination = StringUtils.splitByWholeSeparator(input, "-to-");
            return new Type(sourceToDestination[0], sourceToDestination[1]);
        }
    }

    record Range(long source, long destination, long range) {
        static Range from(final String input) {
            return Util.fromNumbers(input, ints -> new Range(ints[1], ints[0], ints[2]));
        }

        Long convert(long value) {
            final long delta = value - source;
            if (delta >= 0 && delta < range) {
                return destination + delta;
            }
            return null;
        }
    }

    record State(String state, long value) {
        State next(final Map<Type, List<Range>> table) {
            final Type type = table.keySet().stream()
                    .filter(t -> t.source.equalsIgnoreCase(state))
                    .findFirst()
                    .orElseThrow();
            for (final Range range : table.get(type)) {
                final Long destinationValue = range.convert(value);
                if (destinationValue != null) {
                    return new State(type.destination, destinationValue);
                }
            }
            return new State(type.destination, value);
        }

        State location(final Map<Type, List<Range>> table) {
            State location = this;
            while (!location.state.equalsIgnoreCase("location")) {
                location = location.next(table);
            }
            return location;
        }
    }

}

package com.github.eyrekr;

import com.github.eyrekr.immutable.Seq;

import java.util.Comparator;

/**
 * https://adventofcode.com/2023/day/5
 * 1) 214922730
 * 2) 148041808
 */
class D05 {

    public static void main(String[] args) throws Exception {
        //final var sections = Seq.fromArray(Files.readString(Path.of("src/main/resources/D05.txt")).split("\n\n"));
        final var sections = Seq.fromArray(SAMPLE.split("\n\n"));
        //final var seeds = Str.longs(sections.order).batch(2).print();
        final var seeds = Seq.ofNumbersFromString(sections.value).map(value -> Seq.of(value, 1L)).print();
        final var layers = sections.skip(1)
                .map(section -> Seq.ofNumbersFromString(section).batch(3).map(Range::new).map(Interval::new))
                .map(Layer::new);
        final var collapsedIntervals = layers
                .map(Layer::complete)
                .print("\n")
                .reduce(Layer.L0, Layer::merge)
                .intervals
                .print("\n");
        final var location = seeds.flatMap(input -> {
                    final var a = input.value;
                    final var b =  a + input.tail.value - 1;
                    return collapsedIntervals
                            .where(interval -> interval.a <= a && interval.b >= b)
                            .map(interval -> Math.max(interval.a, a) + interval.delta);
                })
                .min(Long::compare);
        System.out.println(location);
    }

    record Layer(Seq<Interval> intervals) {
        static final Layer L0 = new Layer(Seq.of(new Interval(0, Long.MAX_VALUE, 0)));

        Layer complete() {
            var completeIntervals = intervals.sortedBy(Comparator.comparing(Interval::a));
            var a = completeIntervals.value.a;
            if(a != 0) {
                completeIntervals = completeIntervals.addFirst(new Interval(0, a - 1, 0));
            }
//            completeIntervals = completeIntervals.mapWithNext((interval, next) -> {
//                        Seq<Interval> acc = Seq.empty();
//                        if (next == null) {
//                            acc = acc.add(new Interval(interval.b + 1, Long.MAX_VALUE, 0));
//                        } else if (interval.b + 1 < next.a) {
//                            acc = acc.add(new Interval(interval.b + 1, next.a - 1, 0));
//                        }
//                        acc = acc.add(interval);
//                        return acc;
//                    });
            return new Layer(completeIntervals);
        }

        Layer merge(final Layer layer) {
            if (this == L0) {
                return layer;
            }

            //FIXME the merging logic is flawed because we must consider the the mapped src intervals, not the original intervals!!
            var src = intervals;
            var dst = layer.intervals;
            var merged = Seq.<Interval>empty();
            while (!src.isEmpty && !dst.isEmpty) {
                if (src.value.b == dst.value.b) {
                    merged = merged.addFirst(new Interval(src.value.a, src.value.b, src.value.delta + dst.value.delta));
                    src = src.tail;
                    dst = dst.tail;
                } else if (src.value.b < dst.value.b) {
                    merged = merged.addFirst(new Interval(src.value.a, src.value.b, src.value.delta + dst.value.delta));
                    src = src.tail;
                    dst = dst.tail.addFirst(new Interval(src.value.a, dst.value.b, dst.value.delta));
                } else if (src.value.b > dst.value.b) {
                    merged = merged.addFirst(new Interval(dst.value.a, dst.value.b, src.value.delta + dst.value.delta));
                    dst = dst.tail;
                    src = src.tail.addFirst(new Interval(dst.value.a, src.value.b, src.value.delta));
                }
            }
            return new Layer(merged.sortedBy(Comparator.comparing(Interval::a)));
        }
    }

    record Range(long dst, long src, long length) {
        Range(final Seq<Long> values) {
            this(values.at(0), values.at(1), values.at(2));
        }
    }

    record Interval(long a, long b, long delta) {
        Interval(final Range range) {
            this(range.src, range.src + range.length - 1, range.dst - range.src);
        }

        boolean overlaps(final Interval interval) {
            return (a <= interval.a && interval.a <= b) || (a <= interval.b && interval.b <= b);
        }
    }

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
}

package com.github.eyrekr;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

class Day5 {

    public static void main(String[] args) throws Exception {
        final var sections = Seq.fromArray(Files.readString(Path.of("src/main/resources/05.txt")).split("\n\n"));
        final var seeds = Str.longs(sections.value).batch(2).print();
        final var layers = sections.skip(1)
                .map(section -> Str.longs(section).batch(3).map(Range::new).map(Interval::new))
                .map(Layer::new);
        final var collapsedIntervals = layers
                .map(Layer::complete)
                .print("\n")
                .reduceR(Layer.L0, Layer::merge)
                .intervals;
        collapsedIntervals.print("\n");
    }

    record Layer(Seq<Interval> intervals) {
        static final Layer L0 = new Layer(Seq.of(new Interval(0, Long.MAX_VALUE, 0)));

        Layer complete() {
            final var completeIntervals = intervals.sortedBy(Comparator.comparing(Interval::a))
                    .genMap((previous, current, next) -> {
                        Seq<Interval> acc = Seq.empty();
                        if (next == null) {
                            acc = acc.add(new Interval(current.b + 1, Long.MAX_VALUE, 0));
                        } else if (current.b + 1 < next.a) {
                            acc = acc.add(new Interval(current.b + 1, next.a - 1, 0));
                        }
                        acc = acc.add(current);
                        if (previous == null && current.a > 0) {
                            acc = acc.add(new Interval(0, current.a - 1, 0));
                        }
                        return acc;
                    });
            return new Layer(completeIntervals);
        }

        Layer merge(final Layer layer) {
            if (this == L0) {
                return layer;
            }

            var src = intervals;
            var dst = layer.intervals;
            var merged = Seq.<Interval>empty();
            while (!src.isEmpty && !dst.isEmpty) {
                if (src.value.b == dst.value.b) {
                    merged = merged.add(new Interval(src.value.a, src.value.b, src.value.delta + dst.value.delta));
                    src = src.tail;
                    dst = dst.tail;
                } else if (src.value.b < dst.value.b) {
                    merged = merged.add(new Interval(src.value.a, src.value.b, src.value.delta + dst.value.delta));
                    src = src.tail;
                    dst = dst.tail.add(new Interval(src.value.a, dst.value.b, dst.value.delta));
                } else if (src.value.b > dst.value.b) {
                    merged = merged.add(new Interval(dst.value.a, dst.value.b, src.value.delta + dst.value.delta));
                    dst = dst.tail;
                    src = src.tail.add(new Interval(dst.value.a, src.value.b, src.value.delta));
                }
            }
            //drain - should not be necessary
            //if(!src.isEmpty) merged = merged.add(src.value);
            //if(!dst.isEmpty) merged = merged.add(dst.value);
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
    }
}

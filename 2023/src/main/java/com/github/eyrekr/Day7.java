package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

/**
 * <a href="https://adventofcode.com/2023/day/7">...</a>
 * 1) 248559379
 * 2)
 */
class Day7 {

    static final String SAMPLE = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;

    public static void main(String[] args) throws Exception {
        //final String[] lines = SAMPLE.split("\n");
        final String[] lines = Files.readString(Path.of("src/main/resources/07.txt")).split("\n");
        System.out.printf("%d\n%d\n", star1(lines), star2(lines));
    }

    static long star1(final String[] lines) {
        return Seq.fromArray(lines).map(Card::from)
                .sortedBy(Card::compareTo)
                .print("\n")
                .mapWith(Seq.range(0, lines.length), Card::withValue)
                .reduce(0L, (acc, card) -> acc + card.bid * (card.order + 1));
    }

    enum Type {
        HighCard, OnePair, TwoPair, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind;

        static Type from(final String input) {
            final var histogram = input.chars().boxed().collect(groupingBy(identity()));
            final var x = histogram.values().stream().mapToInt(List::size).max().getAsInt();
            return switch (histogram.size()) {
                case 5 -> HighCard; // 23456
                case 4 -> OnePair; // 22345
                case 3 -> x == 3 ? ThreeOfKind : TwoPair; // TwoPair 22334 | ThreeOfKind 22234 |
                case 2 -> x == 4 ? FourOfKind : FullHouse; // FourOfKind 22223 | FullHouse 22233
                case 1 -> FiveOfKind; // 22222
                default -> throw new IllegalStateException(input);
            };
        }
    }

    record Card(Type type, String hand, long bid, String sort, long order) implements Comparable<Card> {
        static Card from(final String input) {
            final var raw = StringUtils.split(input);
            return new Card(Type.from(raw[0]), raw[0], Long.parseLong(raw[1]), StringUtils.replaceChars(raw[0], "AKQJT98765432", "mlkjihgfedcba"), 0L);
        }

        Card withValue(final long value) {
            return new Card(this.type, this.hand, this.bid, this.sort, value);
        }

        @Override
        public int compareTo(final Card card) {
            final int byType = type.compareTo(card.type);
            return byType != 0 ? byType : sort.compareTo(card.sort);
        }
    }
}

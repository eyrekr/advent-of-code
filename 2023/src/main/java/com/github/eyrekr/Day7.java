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
 * 2) 249631254
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
        return Seq.fromArray(lines).map(line -> Card.from(line, false))
                .sortedBy(Card::compareTo)
                .mapWith(Seq.range(0, lines.length), Card::changeOrder)
                .reduce(0L, (acc, card) -> acc + card.bid * (card.order + 1));
    }

    static long star2(final String[] lines) {
        return Seq.fromArray(lines).map(line -> Card.from(line, true))
                .sortedBy(Card::compareTo)
                .mapWith(Seq.range(0, lines.length), Card::changeOrder)
                .print("\n")
                .reduce(0L, (acc, card) -> acc + card.bid * (card.order + 1));
    }

    enum Type {
        HighCard, OnePair, TwoPair, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind;

        static Type from(final String input, final boolean allowJokers) {
            final var histogram = input.chars().boxed().collect(groupingBy(identity()));
            final var distinctNumbers = histogram.size();
            final var longestRun = histogram.values().stream().mapToInt(List::size).max().getAsInt();
            final var jokers = allowJokers ? StringUtils.countMatches(input, 'J') : 0;
            return switch (jokers) {
                case 5 -> FiveOfKind; // JJJJJ
                case 4 -> FiveOfKind; // *JJJJ => QQQQQ
                case 3 -> switch (distinctNumbers) { // **JJJ
                    case 3 -> FourOfKind; // 23JJJ => 23333
                    case 2 -> FiveOfKind; // 22JJJ => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 2 -> switch (distinctNumbers) { // ***JJ
                    case 4 -> ThreeOfKind; // 234JJ => 23444
                    case 3 -> FourOfKind; // 233JJ => 23333
                    case 2 -> FiveOfKind; // 222JJ => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 1 -> switch (distinctNumbers) { // ****J
                    case 5 -> OnePair; // 2345J => 23455
                    case 4 -> ThreeOfKind; // 2234J => 22234
                    case 3 -> longestRun == 3 ? FourOfKind : FullHouse; // 2233J => 22233 | 2223J => 22223
                    case 2 -> FiveOfKind; // 2222J => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 0 -> switch (distinctNumbers) { // *****
                    case 5 -> HighCard; // 23456
                    case 4 -> OnePair; // 22345
                    case 3 -> longestRun == 3 ? ThreeOfKind : TwoPair; // 22334 | 22234
                    case 2 -> longestRun == 4 ? FourOfKind : FullHouse; // 22223 | 22233
                    case 1 -> FiveOfKind; // 22222
                    default -> throw new IllegalStateException(input);
                };
                default -> throw new IllegalStateException(input);
            };
        }
    }

    record Card(Type type, String hand, long bid, String compareBy, long order) implements Comparable<Card> {
        static Card from(final String input, final boolean allowJokers) {
            final var raw = StringUtils.split(input);
            final var order = StringUtils.replaceChars(raw[0], allowJokers ? "AKQT98765432J" : "AKQJT98765432", "MLKJIHGFEDCBA");
            return new Card(Type.from(raw[0], allowJokers), raw[0], Long.parseLong(raw[1]), order, 0L);
        }

        Card changeOrder(final long order) {
            return new Card(this.type, this.hand, this.bid, this.compareBy, order);
        }

        @Override
        public int compareTo(final Card card) {
            final int byType = type.compareTo(card.type);
            return byType != 0 ? byType : compareBy.compareTo(card.compareBy);
        }

        @Override
        public String toString() {
            return String.format("%4d: %s -> %s", order, hand, type);
        }
    }
}

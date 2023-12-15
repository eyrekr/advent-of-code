package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

/**
 * https://adventofcode.com/2023/day/7
 * 1) 248559379
 * 2) 249631254
 */
class D07 {
    static final char JOKER = '*';

    public static void main(String[] args) throws Exception {
        final Seq<String> lines = Seq.fromArray(Files.readString(Path.of("src/main/resources/D07.txt")).split("\n"));
        System.out.printf("%d\n%d\n",
                solve(lines.map(line -> Card.from(line, false))),
                solve(lines.map(line -> Card.from(line, true))));
    }

    static long solve(final Seq<Card> cards) {
        return cards
                .sortedBy(Card::compareTo)
                .mapWith(Seq.range(0, cards.length), Card::changeOrder)
                .print("\n")
                .reduce(0L, (acc, card) -> acc + card.bid * (card.order + 1));
    }

    enum Type {
        HighCard, OnePair, TwoPair, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind;

        static Type from(final String input) {
            final var histogram = input.chars().boxed().collect(groupingBy(identity()));
            final var distinctNumbers = histogram.size();
            final var longestRun = histogram.values().stream().mapToInt(List::size).max().getAsInt();
            final var jokers = StringUtils.countMatches(input, JOKER);
            return switch (jokers) {
                case 5 -> FiveOfKind; // 22222
                case 4 -> FiveOfKind; // 2222* => 22222
                case 3 -> switch (distinctNumbers) {
                    case 3 -> FourOfKind; // 23*** => 23333
                    case 2 -> FiveOfKind; // 22*** => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 2 -> switch (distinctNumbers) {
                    case 4 -> ThreeOfKind; // 234** => 23444
                    case 3 -> FourOfKind; // 233** => 23333
                    case 2 -> FiveOfKind; // 222** => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 1 -> switch (distinctNumbers) {
                    case 5 -> OnePair; // 2345* => 23455
                    case 4 -> ThreeOfKind; // 2234* => 22234
                    case 3 -> longestRun == 3 ? FourOfKind : FullHouse; // 2233* => 22233 | 2223* => 22223
                    case 2 -> FiveOfKind; // 2222* => 22222
                    default -> throw new IllegalStateException(input);
                };
                case 0 -> switch (distinctNumbers) {
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
        static Card from(final String input, final boolean jokersAllowed) {
            final var raw = StringUtils.split(input);
            final var hand = jokersAllowed ? StringUtils.replaceChars(raw[0], 'J', JOKER) : raw[0];
            final var compareBy = StringUtils.replaceChars(hand, "AKQJT98765432" + JOKER, "NMLKJIHGFEDCBA");
            return new Card(Type.from(hand), hand, Long.parseLong(raw[1]), compareBy, 0L);
        }

        Card changeOrder(final int order) {
            return new Card(this.type, this.hand, this.bid, this.compareBy, order);
        }

        @Override
        public int compareTo(final Card card) {
            final int byType = type.compareTo(card.type);
            return byType != 0 ? byType : compareBy.compareTo(card.compareBy);
        }

        @Override
        public String toString() {
            return String.format("%4d: %s %s", order, hand, type);
        }
    }
}

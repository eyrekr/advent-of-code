package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

class Day4 {

    public static void main(String[] args) throws Exception {
        final Card[] deck = Files.readAllLines(Path.of("src/main/resources/04.txt")).stream()
                .map(Card::from)
                .toArray(Card[]::new);

        long sum = 0;
        for (int cardNumber = 0; cardNumber < deck.length; cardNumber++) {
            final Card card = deck[cardNumber];

            // part I
            long points = 0;
            for (final String numberYouHave : card.numbersYouHave) {
                if (card.winningNumbers.contains(numberYouHave)) {
                    if (points == 0) {
                        points = 1;
                    } else {
                        points *= 2;
                    }
                }
            }
            sum += points;

            // part II
            for (int d = 1; d <= card.matchingNumbers && cardNumber + d < deck.length; d++) {
                final Card cardYouWin = deck[cardNumber + d];
                cardYouWin.instances += card.instances; // you win as many times as you have this card
            }
        }

        System.out.printf("SUM = %d\n", sum);
        System.out.printf("TOTAL = %d\n", Arrays.stream(deck).mapToLong(card -> card.instances).sum());
    }

    static class Card {
        final Set<String> winningNumbers;
        final String[] numbersYouHave;
        final long matchingNumbers;

        long instances = 1;

        Card(final Set<String> winningNumbers, final String[] numbersYouHave) {
            this.winningNumbers = winningNumbers;
            this.numbersYouHave = numbersYouHave;
            this.matchingNumbers = Arrays.stream(numbersYouHave).filter(winningNumbers::contains).count();
        }

        static Card from(final String line) {
            final String ticket = StringUtils.substringAfter(line, ":");
            final String[] numbers = StringUtils.split(ticket, '|');
            final Set<String> winningNumbers = Set.of(StringUtils.split(numbers[0]));
            final String[] numbersYouHave = StringUtils.split(numbers[1]);

            return new Card(winningNumbers, numbersYouHave);
        }
    }
}

package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

class Day4 {

    static final String SAMPLE = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;

    public static void main(String[] args) throws Exception {
        //StringUtils.split(SAMPLE, '\n')
        final Card[] deck = Files.readAllLines(Path.of("src/main/resources/04.txt")).stream()
                .map(Card::from)
                .toArray(Card[]::new);

        long sum = 0;
        for (int i = 0; i < deck.length; i++) {
            final Card card = deck[i];

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
        }

        System.out.printf("SUM = %d", sum);
    }

    static class Card {
        final Set<String> winningNumbers;
        final String[] numbersYouHave;
        final long matchingNumbers;

        int instances = 1;

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

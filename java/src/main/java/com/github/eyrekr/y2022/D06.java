package com.github.eyrekr.y2022;

public class D06 {

    final String input;

    D06(final String input) {
        this.input = input;
    }

    long star1() {
        char a1 = 0, a2 = 0, a3 = 0, a4 = 0;
        for (int i = 0; i < input.length(); i++) {
            final boolean allDifferent = a1 != a2 && a1 != a3 && a1 != a4 && a2 != a3 && a2 != a4 && a3 != a4;
            if (i >= 4 && allDifferent) return i;
            a1 = a2;
            a2 = a3;
            a3 = a4;
            a4 = input.charAt(i);
        }
        return -1;
    }

    long star2() {
        return 0L;
    }
}

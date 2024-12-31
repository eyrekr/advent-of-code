package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;

class D09 extends Aoc {

    static final int Empty = -1;
    final int[] denseMap;

    D09(final String input) {
        final int n = input.length();
        denseMap = new int[n];
        for (int i = 0; i < n; i++) denseMap[i] = input.charAt(i) - '0';
    }

    @Override
    public long star1() {
        return checksum(moveLeft(inflate(denseMap)));
    }

    @Override
    public long star2() {
        return -1L;
    }

    static int[] inflate(final int[] denseMap) {
        int l = 0;
        for (int i = 0; i < denseMap.length; i++) l += denseMap[i];

        final int[] inflatedMap = new int[l];

        int j = 0;
        for (int i = 0; i < denseMap.length; i++)
            for (int k = 0; k < denseMap[i]; k++)
                inflatedMap[j++] = i % 2 == 0 ? i / 2 : Empty;

        return inflatedMap;
    }

    static int[] moveLeft(final int[] map) {
        int l = 0, r = map.length - 1;
        while (true) {
            while (map[l] != Empty && l < r) l++;
            if (l >= r) break;
            map[l] = map[r];
            map[r] = Empty;
            r--;
        }
        return map;
    }

    static long checksum(final int[] map) {
        long checksum = 0;
        for (int i = 0; i < map.length; i++)
            if (map[i] != Empty) checksum += (long) i * map[i];
        return checksum;
    }
}
package com.github.eyrekr;

import java.util.List;

record Grid(int m, int n, char[][] data) {
    static Grid from(List<String> lines) {
        final int n = lines.size();
        final int m = lines.get(0).length();
        final char[][] data = new char[m][n];
        for (int y = 0; y < n; y++) {
            final char[] line = lines.get(y).toCharArray();
            for (int x = 0; x < m; x++) {
                data[x][y] = line[x];
            }
        }
        return new Grid(m, n, data);
    }
}

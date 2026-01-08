package com.github.eyrekr.ilp;

public final class Tableau {

    private final long[][] a;
    private final int rows, columns, variables, constraints, objective, Z, C;

    /**
     * <pre>
     *    variables:   5 = a0, a1, a2, a3, a4
     *    constraints: 6 --> slack variables s0, s1, s2, s3, s4, s5
     *
     *
     *
     *
     *                   variables      slack variables
     *                a0 a1 a2 a3 a4 │ s0 s1 s2 s3 s4 s5 │ Z │ C
     *                ───────────────┼───────────────────┼───┼───
     *    c0           0  0  0  0  1 │ 1  0  0  0  0  0  │ 0 │ 1
     *    c1           0  1  0  0  1 │ 0  1  0  0  0  0  │ 0 │ 1
     *    c2           0  0  1  1  0 │ 0  0  1  0  0  0  │ 0 │ 1
     *    c3           1  0  0  0  1 │ 0  0  0  1  0  0  │ 0 │ 1
     *    c4           1  1  1  0  0 │ 0  0  0  0  1  0  │ 0 │ 1
     *    c5           0  0  1  1  0 │ 0  0  0  0  0  1  │ 0 │ 1
     *                ───────────────┼───────────────────┼───┼───
     *    objective   -3 -5 -4 -7 -1 │ 0  0  0  0  0  0  │ 1 │ 0
     * </pre>
     *
     * @param variables   number of variables
     * @param constraints number of constraints
     */
    public Tableau(final int variables, final int constraints) {
        this.variables = variables;
        this.constraints = constraints;
        this.rows = constraints + 1;
        this.columns = variables + constraints + 2;
        this.a = new long[rows][columns];
        this.objective = this.rows - 1;
        this.C = this.columns - 1;
        this.Z = this.C - 1;
        // init slack variables
        for (int s = 0; s < this.constraints; s++) this.a[s][this.variables + s] = 1;
        // init column Z
        this.a[objective][Z] = 1;
        // init column C
        for (int row = 0; row < objective; row++) this.a[row][C] = 1;
    }

    public Tableau setConstraint(final int c, final long[] coefs, final long value) {
        for (int row = 0; row < objective; row++)
            a[row][c] = coefs[row];
        a[objective][c] = value;
        return this;
    }

    public long minimize() {

    }

    private int findPivotColumn() {
        long min = 0L;
        int argmin = -1;
        for (int column = 0; column < variables; column++)
            if (a[objective][column] < min) {
                argmin = column;
                min = a[objective][column];
            }
        return argmin;
    }

    private int findPivotRow(final int pivotColumn) {
        long min = Long.MAX_VALUE;
        int argmin = -1;
        for (int row = 0; row < objective; row++)
            if (a[row][pivotColumn] > 0 && a[row][C] / a[row][pivotColumn] < min) {
                min = a[row][C] / a[row][pivotColumn];
                argmin = row;
            }
        return argmin;
    }

    private void addRowMultipleToRow(final long[] source, final long factor, final long[] target) {
        for (int i = 0; i < columns; i++) target[i] += factor * source[i];
    }
}

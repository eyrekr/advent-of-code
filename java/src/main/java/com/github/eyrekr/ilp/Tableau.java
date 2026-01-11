package com.github.eyrekr.ilp;

import com.github.eyrekr.output.Out;

public final class Tableau {

    private final Q[][] a;
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
        this.a = new Q[rows][columns];
        for (int row = 0; row < rows; row++) for (int column = 0; column < columns; column++) a[row][column] = Q.Zero;
        this.objective = this.rows - 1;
        this.C = this.columns - 1;
        this.Z = this.C - 1;
        // init slack variables
        for (int s = 0; s < this.constraints; s++) this.a[s][this.variables + s] = Q.One;
        // init column Z
        this.a[objective][Z] = Q.One;
        // init column C
        for (int row = 0; row < objective; row++) this.a[row][C] = Q.One;
    }

    public Tableau setConstraint(final int c, final long[] coefs, final long value) {
        for (int row = 0; row < objective; row++)
            a[row][c] = Q.of(coefs[row]);
        a[objective][c] = Q.of(value);
        return this;
    }

    public Q minimize() {
        for (int pivotColumn = findPivotColumn(); pivotColumn >= 0; pivotColumn = findPivotColumn()) {
            final int pivotRow = findPivotRow(pivotColumn);
            Out.print("""
                            pivoting: column %d  row %d
                            """,
                    pivotColumn, pivotRow);
            divideRow(a[pivotRow], a[pivotRow][pivotColumn]);
            for (int row = 0; row < rows; row++)
                if (row != pivotRow && !a[row][pivotColumn].zero)
                    addRowMultipleToRow(a[pivotRow], a[row][pivotColumn].neg(), a[row]);
            print();
        }
        return a[objective][C];
    }

    private int findPivotColumn() {
        Q min = Q.Zero;
        int argmin = -1;
        for (int column = 0; column < variables + constraints; column++)
            if (a[objective][column].lowerThan(min)) {
                argmin = column;
                min = a[objective][column];
            }
        return argmin;
    }

    private int findPivotRow(final int pivotColumn) {
        Q min = Q.Infinity;
        int argmin = -1;
        for (int row = 0; row < objective; row++)
            if (a[row][pivotColumn].positive) {
                final Q k = a[row][C].div(a[row][pivotColumn]);
                if (k.lowerThan(min)) {
                    min = k;
                    argmin = row;
                }
            }
        return argmin;
    }

    private static void divideRow(final Q[] row, final Q factor) {
        for (int i = 0; i < row.length; i++)
            row[i] = row[i].div(factor);
    }

    private static void addRowMultipleToRow(final Q[] row, final Q factor, final Q[] target) {
        for (int i = 0; i < row.length; i++) target[i] = row[i].mul(factor).add(target[i]);
    }

    void print() {
        Out.print("       ");
        for (int i = 0; i < variables; i++) Out.print("a%02d  ", i);
        for (int i = 0; i < constraints; i++) Out.print("s%02d  ", i);
        Out.print("  Z    C\n");
        for (int row = 0; row < objective; row++) {
            Out.print("b%02d  ", row);
            for (int column = 0; column < columns; column++) {
                final Q value = a[row][column];
                if (value.zero) {
                    Out.print("@W%5s@@", value);
                } else if (value.is(Q.One)) {
                    Out.print("@G%5s@@", value);
                } else {
                    Out.print("%5s", value);
                }
            }
            Out.print("\n");
        }

        Out.print("obj: ");
        for (int column = 0; column < columns; column++) {
            final Q value = a[objective][column];
            if (value.zero) {
                Out.print("@b%5s@@", value);
            } else if (value.negative) {
                Out.print("@r%5s@@", value);
            } else {
                Out.print("@c%5s@@", value);
            }
        }
        Out.print("\n");
    }

    public static void main(String[] args) {

        /**/
        {
            final var t = new Tableau(10, 11);
            t.setConstraint(0, new long[]{1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1}, -95);
            t.setConstraint(1, new long[]{1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1}, -54);
            t.setConstraint(2, new long[]{1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0}, -85);
            t.setConstraint(3, new long[]{1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0}, -114);
            t.setConstraint(4, new long[]{1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0}, -69);
            t.setConstraint(5, new long[]{1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1}, -86);
            t.setConstraint(6, new long[]{1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0}, -92);
            t.setConstraint(7, new long[]{1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, -86);
            t.setConstraint(8, new long[]{1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1}, -90);
            t.setConstraint(9, new long[]{0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0}, -78);
            t.print();
            t.minimize();
        }
        /**/

        /*
        {
            final var t = new Tableau(2, 2);
            t.setConstraint(0, new long[]{1, 2}, -40);
            t.setConstraint(1, new long[]{1, 1}, -30);
            t.a[0][t.C] = F.of(12);
            t.a[1][t.C] = F.of(16);
            t.print();
            final var C = t.minimize();
            Out.print("result @G%s@@\n", C);
        }
        /**/

        /*
        {
            final var t = new Tableau(4, 6);
            t.setConstraint(0, new long[]{0, 0, 0, 0, 1, 1}, -3);
            t.setConstraint(1, new long[]{0, 1, 0, 0, 0, 1}, -5);
            t.setConstraint(2, new long[]{0, 0, 1, 1, 1, 0}, -4);
            t.setConstraint(3, new long[]{1, 1, 0, 1, 0, 0}, -7);
            t.print();
            t.minimize();
        }
         /**/
    }
}

package com.github.eyrekr.ilp;

public final class Tableau {

    private final long[][] a;
    private final int rows, columns, variables, constraints, objective, Z, C;

    public Tableau(final int variables, final int constraints) {
        this.variables = variables;
        this.constraints = constraints;
        this.rows = constraints + 1;
        this.columns = variables + constraints + 2;
        this.a = new long[rows][columns];
        this.objective = this.rows - 1;
        this.C = this.columns - 1;
        this.Z = this.C - 1;
        this.a[objective][Z] = 1;
        // todo init slack variables to E
    }

    public Tableau setConstraint(final int c, final long[] coefs, final long value) {
        for (int i = 0; i < objective; i++)
            a[i][c] = coefs[i];
        a[objective][c] = value;
        return this;
    }

    public Tableau setObjective(final long[] coefs) {
        for (int i = 0; i < objective; i++)
            a[i][C] = coefs[i];
        return this;
    }

    public long minimize() {

    }

    private int findPivotColumn() {
        long min = 0L;
        int column = -1;
        for (int i = 0; i < variables; i++)
            if (a[objective][i] < min) {
                column = i;
                min = a[objective][i];
            }
        return column;
    }

    private int findPivotRow(final int pivotColumn) {
        long min = Long.MAX_VALUE;
        int row = -1;
        for (int i = 0; i < objective; i++)
            if (a[i][pivotColumn] > 0 && a[i][C] / a[i][pivotColumn] < min) {
                min = a[i][C] / a[i][pivotColumn];
                row = i;
            }
        return row;
    }

    private void addRowMultipleToRow(final int source, final long factor, final int target) {
        for (int i = 0; i < columns; i++) a[target][i] += factor * a[source][i];
    }
}

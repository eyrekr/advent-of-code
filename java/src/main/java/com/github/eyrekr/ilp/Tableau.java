package com.github.eyrekr.ilp;

import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.output.Out;

public final class Tableau {

    private final F[][] a;
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
        this.a = new F[rows][columns];
        for (int row = 0; row < rows; row++) for (int column = 0; column < columns; column++) a[row][column] = F.Zero;
        this.objective = this.rows - 1;
        this.C = this.columns - 1;
        this.Z = this.C - 1;
        // init slack variables
        for (int s = 0; s < this.constraints; s++) this.a[s][this.variables + s] = F.One;
        // init column Z
        this.a[objective][Z] = F.One;
        // init column C
        for (int row = 0; row < objective; row++) this.a[row][C] = F.One;
    }

    public Tableau setConstraint(final int c, final long[] coefs, final long value) {
        for (int row = 0; row < objective; row++)
            a[row][c] = F.of(coefs[row]);
        a[objective][c] = F.of(value);
        return this;
    }

    public F minimize() {
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
        F min = F.Zero;
        int argmin = -1;
        for (int column = 0; column < variables; column++)
            if (a[objective][column].lowerThan(min)) {
                argmin = column;
                min = a[objective][column];
            }
        return argmin;
    }

    private int findPivotRow(final int pivotColumn) {
        F min = F.Infinity;
        int argmin = -1;
        for (int row = 0; row < objective; row++)
            if (!a[row][pivotColumn].zero && a[row][C].positive) {
                final F k = a[row][C].div(a[row][pivotColumn]);
                if (k.lowerThan(min)) {
                    min = k;
                    argmin = row;
                }
            }
        return argmin;
    }

    private static void divideRow(final F[] row, final F factor) {
        for (int i = 0; i < row.length; i++)
            row[i] = row[i].div(factor);
    }

    private static void addRowMultipleToRow(final F[] row, final F factor, final F[] target) {
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
                final F value = a[row][column];
                if (value.zero) {
                    Out.print("@K%5s@@", value);
                } else if (value.is(F.One)) {
                    Out.print("@G%5s@@", value);
                } else {
                    Out.print("%5s", value);
                }
            }
            Out.print("\n");
        }

        Out.print("obj: ");
        for (int column = 0; column < columns; column++) {
            final F value = a[objective][column];
            if(value.zero) {
                Out.print("@b%5s@@", value);
            } else if(value.negative) {
                Out.print("@r%5s@@", value);
            } else {
                Out.print("@c%5s@@", value);
            }
        }
        Out.print("\n");
    }

    record F(long a, long b, boolean zero, boolean negative, boolean positive) implements Comparable<F> {
        static F Zero = of(0);
        static F One = of(1);
        static F Infinity = of(Long.MAX_VALUE);

        static F of(final long a) {
            return new F(a, 1, a == 0, a < 0, a > 0);
        }

        static F of(final long a, final long b) {
            if (a == 0) return Zero;
            if (b == 0) throw new ArithmeticException("division by 0 not defined");
            if (b < 0) return of(-a, -b);

            final long gcd = Algebra.gcd(Math.abs(a), Math.abs(b));
            return new F(a / gcd, b / gcd, a == 0, a * b < 0, a * b > 0);
        }

        F abs() {
            return negative ? neg() : this;
        }

        F neg() {
            return of(-a, b);
        }

        F add(final F that) {
            return this.b == that.b
                    ? of(this.a + that.a, this.b)
                    : of(this.a * that.b + that.a * this.b, this.b * that.b);
        }

        F mul(final F that) {
            return of(this.a * that.a, this.b * that.b);
        }


        F div(final F that) {
            return mul(that.inv());
        }

        F inv() {
            return of(b, a);
        }

        @Override
        public String toString() {
            return b == 1 ? String.valueOf(a) : a + "/" + b;
        }

        @Override
        public int compareTo(final F that) {
            return Long.compare(this.a * that.b, that.a * this.b);
        }

        boolean is(final long a) {
            return this.a == a && this.b == 1;
        }

        boolean is(final long a, final long b) {
            return this.a == a && this.b == b;
        }

        boolean is(final F that) {
            return this.equals(that);
        }

        boolean lowerThan(final F that) {
            return compareTo(that) < 0;
        }

        boolean greaterThan(final F that) {
            return compareTo(that) > 0;
        }
    }

    public static void main(String[] args) {
        /*
        final var t = new Tableau(11, 10);
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
        t.print();
        */

        final var t = new Tableau(2, 2);
        t.setConstraint(0, new long[]{1,2}, -40);
        t.setConstraint(1, new long[]{1,1}, -30);
        t.a[0][t.C] = F.of(12);
        t.a[1][t.C] = F.of(16);
        t.print();
        final var C = t.minimize();
        Out.print("result @G%s@@\n", C);
    }
}

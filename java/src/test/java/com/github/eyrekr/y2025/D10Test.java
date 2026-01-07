package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;
import org.junit.jupiter.api.Test;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

import static org.assertj.core.api.Assertions.assertThat;

class D10Test extends AocTest {

    D10Test() {
        constructor(D10::new);
        sample("""
                [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
                [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
                [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
                """);
        star1(7L, 498L);
        star2(33L, -1L);
    }

    /**
     * Input
     * <pre>
     * x0 = (3)
     * x1 = (1,3)
     * x2 = (2)
     * x3 = (2,3)
     * x4 = (0,2)
     * x5 = (0,1)
     * -> {3,5,4,7}
     * </pre>
     * Constraints
     * <pre>
     * c0:    x4 + x5 = 3
     * c1:    x1 + x5 = 5
     * c2:    x2 + x3 + x4 = 4
     * c3:    x0 + x1 + x3 = 7
     * x{i} >= 0, x{i} integer
     * </pre>
     * Objective function
     * <pre>
     * minimize x0 + x1 + x2 + x3 + x4 + x5
     * </pre>
     */
    @Test
    void ILP() {
        final ExpressionsBasedModel model = new ExpressionsBasedModel();
        // Variables                                       x{i} >= 0, x{i} integer
        final Variable x0 = model.newVariable("x0").integer(true).lower(0);
        final Variable x1 = model.newVariable("x1").integer(true).lower(0);
        final Variable x2 = model.newVariable("x2").integer(true).lower(0);
        final Variable x3 = model.newVariable("x3").integer(true).lower(0);
        final Variable x4 = model.newVariable("x4").integer(true).lower(0);
        final Variable x5 = model.newVariable("x5").integer(true).lower(0);
        // Constraints
        model.newExpression("c0").set(x4, 1).set(x5, 1).lower(3).upper(3);
        model.newExpression("c1").set(x1, 1).set(x5, 1).lower(5).upper(5);
        model.newExpression("c2").set(x2, 1).set(x3, 1).set(x4, 1).lower(4).upper(4);
        model.newExpression("c3").set(x0, 1).set(x1, 1).set(x3, 1).lower(7).upper(7);
        // Objective function
        model.newExpression("objective").set(x0, 1).set(x1, 1).set(x2, 1).set(x3, 1).set(x4, 1).set(x5, 1).weight(1.0);
        // Solution
        final Optimisation.Result result = model.minimise();
        System.out.printf("""
                           State: %s
                        Solution: %f
                              x0: %f
                              x1: %f
                              x2: %f
                              x3: %f
                              x4: %f
                              x5: %f
                        """,
                result.getState(),
                result.getValue(),
                x0.getValue(),
                x1.getValue(),
                x2.getValue(),
                x3.getValue(),
                x4.getValue(),
                x5.getValue());
        assertThat(result.getState()).isEqualTo(Optimisation.State.OPTIMAL);
        assertThat(result.getValue()).isEqualTo(10.0);
    }
}

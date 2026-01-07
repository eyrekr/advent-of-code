package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

class D10 extends Aoc {

    final Arr<Machine> machines;

    D10(final String input) {
        machines = Arr.ofLinesFromString(input).map(Machine::fromLine);
    }

    @Override
    public long star1() {
        return machines.toLongs(Machine::minPressesToLights).sum();
    }

    // 16316 too low
    @Override
    public long star2() {
        return machines.toLongs(Machine::minPressesToJoltage).sum();
    }

    record Machine(char[] lights, Arr<Longs> buttons, Longs joltages) {
        static Machine fromLine(final String line) { //    [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            final Arr<String> blocks = Arr.fromArray(StringUtils.split(StringUtils.remove(line, ' '), "[](){}"));
            final char[] lights = blocks.removeFirst().toCharArray();
            final Longs joltage = Longs.fromString(blocks.removeLast());
            final Arr<Longs> buttons = blocks.map(Longs::fromString);
            return new Machine(lights, buttons, joltage);
        }

        long minPressesToLights() {
            final Set<String> processedStates = new HashSet<>();
            final Arr<State> queue = Arr.of(State.initialState(lights.length));

            while (queue.isNotEmpty()) {
                final State state = queue.removeFirst();
                { // avoid infinite loops
                    final String serializedState = state.toString();
                    if (processedStates.contains(serializedState)) continue;
                    processedStates.add(serializedState);
                }
                if (state.matches(lights)) return state.steps;
                buttons.map(state).each(queue::addLast);
            }

            throw new IllegalStateException("sequence not found");
        }

        long minPressesToJoltage() {
            Out.print("""
            @b%s@@
            """,
            joltages);
            final ExpressionsBasedModel model = new ExpressionsBasedModel();
            // Variables
            final Variable[] x = buttons
                    .contextMap((longs, i, first, last) -> model
                            .newVariable("x" + i)
                            .integer(true)
                            .lower(0))
                    .toArray(Variable[]::new);
            {// Constraints
                joltages.contextMap((joltage, position, first, last) -> {
                    final Expression constraint = model.newExpression("c" + position);
                    buttons.contextMap((longs, i, _first, _last) -> {
                        if (longs.has(position)) {
                            constraint.set(x[i], 1);
                            Out.print(x[i].getName() + " + ");
                        }
                        return 0;
                    });
                    constraint.lower(joltage);
                    Out.print(" = %d\n", joltage);
                    return 0;
                });
            }
            {// Objective function
                final Expression objective = model.newExpression("objective");
                for (final Variable v : x) {
                    objective.set(v, 1);
                    Out.print(v.getName() + " + ");
                }
                objective.weight(1.0);
            }
            // Solution
            final Optimisation.Result result = model.minimise();
            Out.print("""
                            = %s %d
                            """,
                    result.getState(),
                    (long) result.getValue());
            for (final Variable v : x) Out.print("  %s = %d\n", v.getName(), v.getValue().longValue());
            return (long) result.getValue();
        }
    }

    record State(char[] lights, long steps) implements Function<Longs, State> {
        static State initialState(final int n) {
            final char[] lights = new char[n];
            Arrays.fill(lights, Symbol.Off);
            return new State(lights, 0L);
        }

        boolean matches(final char[] lights) {
            return Arrays.equals(this.lights, lights);
        }

        @Override
        public State apply(final Longs button) {
            final char[] lights = Arrays.copyOf(this.lights, this.lights.length);
            for (final long i : button)
                lights[(int) i] = lights[(int) i] == Symbol.On ? Symbol.Off : Symbol.On;
            return new State(lights, steps + 1);
        }

        @Override
        public String toString() {
            return String.valueOf(lights);
        }
    }

    interface Symbol {
        char On = '#';
        char Off = '.';
    }
}
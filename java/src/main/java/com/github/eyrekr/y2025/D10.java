package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class D10 extends Aoc {

    final Arr<Machine> machines;

    D10(final String input) {
        machines = Arr.ofLinesFromString(input).map(Machine::fromLine);
    }

    @Override
    public long star1() {
        return machines.toLongs(D10::minPressesRequiredToConfigure).sum();
    }

    @Override
    public long star2() {
        return -2L;
    }

    static long minPressesRequiredToConfigure(final Machine machine) {
        final Set<String> processedStates = new HashSet<>();
        final Arr<State> queue = Arr.of(State.initialState(machine));

        while (queue.isNotEmpty()) {
            final State state = queue.removeFirst();
            { // avoid infinite loops
                final String serializedState = state.toString();
                if (processedStates.contains(serializedState)) continue;
                processedStates.add(serializedState);
            }
            if (state.matches(machine.lights)) return state.steps;
            machine.buttons.map(state::apply).each(queue::addLast);
        }

        throw new IllegalStateException("sequence not found");
    }

    record Machine(char[] lights, Arr<Longs> buttons, Longs joltage) {
        static Machine fromLine(final String line) { //    [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            final Arr<String> blocks = Arr.fromArray(StringUtils.split(StringUtils.remove(line, ' '), "[](){}"));
            final char[] lights = blocks.removeFirst().toCharArray();
            final Longs joltage = Longs.fromString(blocks.removeLast());
            final Arr<Longs> buttons = blocks.map(Longs::fromString);
            return new Machine(lights, buttons, joltage);
        }
    }

    record State(char[] lights, long steps) {
        static State initialState(final Machine machine) {
            final char[] lights = new char[machine.lights.length];
            Arrays.fill(lights, Symbol.Off);
            return new State(lights, 0L);
        }

        boolean matches(final char[] lights) {
            return Arrays.equals(this.lights, lights);
        }

        State apply(final Longs button) {
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
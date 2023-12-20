package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/20
 * 1)
 * 2)
 */
class D20 extends AoC {

    static final String BROADCASTER = "broadcaster";

    final Seq<Module> modules;
    final Map<String, Module> map;
    final Module broadcaster;

    D20(final String input) {
        super(input);
        modules = lines.mapWith(Seq.range(0, lines.length), Module::from);
        map = modules.indexBy(module -> module.name);
        broadcaster = map.get(BROADCASTER);

        // add the "output" modules so that we do not have to constantly deal with NPE
        modules.each(module -> module.out
                .where(out -> map.get(out) == null)
                .each(name -> map.put(name, new Module(Type.Output, -1, name, Seq.empty()))));
        // update activations of conjuctions modules (based on the number of inputs)
        modules.each(module -> module.out.map(map::get)
                .where(Objects::nonNull)
                .where(out -> out.type == Type.Conjunction)
                .each(conjunction -> conjunction.activation |= 1L << module.id));
    }

    long star1() {
        modules.print("\n");
        run();
        return 0L;
    }

    Pulses run() {
        long numberOfLowPulses = 0, numerOfHighPulses = 0;
        final LinkedList<Pulse> pulsesToProcess = new LinkedList<>();
        pulsesToProcess.addFirst(new Pulse(null, broadcaster, 0));

        while (!pulsesToProcess.isEmpty()) {
            final Pulse pulse = pulsesToProcess.removeFirst();
            Str.print("%-20s ---%s---> %s\n",
                    pulse.source != null ? pulse.source.name : "button",
                    pulse.value ==0 ? "low" : "high",
                    pulse.target.name);
            final Module module = pulse.target;
            final boolean low = pulse.value == 0;
            final boolean high = !low;

            if (low) numberOfLowPulses++;
            if (high) numerOfHighPulses++;

            final Consumer<Integer> emit = value -> module.out
                    .map(map::get)
                    .map(nextTarget -> new Pulse(module, nextTarget, value))
                    .each(pulsesToProcess::addLast);

            switch (pulse.target.type) { // SIGNAL LOGIC
                case Broadcaster -> emit.accept(0);
                case FlipFlop -> {
                    if (low) {
                        emit.accept(pulse.target.isActivated() ? 0 : 1);
                        module.update(pulse);
                    }
                }
                case Conjunction -> {
                    module.update(pulse);
                    emit.accept(pulse.target.isActivated() ? 0 : 1);
                }
            }
        }

        return new Pulses(numberOfLowPulses, numerOfHighPulses);
    }

    String encodeStateOfModules(final Seq<Module> modules) {
        final long flips = modules.where(module -> module.type == Type.FlipFlop)
                .reduce(0L, (acc, module) -> module.state > 0 ? acc |= 1L << module.id : acc);
        final String cons = modules.where(module -> module.type == Type.Conjunction)
                .map(module -> module.state)
                .map(Long::toHexString)
                .reduce("", (acc, state) -> acc + "." + state);
        return Long.toHexString(flips) + cons;
    }

    long star2() {
        return 0L;
    }

    enum Type {
        FlipFlop, Conjunction, Broadcaster, Output;

        static Type from(final String input) {
            if (BROADCASTER.equals(input)) return Broadcaster;
            if (input.startsWith("%")) return FlipFlop;
            if (input.startsWith("&")) return Conjunction;
            return Output;
        }
    }

    static final class Module {
        final Type type;
        final int id;
        final String name;
        final Seq<String> out;
        long state = 0L;
        long activation = 0L;

        private Module(final Type type, final int id, final String name, final Seq<String> out) {
            this.type = type;
            this.id = id;
            this.name = name;
            this.out = out;
        }

        boolean isActivated() {
            return switch (type) {
                case FlipFlop -> state != 0;
                case Conjunction -> state == activation;
                default -> true;
            };
        }

        Module update(final Pulse pulse) {
            switch (type) {
                case FlipFlop -> {
                    if (pulse.value != 0) {
                        state = state != 0 ? 0 : 1;
                    }
                }
                case Conjunction -> {
                    state = (pulse.value == 0)
                            ? state & ~(1L << pulse.source.id)
                            : state | (1L << pulse.source.id);
                }
                default -> throw new IllegalStateException("Cannot update " + type);
            }
            return this;
        }

        static Module from(final String input, final int id) { //%a -> inv, con
            final var block = StringUtils.split(input, "->");
            block[0] = block[0].trim();
            final Type type = Type.from(block[0]);
            return new Module(
                    type,
                    id,
                    switch (type) {
                        case FlipFlop, Conjunction -> StringUtils.substring(block[0], 1);
                        case Broadcaster, Output -> block[0];
                    },
                    Seq.fromArray(StringUtils.split(block[1], ", ")));
        }

        @Override
        public String toString() {
            return String.format("%3d %-12s %-20s %x|%x -> %s", id, type, name, state, activation, out);
        }
    }

    record Pulse(Module source, Module target, int value) {
    }

    record Pulses(long low, long high) {
    }

}
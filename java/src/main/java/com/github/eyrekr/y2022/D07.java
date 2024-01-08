package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

public class D07 {

    final Seq<String> lines;

    D07(final String input) {
        lines = Seq.ofLinesFromString(input);
    }

    long star1() {
        final F root = F.dir("/");
        process(root, lines.toMutableArr()).calculateSizes();
        return root.print("").star1();
    }

    long star2() {
        final F root = F.dir("/");
        process(root, lines.toMutableArr()).calculateSizes();
        final long unused = 70000000 - root.size;
        return root.star2(30000000 - unused);
    }

    F process(final F parent, final Arr<String> output) {
        while (output.isNotEmpty()) {
            final String[] arg = StringUtils.split(output.removeFirst());
            if (!"$".equals(arg[0])) throw new IllegalStateException(output.getFirst());
            if ("cd".equals(arg[1]))  //cd
                if ("/".equals(arg[2])) ;
                else if ("..".equals(arg[2])) return parent;
                else parent.children
                            .findFirst(f -> f.directory && f.name.equals(arg[2]))
                            .ifPresent(child -> process(child, output))
                            .getOrThrow(IllegalStateException::new);
            else if ("ls".equals(arg[1])) //ls
                while (output.isNotEmpty() && !output.getFirst().startsWith("$")) {
                    final String[] ls = StringUtils.split(output.removeFirst());
                    if ("dir".equals(ls[0])) parent.children.addLast(F.dir(ls[1]));
                    else parent.children.addLast(F.file(ls[1], Long.valueOf(ls[0])));
                }
        }
        return parent;
    }

    static class F {
        final String name;
        long size;
        final boolean directory;
        final Arr<F> children = Arr.empty();

        F(String name, long size, boolean directory) {
            this.name = name;
            this.size = size;
            this.directory = directory;
        }

        static F file(String name, long size) {
            return new F(name, size, false);
        }

        static F dir(String name) {
            return new F(name, 0, true);
        }

        long calculateSizes() {
            if (directory) size = children.reduce(0L, (acc, child) -> acc + child.calculateSizes());
            return size;
        }

        F print(final String indent) {
            Out.print("%s%s @K(%,d)\n", indent, directory ? "@Y**" + name + "**" : "@w" + name, size);
            if (directory) children.each(child -> child.print(indent + "  "));
            return this;
        }

        long star1() {
            return directory
                    ? children.reduce(size < 100000 ? size : 0L, (sum, child) -> sum + child.star1())
                    : 0L;
        }

        long star2(final long threshold) { // total 70.000.000, required 30.000.000
            return directory
                    ? Math.min(
                    size > threshold ? size : Long.MAX_VALUE,
                    children.reduce(Long.MAX_VALUE, (min, child) -> Math.min(min, child.star2(threshold))))
                    : Long.MAX_VALUE;
        }
    }
}

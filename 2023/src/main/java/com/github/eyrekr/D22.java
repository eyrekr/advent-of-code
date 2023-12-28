package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * https://adventofcode.com/2023/day/22
 * 1)
 * 2)
 */
class D22 extends AoC {
    static final int GROUND = 0;

    final Seq<Brick> bricks;
    final Brick bounds;

    D22(final String input) {
        super(input);
        this.bricks = lines.carryMap(new AtomicInteger(GROUND), Brick::from).sortedBy(Brick::z0);
        this.bounds = bricks.reduce(
                new Brick(0, 0, 0, 0, 0, 0, 0),
                (volume, brick) -> new Brick(0,
                        Math.min(volume.x0, brick.x0), Math.min(volume.y0, brick.y0), Math.min(volume.z0, brick.z0),
                        Math.max(volume.x1, brick.x1), Math.max(volume.y1, brick.y1), Math.max(volume.z1, brick.z1)));
    }

    @Override
    long star1() {
        final int[][] zbuffer = new int[bounds.x1 + 1][bounds.y1 + 1];
        final int[][] brickOnTop = new int[bounds.x1 + 1][bounds.y1 + 1];

        Seq<Integer> soleSupportBrick = Seq.empty();
        for (final Brick brick : bricks) {
            int z = Integer.MIN_VALUE;
            Seq<Integer> supportedBy = Seq.empty();
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    if (zbuffer[x][y] == z) {
                        supportedBy = supportedBy.addFirst(brickOnTop[x][y]);
                    } else if (zbuffer[x][y] > z) {
                        z = zbuffer[x][y];
                        supportedBy = Seq.of(brickOnTop[x][y]);
                    }
                }
            }

            final int z1 = z + (brick.z1 - brick.z0) + 1;
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    zbuffer[x][y] = z1;
                    brickOnTop[x][y] = brick.id;
                }
            }

            // if this brick is only supported by one other brick, that brick cannot be safely disintegrated
            supportedBy = supportedBy.unique().removeFirst(GROUND);
            if (supportedBy.length == 1) soleSupportBrick = soleSupportBrick.addFirst(supportedBy.value);
        }

        return bricks.length - soleSupportBrick.unique().length;
    }


    @Override
    long star2() {
        final int[][] zbuffer = new int[bounds.x1 + 1][bounds.y1 + 1];
        final int[][] brickOnTop = new int[bounds.x1 + 1][bounds.y1 + 1];
        final Seq<Edge> edges = bricks.map(brick -> {
                    int z = Integer.MIN_VALUE;
                    Seq<Integer> supportedBy = Seq.empty();
                    for (int x = brick.x0; x <= brick.x1; x++) {
                        for (int y = brick.y0; y <= brick.y1; y++) {
                            if (zbuffer[x][y] == z) {
                                supportedBy = supportedBy.addFirst(brickOnTop[x][y]);
                            } else if (zbuffer[x][y] > z) {
                                z = zbuffer[x][y];
                                supportedBy = Seq.of(brickOnTop[x][y]);
                            }
                        }
                    }

                    final int z1 = z + (brick.z1 - brick.z0) + 1;
                    for (int x = brick.x0; x <= brick.x1; x++) {
                        for (int y = brick.y0; y <= brick.y1; y++) {
                            zbuffer[x][y] = z1;
                            brickOnTop[x][y] = brick.id;
                        }
                    }

                    return supportedBy.unique().removeFirst(GROUND).map(under -> new Edge(under, brick.id));
                })
                .flatMap(Function.identity());
        return bricks.map(brick -> score(brick.id, edges)).reduce(Integer::sum);
    }

    int score(final int id, final Seq<Edge> edges) {
        final Set<Integer> fallen = new HashSet<>();
        final LinkedList<Integer> queue = new LinkedList<>();
        queue.add(id);
        while (isNotEmpty(queue)) {
            final int brick = queue.removeFirst();
            final boolean allUnderThisBrickHaveFallen = brick == id || edges
                    .where(edge -> edge.top == brick)
                    .map(Edge::bottom)
                    .allAre(fallen::contains);
            if (allUnderThisBrickHaveFallen) {
                fallen.add(brick);
                edges.where(edge -> edge.bottom == brick).map(Edge::top).each(queue::addLast);
            }
        }
        return fallen.size() -1;
    }

    record Brick(int id, int x0, int y0, int z0, int x1, int y1, int z1) {
        static Brick from(final AtomicInteger generator, final String input) {
            final var n = Str.longs(input).map(Long::intValue);
            return new Brick(generator.incrementAndGet(), n.at(0), n.at(1), n.at(2), n.at(3), n.at(4), n.at(5));
        }
    }

    record Edge(int bottom, int top) {
    }
}

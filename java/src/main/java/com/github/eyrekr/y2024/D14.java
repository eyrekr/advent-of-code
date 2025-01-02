package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;

import java.util.Arrays;

/*
  The tree looks like this:

  *******************************
  *                             *
  *                             *
  *                             *
  *                             *
  *              *              *
  *             ***             *
  *            *****            *
  *           *******           *
  *          *********          *
  *            *****            *
  *           *******           *
  *          *********          *
  *         ***********         *
  *        *************        *
  *          *********          *
  *         ***********         *
  *        *************        *
  *       ***************       *
  *      *****************      *
  *        *************        *
  *       ***************       *
  *      *****************      *
  *     *******************     *
  *    *********************    *
  *             ***             *
  *             ***             *
  *             ***             *
  *                             *
  *                             *
  *                             *
  *                             *
  *******************************

 */
class D14 extends Aoc {

    interface Space {
        int Width = 101;
        int Height = 103;
        byte Empty = 0;
        byte Robot = '*';
        int AlignedRobots = "*******************************".length();
    }

    final Arr<Robot> robots;

    D14(final String input) {
        robots = Arr.ofLinesFromString(input).map(Robot::fromString);
    }

    @Override
    public long star1() {
        final var quadrants = robots
                .each(robot -> robot.move(100))
                .map(Robot::quadrant)
                .reduce(new long[5], (count, quadrant) -> {
                    count[quadrant] = count[quadrant] + 1;
                    return count;
                });
        return quadrants[1] * quadrants[2] * quadrants[3] * quadrants[4];
    }

    @Override
    public long star2() {
        final byte[] space = new byte[Space.Width * Space.Height];

        int seconds = 0;
        while (true) {
            Arrays.fill(space, Space.Empty);

            robots.each(robot -> {
                robot.move(1);
                space[robot.y * Space.Width + robot.x] = Space.Robot;
            });

            seconds++;
            if (robotsAligned(space)) return seconds;
        }
    }

    static class Robot {
        int x, y, dx, dy;

        static Robot fromString(final String input) {
            final var l = Longs.fromString(input);
            final var robot = new Robot();
            robot.x = (int) l.at(0);
            robot.y = (int) l.at(1);
            robot.dx = (int) l.at(2);
            robot.dy = (int) l.at(3);
            return robot;
        }

        void move(final int seconds) {
            x = mod(x + seconds * dx, Space.Width);
            y = mod(y + seconds * dy, Space.Height);
        }

        int quadrant() { // quadrant numbers don't really matter
            final int qx = x - Space.Width / 2, qy = y - Space.Height / 2;
            if (qx > 0 && qy < 0) return 1;
            if (qx < 0 && qy < 0) return 2;
            if (qx < 0 && qy > 0) return 3;
            if (qx > 0 && qy > 0) return 4;
            return 0;
        }
    }

    /**
     * @return a % k in the range (0, k - 1)
     */
    static int mod(final int a, final int k) {
        final int mod = a % k;
        return mod < 0 ? mod + k : mod;
    }

    static boolean robotsAligned(final byte[] bytes) {
        int run = 0;
        for (int i = 0; i < bytes.length; i++)
            if (bytes[i] == Space.Robot) {
                if (++run >= Space.AlignedRobots) return true;
            } else run = 0;
        return false;
    }
}
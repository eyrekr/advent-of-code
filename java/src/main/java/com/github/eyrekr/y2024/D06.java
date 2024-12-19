package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.raster.Direction;

class D06 extends Aoc {

    final Grid grid;

    D06(final String input) {
        this.grid = Grid.of(input);
    }

    @Override
    public long star1() {
        Grid.It it = grid.first('^');
        Direction direction = Direction.Up;

        while (it != null) {
            it.set('X');
            if (it.lookAhead(direction) == Grid.WALL) direction = direction.turn90DegreesRight();
            else it = it.go(direction);
        }

        return grid.count('X');
    }

    @Override
    public long star2() { // 426 is not the right answer:  your answer is too low
        Grid.It it = grid.first('^');
        Direction direction = Direction.Up;

        while (it != null) {
            if (it.neighbours8[direction.i8] == '.') { // space ahead is empty => what if we place an obstacle there
                final Direction toTheRight = direction.turn90DegreesRight();
                for (Grid.It explore = it.go(toTheRight); explore != null && explore.ch != Grid.WALL; explore = explore.go(toTheRight)) {
                    if ((explore.d & toTheRight.flag) > 0) { // we found our steps in this direction
                        it.go(direction).setState(Grid.State.Closed);
                        break;
                    }
                } 
            }
            
            it = it.set('X').setDistance(it.d | direction.flag); // make sure `it` sees the updated `d` value
            if (it.lookAhead(direction) == Grid.WALL) direction = direction.turn90DegreesRight();
            else it = it.go(direction);
        }

        return grid.count(t -> t.state == Grid.State.Closed);
    }
}
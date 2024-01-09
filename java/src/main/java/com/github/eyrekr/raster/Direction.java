package com.github.eyrekr.raster;

public enum Direction {
    None(0, 0, '×'),
    Up(0, -1, '↑'),
    Down(0, +1, '↓'),
    Left(-1, 0, '←'),
    Right(+1, 0, '→');
    public final int dx, dy;
    public final char ch;

    Direction(int dx, int dy, char ch) {
        this.dx = dx;
        this.dy = dy;
        this.ch = ch;
    }

    public boolean isOpposite(final Direction direction) {
        return switch (this) {
            case None -> false;
            case Up -> direction == Down;
            case Down -> direction == Up;
            case Left -> direction == Right;
            case Right -> direction == Left;
        };
    }

    public Direction opposite() {
        return switch (this) {
            case None -> None;
            case Up -> Down;
            case Down -> Up;
            case Left -> Right;
            case Right -> Left;
        };
    }

    public static Direction fromChar(final char ch) {
        return switch (ch) {
            case 'U', 'u', '^', '↑' -> Direction.Up;
            case 'D', 'd', 'v', '↓' -> Direction.Down;
            case 'L', 'l', '<', '←' -> Direction.Left;
            case 'R', 'r', '>', '→' -> Direction.Right;
            default -> Direction.None;
        };
    }
}
package javachess;

import javachess.decorators.Directions;

/**
 * Class representing a position on the chessboard.
 * The position is represented by x and y coordinates.
 * The coordinate (0, 0) corresponds to the top-left corner of the board.
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Position object from a string representation.
     * @param part the string representation of the position (e.g., "a1", "h8")
     */
    public Position(String part) {
        if (part.length() != 2) {
            throw new IllegalArgumentException("Invalid position format");
        }
        char column = part.charAt(0);
        char row = part.charAt(1);
        x = column - 'a';
        y = 8 - (row - '0');
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IllegalArgumentException("Position out of bounds");
        }
    }

    public boolean equals(Position position) {
        return this.x == position.x && this.y == position.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void add(Position position) {
        this.x += position.x;
        this.y += position.y;
    }

    public void add(Directions directions) {
        this.x += directions.dx;
        this.y += directions.dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position position) {
            return this.x == position.x && this.y == position.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x * 1000 + y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Converts the position to a string representation in PGN format.
     * @return the PGN representation of the position (e.g., "a1", "h8")
     */
    public String asPGN() {
        return String.valueOf((char) ('a' + x)) + (8 - y);
    }
}
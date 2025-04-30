package javachess.decorators;

public enum Directions {
    // Regular moves
    TOP(0, -1),
    BOTTOM(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    TOP_LEFT(-1, -1),
    TOP_RIGHT(1, -1),
    BOTTOM_LEFT(-1, 1),
    BOTTOM_RIGHT(1, 1),
    // Knight moves
    TWO_LEFT_ONE_UP(-2, 1),
    TWO_LEFT_ONE_DOWN(-2, -1),
    TWO_RIGHT_ONE_UP(2, 1),
    TWO_RIGHT_ONE_DOWN(2, -1),
    ONE_LEFT_TWO_UP(-1, 2),
    ONE_LEFT_TWO_DOWN(-1, -2),
    ONE_RIGHT_TWO_UP(1, 2),
    ONE_RIGHT_TWO_DOWN(1, -2);


    public final int dx;
    public final int dy;

    Directions(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }
}

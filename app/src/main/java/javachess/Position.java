package javachess;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position position) {
        return this.x == position.x && this.y == position.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
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
}
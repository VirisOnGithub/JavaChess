package javachess.model;

/**
 * Class representing a move in the chess game.
 * @param from the starting position of the move
 * @param to the ending position of the move
 */
public record Move(Position from, Position to) {

    /**
     * Gets the mean position of a move (used to quickly detect if a pawn just moved two squares).
     * @return
     */
    public Position getMiddlePosition() {
        return new Position((from.getX() + to.getX()) / 2, (from.getY() + to.getY()) / 2);
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}

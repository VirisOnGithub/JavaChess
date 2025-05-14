package javachess.events;

import javachess.model.PieceColor;

/**
 * Event that changes the player whose turn it is.
 */
public class ChangePlayerEvent extends Event {
    private final PieceColor color;

    public ChangePlayerEvent(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
package javachess.events;

import javachess.model.Position;

/**
 * Event that indicates a promotion of a pawn to a different piece.
 * This event is triggered when a pawn reaches the opposite side of the board.
 */
public class PromotionEvent extends Event {
    Position from;

    public PromotionEvent(Position from) {
        this.from = from;
    }

    public Position getFrom() {
        return from;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

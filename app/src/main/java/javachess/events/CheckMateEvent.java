package javachess.events;

import javachess.model.PieceColor;

/**
 * Event that indicates a checkmate situation in the game.
 */
public class CheckMateEvent extends Event {
    private final PieceColor winnerColor;

    public CheckMateEvent(PieceColor winnerColor) {
        this.winnerColor = winnerColor;
    }

    public PieceColor getWinnerColor() {
        return winnerColor;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

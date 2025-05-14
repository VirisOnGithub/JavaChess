package javachess.events;

/**
 * Event that indicates a draw situation in the game.
 * This can be due to insufficient material, or threefold repetition.
 */
public class DrawEvent extends Event {
    String reason;

    public DrawEvent(String reason) {
        this.reason = reason;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public String getReason() {
        return reason;
    }
}

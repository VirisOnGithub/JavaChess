package javachess.events;

/**
 * Event that indicates a stalemate situation in the game.
 * This event is triggered when a player has no legal moves and is not in check.
 */
public class StalemateEvent extends Event {

    @Override
    public String toString() {
        return "PatEvent";
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

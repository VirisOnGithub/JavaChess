package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

/**
 * Event that indicates a check on the king.
 */
public class CheckEvent extends Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

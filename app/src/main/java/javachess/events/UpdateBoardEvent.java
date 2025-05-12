package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

/**
 * Event that indicates an update to the board.
 * This event is used to notify the window to update the board display.
 */
public class UpdateBoardEvent extends Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

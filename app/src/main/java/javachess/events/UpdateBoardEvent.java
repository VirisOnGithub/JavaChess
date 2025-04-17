package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

public class UpdateBoardEvent extends Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

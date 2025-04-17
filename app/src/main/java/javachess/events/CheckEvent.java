package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

public class CheckEvent extends Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

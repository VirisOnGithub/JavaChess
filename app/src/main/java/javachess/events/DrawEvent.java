package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

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

package javachess.events;

import javachess.*;

public class PatEvent extends Event {

    @Override
    public String toString() {
        return "PatEvent";
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

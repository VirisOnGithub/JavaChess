package javachess.events;

import javachess.*;

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

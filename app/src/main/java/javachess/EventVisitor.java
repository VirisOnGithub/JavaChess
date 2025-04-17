package javachess;

import javachess.events.CheckEvent;
import javachess.events.UpdateBoardEvent;

public interface EventVisitor {

    default void visit(Event event) {
        event.accept(this);
    }

    void visit(CheckEvent event);
    void visit(UpdateBoardEvent event);


}

package javachess;

import javachess.events.*;

public interface EventVisitor {

    default void visit(Event event) {
        event.accept(this);
    }

    void visit(CheckEvent event);
    void visit(CheckMateEvent event);
    void visit(UpdateBoardEvent event);
    void visit(PromotionEvent event);
    void visit(DrawEvent event);
    void visit(PatEvent event);
    void visit(ChangePlayerEvent event);
}

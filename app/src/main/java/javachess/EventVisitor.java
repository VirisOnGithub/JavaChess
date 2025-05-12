package javachess;

import javachess.events.*;

/**
 * Interface for the visitor pattern used in the event system.
 * This interface defines methods for visiting different types of events.
 * The default method visit(Event event) allows for a generic visit to any event.
 */
public interface EventVisitor {

    default void visit(Event event) {
        event.accept(this);
    }

    void visit(CheckEvent event);
    void visit(CheckMateEvent event);
    void visit(UpdateBoardEvent event);
    void visit(PromotionEvent event);
    void visit(DrawEvent event);
    void visit(StalemateEvent event);
    void visit(ChangePlayerEvent event);
    void visit(SoundEvent event);
}

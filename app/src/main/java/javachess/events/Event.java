package javachess.events;

/**
 * Abstract class representing an event in the chess game.
 * This class is part of the event visitor design pattern.
 */
public abstract class Event {
    public abstract void accept(EventVisitor visitor);
}

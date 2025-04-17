package javachess;

public abstract class Event {

    public abstract void accept(EventVisitor visitor);

}

package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

public class SoundEvent extends Event {
    private final String sound;

    public SoundEvent(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}

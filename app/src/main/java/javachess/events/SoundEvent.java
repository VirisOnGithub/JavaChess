package javachess.events;

import javachess.Event;
import javachess.EventVisitor;

/**
 * Event that indicates a sound to be played.
 * It is sent to the window to play a sound.
 */
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

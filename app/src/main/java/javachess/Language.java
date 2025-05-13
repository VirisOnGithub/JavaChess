package javachess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Enum representing the supported languages in the application.
 */
public enum Language {
    ENGLISH,
    FRENCH;

    public static Language idToLanguage(int id) {
        return switch (id) {
            case 1 -> FRENCH;
            default -> ENGLISH;
        };
    }

    public static BiMap<Language, Message> getLanguageMap() {
        BiMap<Language, Message> lm = new BiMap<>();
        lm.put(ENGLISH, Message.ENGLISH);
        lm.put(FRENCH, Message.FRENCH);
        return lm;
    }
}
package javachess.translation;

import javachess.model.BiMap;

/**
 * Enum representing the supported languages in the application.
 */
public enum Language {
    ENGLISH,
    FRENCH,
    SPANISH;

    public static Language idToLanguage(int id) {
        return switch (id) {
            case 1 -> FRENCH;
            case 2 -> SPANISH;
            default -> ENGLISH;
        };
    }

    public static BiMap<Language, Message> getLanguageMap() {
        BiMap<Language, Message> lm = new BiMap<>();
        lm.put(ENGLISH, Message.ENGLISH);
        lm.put(FRENCH, Message.FRENCH);
        lm.put(SPANISH, Message.SPANISH);
        return lm;
    }
}
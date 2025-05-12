package javachess;

import java.util.Map;

/**
 * LanguageService is responsible for managing the language of the application.
 * It provides methods to set the language and retrieve messages in the selected language.
 * It is used like i18n.
 */
public class LanguageService {
    private Language language;

    public LanguageService() {
        this.language = Language.ENGLISH;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getMessage(Message key, Map<String, String> variables) {
        String message = switch (language) {
            case FRENCH -> getFrenchMessage(key);
            default -> getEnglishMessage(key);
        };
        return formatMessage(message, variables);
    }

    private String getEnglishMessage(Message key) {
        return switch (key) {
            case CHECKMATE -> "Checkmate! {player} wins!";
            case CHECK -> "Check!";
            case STALEMATE -> "Stalemate!";
            case PROMOTE -> "Choose a piece to promote to:";
            default -> "Unknown message key";
        };
    }

    private String getFrenchMessage(Message key) {
        return switch (key) {
            case CHECKMATE -> "Échec et mat! {player} gagne!";
            case CHECK -> "Échec!";
            case STALEMATE -> "Pat!";
            case PROMOTE -> "Choisissez une pièce pour la promotion:";
            default -> "Clé de message inconnue";
        };
    }

    /**
     * Formats the message by replacing variables in the message template.
     * @param message the message template
     * @param variables the variables to replace in the message
     * @return the formatted message
     */
    private String formatMessage(String message, Map<String, String> variables) {
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return message;
    }
}

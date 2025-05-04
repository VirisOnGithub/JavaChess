package javachess;

import java.util.Map;

public class LanguageService {
    private Language language;

    public LanguageService() {
        this.language = Language.ENGLISH;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getMessage(Message key, Map<String, String> variables) {
        String message;
        switch (language) {
            case FRENCH:
                message = getFrenchMessage(key);
                break;
            case ENGLISH:
            default:
                message = getEnglishMessage(key);
                break;
        }
        return formatMessage(message, variables);
    }

    private String getEnglishMessage(Message key) {
        switch (key) {
            case CHECKMATE:
                return "Checkmate! {player} wins!";
            case CHECK:
                return "Check!";
            case STALEMATE:
                return "Stalemate!";
            case PROMOTE:
                return "Choose a piece to promote to:";
            default:
                return "Unknown message key";
        }
    }

    private String getFrenchMessage(Message key) {
        switch (key) {
            case CHECKMATE:
                return "Échec et mat! {player} gagne!";
            case CHECK:
                return "Échec!";
            case STALEMATE:
                return "Pat!";
            case PROMOTE:
                return "Choisissez une pièce pour la promotion:";
            default:
                return "Clé de message inconnue";
        }
    }

    private String formatMessage(String message, Map<String, String> variables) {
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return message;
    }
}

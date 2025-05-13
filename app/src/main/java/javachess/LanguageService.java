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

    public String getMessage(Message key) {
        return getMessage(key, null);
    }

    public String getEnglishMessage(Message key) {
        return switch (key) {
            case CHECKMATE -> "Checkmate! {player} wins!";
            case CHECK -> "Check!";
            case STALEMATE -> "Stalemate!";
            case DRAW -> "Draw";
            case THREEFOLD_REPETITION -> "This game is a draw due to the threefold repetition rule.";
            case FIFTY_MOVES -> "This game is a draw due to the fifty-move rule.";
            case PROMOTE -> "Choose a piece to promote to:";
            case SETTINGS -> "Settings";
            case LANGUAGE -> "Language";
            case ENGLISH -> "English";
            case FRENCH -> "French";
            case PIECE_SET -> "Piece Set";
            case SOUND -> "Sound";
            case ENABLE_SOUND -> "Enable Sound";
            case SAVE -> "Save";
            case SAVING_FAILED -> "Failed to save settings.";
            case ERROR -> "Error";
            case MAIN_MENU -> "Main Menu";
            case PLAY_VS_PLAYER -> "Play vs Player";
            case PLAY_VS_COMPUTER -> "Play vs Computer";
            case LOAD_FROM_PGN -> "Load from PGN file";
            case OPEN_PGN_FILE -> "Open PGN file";
            case WHITE -> "White";
            case BLACK -> "Black";
            case TO_PLAY -> "{color} to play";
            default -> "Unknown message key";
        };
    }

    private String getFrenchMessage(Message key) {
        return switch (key) {
            case CHECKMATE -> "Échec et mat ! Les {player} gagnent !";
            case CHECK -> "Échec !";
            case STALEMATE -> "Pat !";
            case DRAW -> "Match nul ";
            case THREEFOLD_REPETITION -> "Cette partie est nulle en raison de la règle de la répétition.";
            case FIFTY_MOVES -> "Cette partie est nulle en raison de la règle des cinquante coups.";
            case PROMOTE -> "Choisissez une pièce pour la promotion:";
            case SETTINGS -> "Paramètres";
            case LANGUAGE -> "Langue";
            case ENGLISH -> "Anglais";
            case FRENCH -> "Français";
            case PIECE_SET -> "Ensemble de pièces";
            case SOUND -> "Son";
            case ENABLE_SOUND -> "Activer le son";
            case SAVE -> "Sauvegarder";
            case SAVING_FAILED -> "Échec de la sauvegarde des paramètres.";
            case ERROR -> "Erreur";
            case MAIN_MENU -> "Menu principal";
            case PLAY_VS_PLAYER -> "Jouer contre un joueur";
            case PLAY_VS_COMPUTER -> "Jouer contre l'ordinateur";
            case LOAD_FROM_PGN -> "Charger depuis un fichier PGN";
            case OPEN_PGN_FILE -> "Ouvrir un fichier PGN";
            case WHITE -> "Blancs";
            case BLACK -> "Noirs";
            case TO_PLAY -> "Trait aux {color}";
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

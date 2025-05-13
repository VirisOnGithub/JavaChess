package javachess;

/**
 * Enum representing the two colors of chess pieces.
 * Each color has a string representation for display purposes.
 */
public enum PieceColor {
    WHITE {
        @Override
        public String toString() {
            LanguageService ls = new LanguageService();
            ls.setLanguage(new ConfigParser().getLanguage());
            return ls.getMessage(Message.WHITE);
        }
    },
    BLACK {
        @Override
        public String toString() {
            LanguageService ls = new LanguageService();
            ls.setLanguage(new ConfigParser().getLanguage());
            return ls.getMessage(Message.BLACK);
        }
    }
}
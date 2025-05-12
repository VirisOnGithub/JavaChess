package javachess;

/**
 * Enum representing the two colors of chess pieces.
 * Each color has a string representation for display purposes.
 */
public enum PieceColor {
    WHITE {
        @Override
        public String toString() {
            return "White";
        }
    },
    BLACK {
        @Override
        public String toString() {
            return "Black";
        }
    }
}
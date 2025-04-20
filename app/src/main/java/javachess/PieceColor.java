package javachess;

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
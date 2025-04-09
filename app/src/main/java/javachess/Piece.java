package javachess;

public abstract class Piece {
    private PieceColor color;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public abstract PieceType getType();

    public String findFile() {
        String filename = "";
        switch (color) {
            case WHITE:
                filename += "white";
                break;
            case BLACK:
                filename += "black";
                break;
        }
        filename += "-";
        switch (getType()) {
            case PAWN:
                filename += "pawn";
                break;
            case ROOK:
                filename += "rook";
                break;
            case KNIGHT:
                filename += "knight";
                break;
            case BISHOP:
                filename += "bishop";
                break;
            case QUEEN:
                filename += "queen";
                break;
            case KING:
                filename += "king";
                break;
        }
        filename += ".png";
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return piece.getType() == getType() && color == piece.color;
    }

    @Override
    public int hashCode() {
        return color.ordinal() * 10 + getType().ordinal();
    }
}

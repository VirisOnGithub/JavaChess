package javachess;

import javachess.decorators.PieceDecorator;
import javachess.pieces.*;

/**
 * Abstract class representing a chess piece.
 * This class serves as a base for all specific piece types (e.g., Pawn, Rook, Knight, etc.).
 */
public abstract class Piece {
    private final PieceColor color;
    private Cell cell;
    private boolean moved = false;
    protected PieceDecorator decorator;

    public Piece(PieceColor color, Cell cell) {
        this.color = color;
        this.cell = cell;
    }

    public Piece(PieceColor color){
        this.color = color;
        this.cell = null;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved() {
        this.moved = true;
    }

    public Cell getCell() {
        return cell;
    }

    public abstract PieceType getType();

    public PieceDecorator getDecorator() {
        return decorator;
    }

    /**
     * Finds the filename of the image representing the piece.
     * The filename is constructed based on the color and type of the piece.
     *
     * @return The filename of the image representing the piece.
     */
    public String findFile() {
        String filename = "";
        switch (color) {
            case WHITE:
                filename += "w";
                break;
            case BLACK:
                filename += "b";
                break;
        }
        switch (getType()) {
            case PAWN:
                filename += "P";
                break;
            case ROOK:
                filename += "R";
                break;
            case KNIGHT:
                filename += "N";
                break;
            case BISHOP:
                filename += "B";
                break;
            case QUEEN:
                filename += "Q";
                break;
            case KING:
                filename += "K";
                break;
        }
        filename += ".png";
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece piece)) return false;
        return piece.getType() == getType() && color == piece.color;
    }

    @Override
    public int hashCode() {
        return color.ordinal() * 10 + getType().ordinal();
    }

    @Override
    public String toString() {
        String type;
        switch (getType()) {
            case PAWN -> type = "P";
            case ROOK -> type = "R";
            case KNIGHT -> type = "N";
            case BISHOP -> type = "B";
            case QUEEN -> type = "Q";
            case KING -> type = "K";
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        }
        return "Piece{" +
                "type=" + type +
                "; color=" + color +
                '}';
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * Returns the FEN character representing the piece.
     * @return The FEN character representing the piece.
     */
    public char getFEN() {
        char fen = switch (getType()) {
            case PAWN -> 'P';
            case ROOK -> 'R';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case KING -> 'K';
        };
        return color == PieceColor.WHITE ? fen : Character.toLowerCase(fen);
    }

    static Piece fromFEN(char c, Cell cell) {
        PieceColor color = Character.isUpperCase(c) ? PieceColor.WHITE : PieceColor.BLACK;
        char pieceChar = Character.toUpperCase(c);

        return switch (pieceChar) {
            case 'P' -> new Pawn(color, cell);
            case 'R' -> new Rook(color, cell);
            case 'N' -> new Knight(color, cell);
            case 'B' -> new Bishop(color, cell);
            case 'Q' -> new Queen(color, cell);
            case 'K' -> new King(color, cell);
            default -> throw new IllegalArgumentException("Invalid piece character: " + c);
        };
    }
}

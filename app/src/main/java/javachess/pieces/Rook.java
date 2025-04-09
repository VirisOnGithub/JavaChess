package javachess.pieces;

import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.ROOK;
    }
}

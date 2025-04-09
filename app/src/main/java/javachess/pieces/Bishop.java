package javachess.pieces;

import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.BISHOP;
    }
}

package javachess.pieces;

import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

public class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.KING;
    }
}

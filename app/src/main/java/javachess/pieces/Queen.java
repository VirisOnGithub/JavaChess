package javachess.pieces;

import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }
}

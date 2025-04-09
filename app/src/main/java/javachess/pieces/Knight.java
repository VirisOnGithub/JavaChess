package javachess.pieces;

import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

public class Knight extends Piece {
    public Knight(PieceColor color){
        super(color);
    }

    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}

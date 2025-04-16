package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;
import javachess.decorators.KnightDecorator;

public class Knight extends Piece {
    public Knight(PieceColor color, Cell cell){
        super(color, cell);
        decorator = new KnightDecorator(this, cell.getBoard(), null);
    }

    public Knight(PieceColor color){
        super(color);
    }

    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}

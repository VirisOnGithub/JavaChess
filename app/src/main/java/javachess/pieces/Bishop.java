package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;
import javachess.decorators.BishopDecorator;

public class Bishop extends Piece {
    public Bishop(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new BishopDecorator(this, cell.getBoard(), null);
    }

    public Bishop(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.BISHOP;
    }
}

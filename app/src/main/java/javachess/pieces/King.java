package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

import javachess.decorators.RookDecorator;

public class King extends Piece {
    public King(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new RookDecorator(this, cell.getBoard(), null);
    }

    public King(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.KING;
    }
}

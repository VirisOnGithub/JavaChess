package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;

import javachess.decorators.KingDecorator;
import javachess.decorators.RoqueDecorator;

public class King extends Piece {
    public King(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new KingDecorator(this, cell.getBoard(), new RoqueDecorator(this, cell.getBoard(), null));
        // new RoqueDecorator(this, cell.getBoard(), null)
    }

    public King(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.KING;
    }
}

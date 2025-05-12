package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;
import javachess.decorators.BishopDecorator;
import javachess.decorators.RookDecorator;

/**
 * Queen class that represents the queen piece in chess.
 */
public class Queen extends Piece {
    public Queen(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new RookDecorator(this, cell.getBoard(), new BishopDecorator(this, cell.getBoard(), null));
    }

    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }
}

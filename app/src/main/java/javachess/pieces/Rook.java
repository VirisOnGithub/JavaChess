package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;
import javachess.decorators.RookDecorator;

/**
 * Class representing a rook piece in a chess game.
 */
public class Rook extends Piece {
    public Rook(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new RookDecorator(this, cell.getBoard(), null);
    }

    public Rook(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.ROOK;
    }
}

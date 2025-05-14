package javachess.pieces;

import javachess.model.Cell;
import javachess.model.Piece;
import javachess.model.PieceColor;
import javachess.model.PieceType;
import javachess.decorators.EnPassantDecorator;
import javachess.decorators.PawnDecorator;

/**
 * Class that represents a pawn piece.
 */
public class Pawn extends Piece {
    public Pawn(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new PawnDecorator(this, cell.getBoard(), new EnPassantDecorator(this, cell.getBoard(), null));
    }

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}

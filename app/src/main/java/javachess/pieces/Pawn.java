package javachess.pieces;

import javachess.Cell;
import javachess.Piece;
import javachess.PieceColor;
import javachess.PieceType;
import javachess.decorators.EnPassantDecorator;
import javachess.decorators.PawnDecorator;

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

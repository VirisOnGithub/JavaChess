package javachess.pieces;

import javachess.model.Cell;
import javachess.model.Piece;
import javachess.model.PieceColor;
import javachess.model.PieceType;
import javachess.decorators.KnightDecorator;

/**
 * Class representing a knight piece in chess.
 */
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

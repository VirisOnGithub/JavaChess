package javachess.pieces;

import javachess.model.Cell;
import javachess.model.Piece;
import javachess.model.PieceColor;
import javachess.model.PieceType;

import javachess.decorators.KingDecorator;
import javachess.decorators.CastlingDecorator;

/**
 * Class that represents a king piece in the chess game.
 */
public class King extends Piece {
    public King(PieceColor color, Cell cell) {
        super(color, cell);
        decorator = new KingDecorator(this, cell.getBoard(), new CastlingDecorator(this, cell.getBoard(), null));
        // new RoqueDecorator(this, cell.getBoard(), null)
    }

    public King(PieceColor color) {
        super(color);
    }

    public PieceType getType() {
        return PieceType.KING;
    }
}

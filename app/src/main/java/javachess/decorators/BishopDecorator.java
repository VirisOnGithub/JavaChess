package javachess.decorators;

import javachess.model.Board;
import javachess.model.Cell;
import javachess.model.Piece;

import java.util.ArrayList;

public class BishopDecorator extends PieceDecorator{
    private final PieceDecorator pieceDecorator;

    public BishopDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
    }


    /**
     * Get the valid cells for the bishop.
     * @return An ArrayList of valid cells for the bishop.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        Directions[] directions = {
                Directions.TOP_LEFT,
                Directions.TOP_RIGHT,
                Directions.BOTTOM_LEFT,
                Directions.BOTTOM_RIGHT
        };
        ArrayList<Cell> validCells = getDirectedCells(directions, this);
        validCells.addAll(pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells());
        return validCells;
    }
}

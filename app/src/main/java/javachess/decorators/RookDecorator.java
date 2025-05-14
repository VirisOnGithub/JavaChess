package javachess.decorators;

import javachess.model.Board;
import javachess.model.Cell;
import javachess.model.Piece;

import java.util.ArrayList;

public class RookDecorator extends PieceDecorator {
    private final PieceDecorator pieceDecorator;

    public RookDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
    }

    /**
     * Get the valid cells for the rook.
     * @return An ArrayList of valid cells for the rook.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        Directions[] directions = {Directions.TOP, Directions.BOTTOM, Directions.LEFT, Directions.RIGHT};
        ArrayList<Cell> validCells = getDirectedCells(directions, this);
        validCells.addAll(pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells());
        return validCells;
    }
}

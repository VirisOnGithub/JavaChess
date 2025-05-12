package javachess.decorators;

import javachess.*;
import java.util.ArrayList;

public class KingDecorator extends PieceDecorator {
    private final PieceDecorator pieceDecorator;

    public KingDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
    }


    /**
     * Get the valid cells for the king.
     * @return An ArrayList of valid cells for the king.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        Directions[] directions = {
                Directions.TOP,
                Directions.BOTTOM,
                Directions.LEFT,
                Directions.RIGHT,
                Directions.TOP_LEFT,
                Directions.TOP_RIGHT,
                Directions.BOTTOM_LEFT,
                Directions.BOTTOM_RIGHT
        };
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        BiMap<Position, Cell> cells = this.board.getCells();
        Cell cell = this.piece.getCell();
        for (Directions direction : getDirections(directions)) {
            Cell newCell = board.getNextCell(cell, direction);
            if (cells.containsReverse(newCell)) {
                if (newCell.isEmpty() || newCell.getPiece().getColor() != this.piece.getColor()) {
                    validCells.add(newCell);
                }
            }
        }
        return validCells;
    }

    private static Directions[] getDirections(Directions[] directions) {
        return directions;
    }
}

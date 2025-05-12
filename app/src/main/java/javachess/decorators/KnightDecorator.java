package javachess.decorators;

import javachess.*;

import java.util.ArrayList;

public class KnightDecorator extends PieceDecorator {

    private final PieceDecorator pieceDecorator;

    public KnightDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
    }

    /**
     * Get the valid cells for the knight.
     * @return An ArrayList of valid cells for the knight.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        Directions[] directions = {
            Directions.TWO_LEFT_ONE_UP,
            Directions.TWO_LEFT_ONE_DOWN,
            Directions.TWO_RIGHT_ONE_UP,
            Directions.TWO_RIGHT_ONE_DOWN,
            Directions.ONE_LEFT_TWO_UP,
            Directions.ONE_LEFT_TWO_DOWN,
            Directions.ONE_RIGHT_TWO_UP,
            Directions.ONE_RIGHT_TWO_DOWN
        };
        ArrayList<Cell> validCells = new ArrayList<>();
        BiMap<Position, Cell> cells = this.board.getCells();
        Cell cell = this.piece.getCell();
        for (Directions direction : directions) {
            Cell currCell = board.getNextCell(cell, direction);
            if (cells.containsReverse(currCell)) {
                if (currCell.isEmpty() || currCell.getPiece().getColor() != this.piece.getColor()) {
                    validCells.add(currCell);
                }
            }
        }
        validCells.addAll(pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells());
        return validCells;
    }
}

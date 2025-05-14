package javachess.decorators;

import javachess.model.*;

import java.util.ArrayList;

public abstract class PieceDecorator {
    public ArrayList<Cell> getValidCells(){
        return null;
    }
    protected Board board;
    protected Piece piece;

    /**
     * Get the valid cells for the piece. Only works for infinite range pieces.
     * @return An ArrayList of valid cells for the piece.
     */
    ArrayList<Cell> getDirectedCells(Directions[] directions, PieceDecorator currentDecorator) {
        ArrayList<Cell> validCells = new ArrayList<>();
        BiMap<Position, Cell> cells = currentDecorator.board.getCells();
        Cell cell = currentDecorator.piece.getCell();
        for (Directions direction : directions) {
            Cell curr = board.getNextCell(cell, direction);
            while (curr != null) {
                if (curr.isEmpty()) {
                    validCells.add(curr);
                } else {
                    if (curr.getPiece().getColor() != currentDecorator.piece.getColor()) {
                        validCells.add(curr);
                    }
                    break;
                }
                curr = board.getNextCell(curr, direction);
            }
        }
        return validCells;
    }

    /**
     * Check if the next cell is valid and add it to the list of valid cells.
     * @param cell The current cell.
     * @param nextCellPos The position of the cell we want to check.
     * @param cells The map of cells.
     * @param validCells The list of valid cells.
     */
    protected void checkValidCell(Cell cell, Position nextCellPos, BiMap<Position, Cell> cells, ArrayList<Cell> validCells) {
        if (cells.contains(nextCellPos)) {
            Cell nextCell = cells.get(nextCellPos);
            if (!nextCell.isEmpty() && nextCell.getPiece().getColor() != cell.getPiece().getColor()) {
                validCells.add(nextCell);
            }
        }
    }


}

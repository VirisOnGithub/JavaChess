package javachess.decorators;

import javachess.*;

import java.util.ArrayList;

public abstract class PieceDecorator {
    public ArrayList<Cell> getValidCells(){
        return null;
    }
    protected Board board;
    protected Piece piece;

    ArrayList<Cell> getDirectedCells(Directions[] directions, PieceDecorator currentDecorator) {
        ArrayList<Cell> validCells = new ArrayList<>();
        BiMap<Position, Cell> cells = currentDecorator.board.getCells();
        Cell cell = currentDecorator.piece.getCell();
        Position position = cells.getReverse(cell);
        for (Directions direction : directions) {
            Position curr = new Position(position.getX() + direction.dx, position.getY() + direction.dy);
            int x = position.getX();
            int y = position.getY();
            while (cells.contains(curr)) {
                Cell nextCell = cells.get(curr);
                if (nextCell.isEmpty()) {
                    validCells.add(nextCell);
                } else {
                    if (nextCell.getPiece().getColor() != currentDecorator.piece.getColor()) {
                        validCells.add(nextCell);
                    }
                    break;
                }
                x += direction.dx;
                y += direction.dy;
                curr.set(x, y);
            }
        }
        return validCells;
    }

    protected void checkValidCell(Cell cell, Position nextCellPos, BiMap<Position, Cell> cells, ArrayList<Cell> validCells) {
        if (cells.contains(nextCellPos)) {
            Cell nextCell = cells.get(nextCellPos);
            if (!nextCell.isEmpty() && nextCell.getPiece().getColor() != cell.getPiece().getColor()) {
                validCells.add(nextCell);
            }
        }
    }


}

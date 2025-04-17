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
        Position position = cells.getReverse(cell);
        int x = position.getX();
        int y = position.getY();
        for (Directions direction : directions) {
            Position newPosition = new Position(x + direction.dx, y + direction.dy);
            if (cells.contains(newPosition)) {
                Cell newCell = cells.get(newPosition);
                if (newCell.isEmpty() || newCell.getPiece().getColor() != this.piece.getColor()) {
                    validCells.add(newCell);
                }
            }
        }
        return validCells;
    }
}

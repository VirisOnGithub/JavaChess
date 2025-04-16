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
        Position position = cells.getReverse(cell);
        for (Directions direction : directions) {
            Position curr = new Position(position.getX() + direction.dx, position.getY() + direction.dy);
            if (cells.contains(curr)) {
                Cell nextCell = cells.get(curr);
                if (nextCell.isEmpty() || nextCell.getPiece().getColor() != this.piece.getColor()) {
                    validCells.add(nextCell);
                }
            }
        }
        validCells.addAll(pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells());
        return validCells;
    }
}

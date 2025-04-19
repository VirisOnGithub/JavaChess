package javachess.decorators;

import javachess.*;
import javachess.PieceType;

import java.util.ArrayList;

public class RoqueDecorator extends PieceDecorator{
    private final PieceDecorator pieceDecorator;
    private final PieceType roqueWith;

    public RoqueDecorator(Piece piece, Board board, PieceDecorator pieceDecorator, PieceType roqueWith) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
        this.roqueWith = roqueWith;
    }

    public RoqueDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this(piece, board, pieceDecorator, PieceType.ROOK);
    }

    @Override
    public ArrayList<Cell> getValidCells() {
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        // Check if the king moved (no roque possible)
        if(piece.hasMoved()){
            return validCells;
        }
        BiMap<Position, Cell> cells = board.getCells();
        Cell pieceCell = piece.getCell();
        Position currentPosition = cells.getReverse(pieceCell);

        for (Directions direction : new Directions[]{Directions.LEFT, Directions.RIGHT}) {
            Position tempPosition = new Position(currentPosition.getX(), currentPosition.getY()); // clone
            while (cells.contains(tempPosition)) {
                tempPosition.add(direction);
                Cell actualCell = cells.get(tempPosition);
                if (actualCell == null) {
                    break;
                }
                Piece actualPiece = actualCell.getPiece();
                if (actualPiece != null && actualPiece.getType() == roqueWith
                    && actualPiece.getColor() == piece.getColor() && !piece.hasMoved()) {
                    // Select the correct position to move the king
                    tempPosition.add(direction == Directions.LEFT ? Directions.RIGHT : Directions.LEFT);
                    validCells.add(cells.get(tempPosition));                }
                if (!actualCell.isEmpty()) {
                    break;
                }
            }
        }
        return validCells;
    }
}

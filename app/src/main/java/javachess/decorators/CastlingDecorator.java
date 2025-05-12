package javachess.decorators;

import javachess.*;
import javachess.PieceType;

import java.util.ArrayList;

public class CastlingDecorator extends PieceDecorator{
    private final PieceDecorator pieceDecorator;
    private final PieceType castleWith;

    public CastlingDecorator(Piece piece, Board board, PieceDecorator pieceDecorator, PieceType castleWith) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
        this.castleWith = castleWith;
    }

    public CastlingDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this(piece, board, pieceDecorator, PieceType.ROOK);
    }

    /**
     * Get the valid cells for the castling move (king).
     * @return An ArrayList of valid cells for the castling move.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        // Check if the king already moved (no castling possible)
        if(piece.hasMoved()){
            return validCells;
        }
        BiMap<Position, Cell> cells = board.getCells();
        Cell pieceCell = piece.getCell();

        for (Directions direction : new Directions[]{Directions.LEFT, Directions.RIGHT}) {
            Cell currCell = board.getNextCell(pieceCell, direction);
            while (cells.containsReverse(currCell)) {
                // Check if the cell exists
                if (currCell == null) {
                    break;
                }
                Piece actualPiece = currCell.getPiece();
                if (actualPiece != null && actualPiece.getType() == castleWith
                    && actualPiece.getColor() == piece.getColor() && !piece.hasMoved() && !actualPiece.hasMoved()) {
                    // Select the correct position to move the king
                    Cell tempCell = board.getNextCell(board.getNextCell(pieceCell, direction), direction); // the king moves 2 times
                    validCells.add(tempCell);
                    break;
                }
                // Check if the cell is empty
                if (!currCell.isEmpty()) {
                    break;
                }
                currCell = board.getNextCell(currCell, direction);
            }
        }
        return validCells;
    }
}

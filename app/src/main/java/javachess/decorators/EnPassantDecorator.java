package javachess.decorators;

import javachess.*;
import javachess.pieces.Pawn;

import java.util.ArrayList;

public class EnPassantDecorator extends PieceDecorator {
    private final PieceDecorator pieceDecorator;

    public EnPassantDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.piece = piece;
        this.board = board;
        this.pieceDecorator = pieceDecorator;
    }

    @Override
    public ArrayList<Cell> getValidCells() {
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        BiMap<Position, Cell> cells = this.board.getCells();
        Position piecePosition = cells.getReverse(this.piece.getCell());

        Move lastMove = this.board.getLastMove();
        if(lastMove != null && cells.get(lastMove.getTo()).getPiece() instanceof Pawn){
            int lastMoveFromX = lastMove.getFrom().getY();
            int lastMoveToY = lastMove.getTo().getY();
            int lastMoveToX = lastMove.getTo().getX();

            if(Math.abs(lastMoveFromX - lastMoveToY) == 2){
                if(lastMoveToY == piecePosition.getY() && Math.abs(lastMoveToX - piecePosition.getX()) == 1){
                    Position enPassantPosition = lastMove.getMiddlePosition();
                    if(cells.contains(enPassantPosition)){
                        Cell enPassantCell = cells.get(enPassantPosition);
                        if(enPassantCell.isEmpty()){
                            validCells.add(enPassantCell);
                        }
                    }
                }
            }
        }

        return validCells;
    }
}

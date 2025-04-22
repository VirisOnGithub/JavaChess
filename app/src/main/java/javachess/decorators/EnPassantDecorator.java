package javachess.decorators;

import javachess.Board;
import javachess.Cell;
import javachess.Piece;

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
        return new ArrayList<>();
    }
}

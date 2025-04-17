package javachess.decorators;

import javachess.*;
import javachess.PieceColor;
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

    @Override
    public ArrayList<Cell> getValidCells() {
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        if (!piece.hasMoved()) {

        }
        return validCells;
    }
}

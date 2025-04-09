package javachess.decorators;

import javachess.Cell;

import java.util.ArrayList;

public class RookDecorator implements PieceDecorator {
    private final PieceDecorator pieceDecorator;

    public RookDecorator(PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
    }

    @Override
    public ArrayList<Cell> getValidCells() {
        ArrayList<Cell> validCells = pieceDecorator.getValidCells();

        return validCells;
    }
}

package javachess.decorators;

import javachess.model.*;

import java.util.ArrayList;

public class PawnDecorator extends PieceDecorator {
    private final PieceDecorator pieceDecorator;

    public PawnDecorator(Piece piece, Board board, PieceDecorator pieceDecorator) {
        this.pieceDecorator = pieceDecorator;
        this.piece = piece;
        this.board = board;
    }


    /**
     * Get the valid cells for the pawn.
     * @return An ArrayList of valid cells for the pawn.
     */
    @Override
    public ArrayList<Cell> getValidCells() {
        boolean isWhite = this.piece.getColor() == PieceColor.WHITE;
        Directions move = isWhite ? Directions.TOP : Directions.BOTTOM;
        // Define the attack directions for the pawn (diagonal moves in which it should take a piece)
        Directions[] attack = isWhite
                ? new Directions[]{Directions.TOP_LEFT, Directions.TOP_RIGHT}
                : new Directions[]{Directions.BOTTOM_LEFT, Directions.BOTTOM_RIGHT};
        ArrayList<Cell> validCells = pieceDecorator == null ? new ArrayList<>() : pieceDecorator.getValidCells();
        BiMap<Position, Cell> cells = this.board.getCells();
        Cell cell = this.piece.getCell();
        Position position = cells.getReverse(cell);
        int x = position.getX();
        int y = position.getY();
        Position forward = new Position(x + move.dx, y + move.dy);
        Position left = new Position(x + attack[0].dx, y + attack[0].dy);
        Position right = new Position(x + attack[1].dx, y + attack[1].dy);
        if (cells.contains(forward)) {
            Cell forwardCell = cells.get(forward);
            if (forwardCell.isEmpty()) {
                validCells.add(forwardCell);
                if (!this.piece.hasMoved()) {
                    Position doubleForward = new Position(x + move.dx * 2, y + move.dy * 2);
                    if (cells.contains(doubleForward)) {
                        Cell doubleForwardCell = cells.get(doubleForward);
                        if (doubleForwardCell.isEmpty()) {
                            validCells.add(doubleForwardCell);
                        }
                    }
                }
            }
        }
        // Check if the pawn can attack a piece
        checkValidCell(cell, left, cells, validCells);
        checkValidCell(cell, right, cells, validCells);
        return validCells;
    }
}

package javachess;

import javachess.decorators.PieceDecorator;

public abstract class Piece {
    private final PieceColor color;
    private Cell cell;
    private boolean moved = false;
    protected PieceDecorator decorator;

    public Piece(PieceColor color, Cell cell) {
        this.color = color;
        this.cell = cell;
    }

    public Piece(PieceColor color){
        this.color = color;
        this.cell = null;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved() {
        this.moved = true;
    }

    public Cell getCell() {
        return cell;
    }

    public abstract PieceType getType();

    public PieceDecorator getDecorator() {
        return decorator;
    }

    public String findFile() {
        String filename = "";
        switch (color) {
            case WHITE:
                filename += "white";
                break;
            case BLACK:
                filename += "black";
                break;
        }
        filename += "-";
        switch (getType()) {
            case PAWN:
                filename += "pawn";
                break;
            case ROOK:
                filename += "rook";
                break;
            case KNIGHT:
                filename += "knight";
                break;
            case BISHOP:
                filename += "bishop";
                break;
            case QUEEN:
                filename += "queen";
                break;
            case KING:
                filename += "king";
                break;
        }
        filename += ".png";
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece piece)) return false;
        return piece.getType() == getType() && color == piece.color;
    }

    @Override
    public int hashCode() {
        return color.ordinal() * 10 + getType().ordinal();
    }

    @Override
    public String toString() {
        String type;
        switch (getType()) {
            case PAWN -> type = "P";
            case ROOK -> type = "R";
            case KNIGHT -> type = "N";
            case BISHOP -> type = "B";
            case QUEEN -> type = "Q";
            case KING -> type = "K";
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        }
        return "Piece{" +
                "type=" + type +
                "; color=" + color +
                '}';
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}

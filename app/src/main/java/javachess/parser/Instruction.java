package javachess.parser;

import javachess.*;

public class Instruction {
    private final PieceColor pieceColor;
    private final PieceType pieceType;
    private final Position to;
    private final boolean isCapture;
    private final boolean isRoque;
    private final boolean isCheck;
    private final boolean isCheckMate;
    private final Character ambiguity;
    private PieceType promoteTo;

    public Instruction(PieceColor pieceColor, PieceType pieceType, Position position, boolean isCapture, boolean isRoque, boolean isCheck, boolean isCheckMate, Character ambiguity) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        to = position;
        this.isCapture = isCapture;
        this.isRoque = isRoque;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
        this.ambiguity = ambiguity;
        this.promoteTo = null;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Position getTo() {
        return to;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public boolean isRoque() {
        return isRoque;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public Character getAmbiguity() {
        return ambiguity;
    }

    public PieceType getPromoteTo() {
        return promoteTo;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "pieceColor=" + pieceColor +
                "pieceType=" + pieceType +
                ", to=" + to +
                ", isCapture=" + isCapture +
                ", isRoque=" + isRoque +
                ", isCheck=" + isCheck +
                ", isCheckMate=" + isCheckMate +
                ", ambiguity=" + ambiguity +
                ", promoteTo=" + promoteTo +
                '}';
    }

    public void setPromoteTo(PieceType promoteTo) {
        this.promoteTo = promoteTo;
    }
}

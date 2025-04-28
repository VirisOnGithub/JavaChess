package javachess.parser;

import javachess.PieceColor;

public class CastlingInstruction implements Instruction {
    private final boolean isLongCastling;
    private final PieceColor pieceColor;

    public CastlingInstruction(boolean isLongCastling, PieceColor pieceColor) {
        this.isLongCastling = isLongCastling;
        this.pieceColor = pieceColor;
    }

    public boolean isLongCastling() {
        return isLongCastling;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }
}

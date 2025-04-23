package javachess.parser;

public class CastlingInstruction implements Instruction {
    boolean isLongCastling;

    public CastlingInstruction(boolean isLongCastling) {
        this.isLongCastling = isLongCastling;
    }
}

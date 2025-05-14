package javachess.parser;

import javachess.model.PieceColor;

/**
 * Instruction that indicates a castling move.
 */
public record CastlingInstruction(boolean isLongCastling, PieceColor pieceColor) implements Instruction {
}

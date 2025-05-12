package javachess;

import javachess.parser.CastlingInstruction;
import javachess.parser.Instruction;
import javachess.parser.RegularInstruction;

import java.util.ArrayList;

public class MoveInitializer {
    private final ArrayList<Instruction> instructions;
    private final Board board;

    public MoveInitializer(ArrayList<Instruction> instructions, Board board) {
        this.instructions = instructions;
        this.board = board;
    }

    public Board initialize() {
        for (Instruction instruction : instructions) {
            Move correspondingMove = createMove(instruction);

        }
        return board;
    }

    private Move createMove(Instruction instruction) {
        if(instruction instanceof CastlingInstruction(boolean isLongCastling, PieceColor pieceColor)){
            int side = pieceColor == PieceColor.WHITE ? 0 : 7;
            Position kingPos = new Position(side, 4);
            Position rookPos = new Position(side, isLongCastling ? 0 : 7);
            return new Move(kingPos, rookPos);
        }
        RegularInstruction regularInstruction = (RegularInstruction) instruction;
        ArrayList<Position> possiblesFrom = board.getPieceOriginFromMove(regularInstruction.getTo(), regularInstruction.getPieceColor(), regularInstruction.getPieceType());
        switch (possiblesFrom.size()){
            case 0:
                System.err.println("Invalid move: " + regularInstruction);
                return null;
            case 1:
                return new Move(possiblesFrom.getFirst(), regularInstruction.getTo());
            default:
                char ambiguity = regularInstruction.getAmbiguity();
                int ambiguityColumn = ambiguity - 'a';
                for (Position possibleFrom : possiblesFrom) {
                    if (possibleFrom.getY() == ambiguityColumn) {
                        return new Move(possibleFrom, regularInstruction.getTo());
                    }
                }
                System.err.println("Invalid move (ambiguity not resolved) : " + regularInstruction);
                return null;
        }
    }
}

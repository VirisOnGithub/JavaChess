package javachess;

import javachess.decorators.Directions;
import javachess.parser.CastlingInstruction;
import javachess.parser.Instruction;
import javachess.parser.RegularInstruction;
import javachess.pieces.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class Board {
    private final BiMap<Position, Cell> cells;
    private Move lastMove;

    public Board(int size) {
        cells = new BiMap<>();
        setInitialPieces();
        lastMove = null;
    }

    public Board(){
        this(8);
    }

    public BiMap<Position, Cell> getCells() {
        return cells;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setInitialPieces(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(j == 0 || j == 1 || j == 6 || j == 7){
                    Piece piece = null;
                    Cell currentCell = new Cell(this);
                    if(j == 0 || j == 7){
                        PieceColor pieceColor = j == 0 ? PieceColor.BLACK : PieceColor.WHITE;
                        switch (i) {
                            case 0, 7 -> piece = new Rook(pieceColor, currentCell);
                            case 1, 6 -> piece = new Knight(pieceColor, currentCell);
                            case 2, 5 -> piece = new Bishop(pieceColor, currentCell);
                            case 3 -> piece = new Queen(pieceColor, currentCell);
                            case 4 -> piece = new King(pieceColor, currentCell);
                        }
                    } else {
                        piece = new Pawn(j == 1 ? PieceColor.BLACK : PieceColor.WHITE, currentCell);
                    }
                    currentCell.setPiece(piece);
                    cells.put(new Position(i, j), currentCell);
                } else {
                    cells.put(new Position(i, j), new Cell(this));
                }
            }
        }
    }

    void applyMove(Move move, boolean roque){
        Cell fromCell = cells.get(move.getFrom());
        Cell toCell = cells.get(move.getTo());
        if(getValidCellsForBoard(fromCell.getPiece()).contains(toCell) || roque){
            Piece piece = fromCell.getPiece();
            fromCell.setPiece(null);
            toCell.setPiece(piece);
            piece.setMoved();
            lastMove = move;
//            System.out.println(getIdString());
        } else {
            System.err.println("Invalid move");
        }
    }

    boolean isCheck(PieceColor color){
        // find the king
        for (Cell cell : cells.reverseKeySet()) {
            Piece piece = cell.getPiece();
            if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                // check if any opponent piece can attack the king
                for (Cell opponentCell : cells.reverseKeySet()) {
                    Piece opponentPiece = opponentCell.getPiece();
                    if (opponentPiece != null && opponentPiece.getColor() != color) {
                        if (opponentPiece.getDecorator().getValidCells().contains(cell)) {
                            return true; // King is in check
                        }
                    }
                }
                return false;
            }
        }
        return false; // King not found (should not happen)
    }

    public boolean cannotMoveWithoutMate(PieceColor color) {
        for (Cell from : cells.reverseKeySet()) {
            Piece piece = from.getPiece();
            Position fromPos = cells.getReverse(from);
            if (piece != null && piece.getColor() == color) {
                ArrayList<Cell> destinations = piece.getDecorator().getValidCells();

                for (Cell to : destinations) {
                    Piece captured = to.getPiece();

                    to.setPiece(piece);
                    from.setPiece(null);

                    boolean inCheck = isCheck(color);

                    from.setPiece(piece);
                    to.setPiece(captured);

                    if (!inCheck) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    ArrayList<Cell> getValidCellsForBoard(Piece piece) {
        PieceColor color = piece.getColor();
        Cell from = piece.getCell();
        ArrayList<Cell> validCells = new ArrayList<>();
        for (Cell to : piece.getDecorator().getValidCells()) {
            Piece captured = to.getPiece();

            to.setPiece(piece);
            from.setPiece(null);

            boolean inCheck = isCheck(color);

            from.setPiece(piece);
            to.setPiece(captured);

            if (!inCheck) {
                validCells.add(to);
            }
        }
        return validCells;
    }

    public Cell getNextCell(Cell currentCell, Directions direction){
        Position currentPosition = cells.getReverse(currentCell);
        Position nextPosition = new Position(currentPosition.getX() + direction.dx, currentPosition.getY() + direction.dy);
        return cells.get(nextPosition);
    }


    public void log() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = cells.get(new Position(i, j)).getPiece();
                if(piece != null){
                    switch (piece.getType()){
                        case PAWN -> System.out.print("P ");
                        case ROOK -> System.out.print("R ");
                        case KNIGHT -> System.out.print("N ");
                        case BISHOP -> System.out.print("B ");
                        case QUEEN -> System.out.print("Q ");
                        case KING -> System.out.print("K ");
                    }
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    public String getIdString() {
        HashSet<Position> cellsSet = new HashSet<>(cells.keySet());
        // SOrt the cell to always have the same order
        TreeSet<Position> sortedCells = new TreeSet<>((pos1, pos2) -> {
            if (pos1.getX() != pos2.getX()) {
                return Integer.compare(pos1.getX(), pos2.getX());
            }
            return Integer.compare(pos1.getY(), pos2.getY());
        });
        sortedCells.addAll(cellsSet);
        StringBuilder idString = new StringBuilder();
        for (Position position : sortedCells) {
            Cell cell = cells.get(position);
            Piece piece = cell.getPiece();
            if (piece != null) {
                // Piece has a custom unique hashCode for each piece
                idString.append(piece.hashCode());
            } else {
                idString.append("X");
            }
        }
        return idString.toString();
    }

    public ArrayList<Position> getPieceOriginFromMove(Position to, PieceColor pieceColor, PieceType pieceType) {
        ArrayList<Position> pieceOrigins = new ArrayList<>();
        Cell toCell = cells.get(to);
        for(Cell cell : cells.reverseKeySet()){
            if(cell.getPiece().getColor() == pieceColor && cell.getPiece().getType() == pieceType && cell.getPiece().getDecorator().getValidCells().contains(toCell)){
                pieceOrigins.add(cells.getReverse(cell));
            }
        }
        return pieceOrigins;
    }

    public Position findPieceByItsFinalPosition(RegularInstruction instruction) {
        ArrayList<Position> pieceOrigins = new ArrayList<>();
        Position to = instruction.getTo();
        Cell toCell = cells.get(to);
        for (Cell cell : cells.reverseKeySet()) {
            Piece piece = cell.getPiece();
            if(piece.getColor() == instruction.getPieceColor() && piece.getType() == instruction.getPieceType() && piece.getDecorator().getValidCells().contains(toCell)){
                pieceOrigins.add(cells.getReverse(cell));
            }
        }
        if(pieceOrigins.isEmpty()){
           return null;
        }
        if(pieceOrigins.size() == 1){
            return pieceOrigins.getFirst();
        }
        char ambiguity = instruction.getAmbiguity();
        int fromX = ambiguity - 'a';
        for(Position position : pieceOrigins){
            if (position.getX() == fromX) {
                return position;
            }
        }
        return null;
    }

    public ArrayList<Move> getMovesFromInstructions (ArrayList<Instruction> instructions) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Instruction instruction : instructions) {
            if(instruction instanceof CastlingInstruction ci) {
                boolean isWhite = ci.getPieceColor() == PieceColor.WHITE;
                int piecesY = isWhite ? 0 : 7;
                Position kingPosition = new Position(4, piecesY);
                Position rookPosition = new Position(ci.isLongCastling() ? 0 : 7, piecesY);
                moves.add(new Move(kingPosition, rookPosition));
            } else {
                RegularInstruction ri = (RegularInstruction) instruction;
                Position fromPos = findPieceByItsFinalPosition(ri);
                if (fromPos != null) {
                    moves.add(new Move(fromPos, ri.getTo()));
                } else {
                    System.err.println("Error: invalid regular instruction, " + ri);
                }
            }
        }
        return moves;
    }
}

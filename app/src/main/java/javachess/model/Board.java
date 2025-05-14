package javachess.model;

import javachess.decorators.Directions;
import javachess.parser.CastlingInstruction;
import javachess.parser.Instruction;
import javachess.parser.RegularInstruction;
import javachess.pieces.*;

import java.util.*;

import static java.lang.System.exit;

/**
 * Class representing a chess board.
 * The board is an 8x8 grid of cells, each of which can contain a piece.
 * The board keeps track of the last move made and provides methods to manipulate the pieces on the board.
 */
public class Board {
    private final BiMap<Position, Cell> cells;
    private Move lastMove;
    boolean isEnPassantPossible;
    int moveCounter = 1;

    public Board() {
        cells = new BiMap<>();
        setInitialPieces();
        lastMove = null;
    }

    public BiMap<Position, Cell> getCells() {
        return cells;
    }

    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Sets the initial pieces on the board.
     * The pieces are placed in their starting positions according to the rules of chess.
     */
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

    /**
     * Applies a move to the board.
     *
     * @param move the move to be applied
     * @param roque whether the move is a castling move
     */
    void applyMove(Move move, boolean roque){
        Cell fromCell = cells.get(move.from());
        Cell toCell = cells.get(move.to());
        if(getValidCellsForBoard(fromCell.getPiece()).contains(toCell) || roque){
            Piece piece = fromCell.getPiece();
            fromCell.setPiece(null);
            toCell.setPiece(piece);
            piece.setMoved();
            lastMove = move;
            isEnPassantPossible = Math.abs(move.to().getY() - move.from().getY()) == 2 && piece.getType() == PieceType.PAWN;
            if(piece.getColor() == PieceColor.BLACK){
                moveCounter++;
            }
        } else {
            System.err.println("Invalid move");
        }
    }

    /**
     * Checks if the king of the given color is in check.
     *
     * @param color the color of the king to check
     * @return true if the king is in check, false otherwise
     */
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

    /**
     * Checks if the king of the given color is in checkmate.
     * it simulates all the possible moves of the pieces of the given color
     *
     * @param color the color of the king to check
     * @return true if the king is in checkmate, false otherwise
     */
    public boolean cannotMoveWithoutMate(PieceColor color) {
        for (Cell from : cells.reverseKeySet()) {
            Piece piece = from.getPiece();
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

    /**
     * Gets all the possible cells for a piece to move to, (excluding the cells that would put the king in check).
     * @param piece the piece to check
     * @return a list of valid cells for the piece to move to
     */
    public ArrayList<Cell> getValidCellsForBoard(Piece piece) {
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

    /**
     * Gets the next cell in the given direction from the current cell.
     * Useful for decorators
     *
     * @param currentCell the current cell
     * @param direction   the direction to move in
     * @return the next cell in the given direction
     */
    public Cell getNextCell(Cell currentCell, Directions direction){
        Position currentPosition = cells.getReverse(currentCell);
        Position nextPosition = new Position(currentPosition.getX() + direction.dx, currentPosition.getY() + direction.dy);
        return cells.get(nextPosition);
    }


//    public void log() {
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                Piece piece = cells.get(new Position(j, i)).getPiece();
//                if(piece != null){
//                    switch (piece.getType()){
//                        case PAWN -> System.out.print("P ");
//                        case ROOK -> System.out.print("R ");
//                        case KNIGHT -> System.out.print("N ");
//                        case BISHOP -> System.out.print("B ");
//                        case QUEEN -> System.out.print("Q ");
//                        case KING -> System.out.print("K ");
//                    }
//                } else {
//                    System.out.print("X ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println("\n");
//    }

    /**
     * Gets the ID string of the board.
     * The ID string is a unique representation of the board state.
     * It is used by the threefold repetition rule to check if the same position has occurred before.
     *
     * @return the ID string of the board
     */
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

    /**
     * Gets the origin positions of pieces that can move to the given position.
     * This is used to parse moves from PGN files.
     *
     * @param to        the target position
     * @param pieceColor the color of the piece
     * @param pieceType  the type of the piece
     * @return a list of positions from which a piece can move to the target position
     */
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
            if(piece != null && piece.getColor() == instruction.getPieceColor() && piece.getType() == instruction.getPieceType() && piece.getDecorator().getValidCells().contains(toCell)) {
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

    /**
     * Converts an instruction to a move.
     * This is used to parse moves from PGN files.
     *
     * @param instruction the instruction to convert
     * @return the move corresponding to the instruction
     */
    public Move getMoveFromInstructions (Instruction instruction) {
        if(instruction instanceof CastlingInstruction(boolean isLongCastling, PieceColor pieceColor)) {
            boolean isWhite = pieceColor == PieceColor.WHITE;
            int piecesY = isWhite ? 7 : 0;
            Position kingPosition = new Position(4, piecesY);
            Position rookPosition = new Position(isLongCastling ? 2 : 6, piecesY);
            return new Move(kingPosition, rookPosition);
        } else {
            RegularInstruction ri = (RegularInstruction) instruction;
            Position fromPos = findPieceByItsFinalPosition(ri);
            if (fromPos != null) {
                return new Move(fromPos, ri.getTo());
            } else {
                System.err.println("Error: invalid regular instruction, " + ri);
                exit(0);
                return null;
            }
        }
    }

    public String getSimplifiedFEN(PieceColor color) {
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int emptyCount = 0;
            for (int j = 0; j < 8; j++) {
                Cell cell = cells.get(new Position(j, i));
                Piece piece = cell.getPiece();
                if (piece != null) {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece.getFEN());
                } else {
                    emptyCount++;
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (i < 7) {
                fen.append("/");
            }
        }
        fen.append(" ").append(color == PieceColor.BLACK ? "b" : "w");
        fen.append(" ").append(getCastlingRights());
        fen.append(" ").append(isEnPassantPossible ? lastMove.getMiddlePosition().asPGN() : "-");
        return fen.toString();
    }

    /**
     * Converts the current state of the board to a FEN string.
     * see <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">here</a>
     *
     * @param fiftyMoveRuleCounter the counter for the fifty-move rule
     * @param color                the color of the player to move
     * @return the FEN string representing the current state of the board
     */
    public String getFEN(int fiftyMoveRuleCounter, PieceColor color) {
        return getSimplifiedFEN(color) + " " + fiftyMoveRuleCounter +
                " " + moveCounter;
    }

    /**
     * Gets the current state of castling possibilities on the board.
     * @return a string representing the castling rights
     */
    private String getCastlingRights() {
        StringBuilder castlingRights = new StringBuilder();
        // LinkedHashMap keeps the order of insertion (see https://stackoverflow.com/questions/663374/java-ordered-map)
        Map<Position, String> castlingMap = new LinkedHashMap<>(){
            {
                put(new Position(7, 7), "K");
                put(new Position(0, 7), "Q");
                put(new Position(7, 0), "k");
                put(new Position(0, 0), "q");
            }
        };
        for (Position position : castlingMap.keySet()) {
            Cell cell = cells.get(position);
            Piece piece = cell.getPiece();
            if (piece != null && piece.getType() == PieceType.ROOK && !piece.hasMoved()) {
                castlingRights.append(castlingMap.get(position));
            }
        }
        return castlingRights.toString();
    }
}

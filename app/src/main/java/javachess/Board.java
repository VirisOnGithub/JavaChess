package javachess;

import javachess.pieces.*;

public class Board {
    private final BiMap<Position, Cell> cells;

    public Board(int size) {
        cells = new BiMap<>();
        setInitialPieces();
    }

    public Board(){
        this(8);
    }

    public BiMap<Position, Cell> getCells() {
        return cells;
    }

    public void setInitialPieces(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(i == 0 || i == 1 || i == 6 || i == 7){
                    Piece piece = null;
                    Cell currentCell = new Cell(this);
                    if(i == 0 || i == 7){
                        PieceColor pieceColor = i == 0 ? PieceColor.BLACK : PieceColor.WHITE;
                        switch (j) {
                            case 0, 7 -> piece = new Rook(pieceColor, currentCell);
                            case 1, 6 -> piece = new Knight(pieceColor, currentCell);
                            case 2, 5 -> piece = new Bishop(pieceColor, currentCell);
                            case 3 -> piece = new Queen(pieceColor, currentCell);
                            case 4 -> piece = new King(pieceColor, currentCell);
                        }
                    } else {
                        piece = new Pawn(i == 1 ? PieceColor.BLACK : PieceColor.WHITE, currentCell);
                    }
                    currentCell.setPiece(piece);
                    cells.put(new Position(i, j), currentCell);
                } else {
                    cells.put(new Position(i, j), new Cell(this));
                }
            }
        }
    }

    void applyMove(Move move){
        Cell fromCell = cells.get(move.getFrom());
        Cell toCell = cells.get(move.getTo());
        if(fromCell.getPiece().getDecorator().getValidCells().contains(toCell)){
            Piece piece = fromCell.getPiece();
            fromCell.setPiece(null);
            toCell.setPiece(piece);
            piece.setMoved();
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
}

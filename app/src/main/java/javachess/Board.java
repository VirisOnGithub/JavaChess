package javachess;

import javachess.pieces.*;

import java.awt.*;

public class Board {
    private BiMap<Position, Cell> cells;

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
        Piece piece = fromCell.getPiece();
        fromCell.setPiece(null);
        toCell.setPiece(piece);
    }
}

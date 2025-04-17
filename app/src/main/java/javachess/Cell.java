package javachess;

public class Cell {
    private Piece piece;
    private final Board board;

    public Cell(Board board) {
        this.piece = null;
        this.board = board;
    }

    public Cell(Piece piece, Board board) {
        this.piece = piece;
        this.board = board;
    }

    public Piece getPiece() {
        return piece;
    }

    public Board getBoard() {
        return board;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if(piece != null){
            piece.setCell(this);
        }
    }

    public boolean isEmpty() {
        return piece == null;
    }

    @Override
    public String toString() {
        return piece != null ? piece.toString() : "Empty";
    }
}

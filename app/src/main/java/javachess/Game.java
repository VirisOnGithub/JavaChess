package javachess;

import javachess.events.*;
import javachess.pieces.Pawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.stream.Collectors;

public class Game extends Observable {
    private final ArrayList<Player> players;
    protected Move move;
    private final Board board;
    private boolean gameDone = false;
    private int actualPlayer = 0;
    public Piece promoteTo = null;
    private final HashMap<String, Integer> history = new HashMap<>();

    public Game(){
        board = new Board();
        players = new ArrayList<>();
        Window window = new Window(this);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();
    }

    public Move getMove() {
        return move;
    }

    public Board getBoard() {
        return board;
    }

    public Player getNextPlayer() {
        return players.get(actualPlayer++ % players.size());
    }

    public Player getPreviousPlayer() {
        return players.get((actualPlayer - 1) % players.size());
    }

    public void playGame(){
        players.add(new Player(this, PieceColor.WHITE));
        players.add(new Player(this, PieceColor.BLACK));

        while(!gameDone){
            Player currentPlayer = getCurrentPlayer();
            if (board.canMoveWithoutMate(currentPlayer.getColor())) {
                notifyAll(board.isCheck(currentPlayer.getColor()) ? new CheckMateEvent(getPreviousPlayer().getColor()) : new PatEvent());
                gameDone = true;
                break;
            }
            if (board.isCheck(currentPlayer.getColor())) {
                notifyAll(new CheckEvent());
            }
            // handle positionHistory
            int getPositionHistoryNumber = getPositionHistoryNumber();
            if (getPositionHistoryNumber > 2) {
                notifyAll(new DrawEvent("The game is a draw due to the threefold repetition rule."));
                gameDone = true;
                break;
            }
            boolean successMove;
            do {
                Move move = currentPlayer.getMove();
                successMove = setMove(move.getFrom(), move.getTo(), false);
            } while (!successMove);
            actualPlayer++;
        }
    }

    private int getPositionHistoryNumber() {
        return history.compute(board.getIdString(), (key, value) -> value == null ? 1 : value + 1);
    }

    private void notifyAll(Event event){
        setChanged();
        notifyObservers(event);
    }

    public boolean setMove(Position from, Position to, boolean castling) {
//        System.out.println("Move from " + from + " to " + to);
        move = new Move(from, to);
        Cell fromCell = board.getCells().get(from);
        Cell toCell = board.getCells().get(to);
        Piece pieceFrom = fromCell.getPiece();
        Piece pieceTo = toCell.getPiece();
        if (pieceFrom == null) {
            System.err.println("Invalid move: No piece at the source position.");
            return false;
        }

        if (pieceFrom.getColor() != getCurrentPlayer().getColor()) {
            System.err.println("Invalid move: The piece does not belong to the current player.");
            return false;
        }

        if (!board.getValidCellsForBoard(fromCell.getPiece()).contains(toCell) && !castling) {
            System.out.println(String.valueOf(board.getValidCellsForBoard(fromCell.getPiece())
                    .stream()
                    .map(Object::hashCode)
                    .collect(Collectors.toCollection(ArrayList::new))));
            System.out.println("\n");
            System.out.println(toCell.hashCode());
            System.err.println("Invalid move: The destination cell is not valid for the selected piece.");
            return false;
        }

        if (pieceTo != null && pieceFrom.getColor() == pieceTo.getColor()) {
            System.err.println("Invalid move: The destination cell contains a piece of the same color.");
            return false;
        }
        board.applyMove(move, castling);
        // handle castling
        if (pieceFrom.getType() == PieceType.KING && Math.abs(from.getY() - to.getY()) > 1) {
            System.out.println("Castling");

            Position rookFrom = new Position(from.getX(), to.getY() > from.getY() ? 7 : 0);
            Position rookTo = new Position(from.getX(), to.getY() + (to.getY() > from.getY() ? -1 : 1));
            Piece rook = board.getCells().get(rookFrom).getPiece();
            if (rook != null && rook.getType() == PieceType.ROOK && rook.getColor() == pieceFrom.getColor()) {
                setMove(rookFrom, rookTo, true);
            }
        }
        // handle promotion
        if (pieceFrom.getType() == PieceType.PAWN && (to.getX() == 0 || to.getX() == 7)) {
            notifyAll(new UpdateBoardEvent()); // show the pawn reaching the end
            notifyAll(new PromotionEvent(to));
            synchronized (this) {
                try {
                    // waiting for the player to choose the piece
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Je me réveille");
            if (promoteTo == null) {
                System.err.println("Invalid promotion");
                return false;
            }
            Cell pawnCell = board.getCells().get(to);
            pawnCell.setPiece(promoteTo);
            promoteTo.setCell(pawnCell);
        }

        // handle en passant
        if (pieceFrom.getType() == PieceType.PAWN && Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 1) {
            Position enPassantPosition = new Position(from.getX(), to.getY());
            Cell enPassantCell = board.getCells().get(enPassantPosition);
            if (enPassantCell != null && enPassantCell.getPiece() instanceof Pawn) {
                Piece enPassantPiece = enPassantCell.getPiece();
                if (enPassantPiece.getColor() != pieceFrom.getColor()) {
                    enPassantCell.setPiece(null);
                }
            }
        }

        notifyAll(new UpdateBoardEvent());
        return true;
    }

    public Player getCurrentPlayer() {
        return players.get(actualPlayer % players.size());
    }
}

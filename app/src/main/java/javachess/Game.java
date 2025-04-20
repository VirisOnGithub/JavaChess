package javachess;

import javachess.events.CheckEvent;
import javachess.events.CheckMateEvent;
import javachess.events.UpdateBoardEvent;

import java.util.ArrayList;
import java.util.Observable;

public class Game extends Observable {
    private final ArrayList<Player> players;
    protected Move move;
    private final Board board;
    private boolean gameDone = false;
    private int actualPlayer = 0;

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
            if (board.isCheckMate(currentPlayer.getColor())) {
                notifyAll(new CheckMateEvent(getPreviousPlayer().getColor()));
                gameDone = true;
                break;
            }
            if (board.isCheck(currentPlayer.getColor())) {
                notifyAll(new CheckEvent());
            }
            boolean successMove;
            do {
                Move move = currentPlayer.getMove();
                successMove = setMove(move.getFrom(), move.getTo(), false);
            } while (!successMove);
            actualPlayer++;
        }
    }

    private void notifyAll(Event event){
        setChanged();
        notifyObservers(event);
    }

    public boolean setMove(Position from, Position to, boolean roque) {
        System.out.println("Move from " + from + " to " + to);
        move = new Move(from, to);
        Piece pieceFrom = board.getCells().get(from).getPiece();
        if(pieceFrom == null || pieceFrom.getColor() != getCurrentPlayer().getColor()){
            System.err.println("Invalid move, Piece: " + pieceFrom);
            return false;
        }
        board.applyMove(move, roque);
        // handle roque
        System.out.println(Math.abs(from.getY() - to.getY()));
        if (pieceFrom.getType() == PieceType.KING && Math.abs(from.getY() - to.getY()) > 1) {
            Position rookFrom = new Position(from.getX(), to.getY() + (to.getY() > from.getY() ? 1 : -1));
            Position rookTo = new Position(from.getX(), to.getY() + (to.getY() > from.getY() ? -1 : 1));
            Piece rook = board.getCells().get(rookFrom).getPiece();
            if (rook != null && rook.getType() == PieceType.ROOK && rook.getColor() == pieceFrom.getColor()) {
                setMove(rookFrom, rookTo, true);
            }
        }
        notifyAll(new UpdateBoardEvent());
        return true;
    }

    public Player getCurrentPlayer() {
        return players.get(actualPlayer % players.size());
    }
}

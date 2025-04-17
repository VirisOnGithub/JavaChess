package javachess;

import javachess.events.CheckEvent;
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

    public void playGame(){
        players.add(new Player(this, PieceColor.WHITE));
        players.add(new Player(this, PieceColor.BLACK));

        while(!gameDone){
            Player currentPlayer = getCurrentPlayer();
            if (board.isCheck(currentPlayer.getColor())) {
                notifyAll(new CheckEvent());
            }
            boolean successMove;
            do {
                Move move = currentPlayer.getMove();
                successMove = setMove(move.getFrom(), move.getTo());
            } while (!successMove);
            actualPlayer++;
        }
    }

    private void notifyAll(Event event){
        setChanged();
        notifyObservers(event);
    }

    public boolean setMove(Position from, Position to) {
        move = new Move(from, to);
        Piece pieceFrom = board.getCells().get(from).getPiece();
        if(pieceFrom == null || pieceFrom.getColor() != getCurrentPlayer().getColor()){
            System.err.println("Invalid move");
            return false;
        }
        board.applyMove(move);
        notifyAll(new UpdateBoardEvent());
        return true;
    }

    public Player getCurrentPlayer() {
        return players.get(actualPlayer % players.size());
    }
}

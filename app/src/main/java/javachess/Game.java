package javachess;

import java.util.ArrayList;
import java.util.Observable;

public class Game extends Observable {
    private final ArrayList<Player> players;
    private Move move;
    private final Board board;
    private boolean gameDone = false;
    private int actualPlayer = 0;

    public Game(){
        board = new Board();
        players = new ArrayList<>();
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
        players.add(new Player(this));
        players.add(new Player(this));

        while(!gameDone){
            Player currentPlayer = getNextPlayer();
            Move move = currentPlayer.getMove();

        }
    }

    public void setMove(Position from, Position to) {
        move = new Move(from, to);
        board.applyMove(move);
        setChanged();
        notifyObservers();
        synchronized (this){
            this.notify();
        }
    }
}

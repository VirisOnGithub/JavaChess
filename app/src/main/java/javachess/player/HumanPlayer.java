package javachess.player;

import javachess.model.Game;
import javachess.model.Move;
import javachess.model.PieceColor;
import javachess.model.Position;

/**
 * Class representing a player in the chess game.
 * Each player has a color and can make moves.
 */
public class HumanPlayer implements Player {
    final Game game;
    private final PieceColor color;

    public HumanPlayer(Game game, PieceColor color) {
        this.game = game;
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public Move getMove(){
        synchronized (game) {
            try {
                game.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return game.getMove();
    }

    public void setMove(Position from, Position to) {
        game.move = new Move(from, to);
        synchronized (game){
            game.notify();
        }
    }
}

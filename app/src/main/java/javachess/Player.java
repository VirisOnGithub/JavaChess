package javachess;

/**
 * Class representing a player in the chess game.
 * Each player has a color and can make moves.
 */
public class Player {
    final Game game;
    private final PieceColor color;

    public Player(Game game, PieceColor color) {
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

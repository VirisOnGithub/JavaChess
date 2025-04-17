package javachess;

public class Player {
    final Game game;
    private final PieceColor color;
    private Move move;

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
        this.move = new Move(from, to);
        game.move = move;
        synchronized (game){
            game.notify();
        }
    }
}

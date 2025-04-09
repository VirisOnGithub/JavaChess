package javachess;

public class Player {
    Game game;

    public Player(Game game) {
        this.game = game;
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
}

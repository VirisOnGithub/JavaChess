package javachess.player;

import javachess.Move;
import javachess.PieceColor;
import javachess.Position;

public interface Player {
    /**
     * Returns the color of the player.
     *
     * @return the color of the player
     */
    PieceColor getColor();

    Move getMove();
}

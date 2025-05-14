package javachess.player;

import javachess.model.Move;
import javachess.model.PieceColor;

public interface Player {
    /**
     * Returns the color of the player.
     *
     * @return the color of the player
     */
    PieceColor getColor();

    Move getMove();
}

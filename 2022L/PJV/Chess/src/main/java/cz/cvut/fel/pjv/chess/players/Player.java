package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.utils.Colors;

public interface Player {

    void makeMove();

    Colors getPlayerColor();
}

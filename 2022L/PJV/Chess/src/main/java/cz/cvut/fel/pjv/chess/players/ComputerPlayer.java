package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.utils.Colors;


public class ComputerPlayer implements Player{
    public boolean ongoingGame;
    private final Colors playerColor;

    public ComputerPlayer(Colors playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public Colors getPlayerColor() {
        return playerColor;
    }

    public void makeMove() {
    }

}

package cz.cvut.fel.pjv.chess.board;

public class Clock extends Board {
    //true = white is on move, false = black is on move
    boolean swap = true;

    private void WhiteTimer(){
    }

    private void BlackTimer(){
    }

    public boolean IsTimeZero() {
        return false;
    }
}

package cz.cvut.fel.pjv.chess.utils;

public class Moves {
    //class to represent a pair of numbers
    final Pair piecePosition;
    final Pair moveSquare;

    public Moves(Pair piecePosition, Pair moveSquare) {
        this.piecePosition = piecePosition;
        this.moveSquare = moveSquare;
    }

    public Pair getPiecePosition() {
        return piecePosition;
    }

    public Pair getMoveSquare() {
        return moveSquare;
    }
}

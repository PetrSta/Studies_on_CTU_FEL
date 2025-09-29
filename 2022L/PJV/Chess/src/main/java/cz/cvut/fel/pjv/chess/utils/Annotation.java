package cz.cvut.fel.pjv.chess.utils;

public enum Annotation {
    //enum to represent pieces on squares
    Pawn("P"),
    Bishop("B"),
    Knight("N"),
    Rook("R"),
    Queen("Q"),
    King("K"),
    empty("E");

    @Override
    public String toString() {
        return annotation;
    }

    private final String annotation;

    Annotation(String annotation) {
        this.annotation = annotation;
    }
}

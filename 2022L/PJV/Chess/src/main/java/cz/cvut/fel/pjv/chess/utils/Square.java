package cz.cvut.fel.pjv.chess.utils;

import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.pieces.*;

//class created to store piece instances in 2d array
public class Square {
    public Pair squareIndexes;
    public Pieces pieceInstance;

    public Square(Pair squareIndexes, Pieces pieceInstance) {
        this.squareIndexes = squareIndexes;
        this.pieceInstance = pieceInstance;
    }
}

package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.RookMovement;
import cz.cvut.fel.pjv.chess.utils.Annotation;

import java.util.List;

public class Rook implements Pieces {
    //must be false if castle is to be played
    private boolean hasMoved = false;

    private Pair position;
    private final Colors color;
    public final Annotation annotation = Annotation.Rook;

    public Rook(Pair position, Colors color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public Pair getPosition() {
        return position;
    }

    @Override
    public void updatePosition(Pair position) {
        this.position = position;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public Colors getColor() {
        return color;
    }

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }



    @Override
    public List<Pair> getValidMoves(Square[][] squareList, boolean controlCheckLogic) {
        List<Pair> validMoves = RookMovement.getRookStyleMoves(this.getPosition(), this.getColor(), squareList);
        //brute force for check logic
        if(controlCheckLogic) {
            validMoves.removeIf(move -> Board.checkIfChecked(this.getColor(), move, this.getPosition(), squareList));
        }
        return validMoves;
    }
}

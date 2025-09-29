package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Annotation;
import cz.cvut.fel.pjv.chess.utils.bishopMovement;
import cz.cvut.fel.pjv.chess.board.Board;

import java.util.List;

public class Bishop implements Pieces {

    private Pair position;
    private final Colors color;
    //will be used for game saving
    public final Annotation annotation = Annotation.Bishop;

    public Bishop(Pair position, Colors color) {
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
        List<Pair> validMoves = bishopMovement.getBishopStyleMoves(this.getPosition(), this.getColor(), squareList);
        //brute force for check logic
        if(controlCheckLogic) {
            validMoves.removeIf(move -> Board.checkIfChecked(this.getColor(), move, this.getPosition(), squareList));
        }
        return validMoves;
    }
}

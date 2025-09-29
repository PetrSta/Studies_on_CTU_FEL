package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Annotation;

import java.util.ArrayList;
import java.util.List;

public class Knight implements Pieces {

    private Pair position;
    private final Colors color;
    public final Annotation annotation = Annotation.Knight;

    public Knight(Pair position, Colors color) {
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

    //support method for getValidMoves
    private boolean testPossibleMove(Pair possibleMove, Square[][] squareList) {
        //function to check if intended move square is free or if there is opponents piece
        // in other cases move will not be added to possible moves
        Square possibleSquare = squareList[possibleMove.getX()][possibleMove.getY()];
        //check if square is empty or taking is possible
        return possibleSquare.pieceInstance == null || possibleSquare.pieceInstance.getColor() != this.getColor();
    }

    //method that simply checks if potential move would go out of chessboard
    private boolean checkBorder(Pair potentialMove) {
        return potentialMove.getX() <= 7 && potentialMove.getX() >= 0
                && potentialMove.getY() <= 7 && potentialMove.getY() >= 0;
    }

    private Pair calculateMoves(int signX, int signY, Square[][] squareList) {
        Pair tmpMove = new Pair(this.position.getX() + signX, this.position.getY() + signY);
        if(checkBorder(tmpMove)) {
            if(testPossibleMove(tmpMove, squareList)) {
                return tmpMove;
            }
        }
        return null;
    }

    @Override
    public List<Pair> getValidMoves(Square[][] squareList, boolean controlCheckLogic) {
        //initialize variables
        List<Pair> validMoves = new ArrayList<>();
        //moving knight top left
        Pair topLeft = calculateMoves(-1, 2, squareList);
        if(topLeft != null) {
            validMoves.add(topLeft);
        }
        //moving knight top right
        Pair topRight = calculateMoves(1, 2, squareList);
        if(topRight != null) {
            validMoves.add(topRight);
        }
        //moving knight left top
        Pair leftTop = calculateMoves(-2, 1, squareList);
        if(leftTop != null) {
            validMoves.add(leftTop);
        }
        //moving knight left bottom
        Pair leftBottom = calculateMoves(-2, -1, squareList);
        if(leftBottom != null) {
            validMoves.add(leftBottom);
        }
        //moving knight right top
        Pair rightTop = calculateMoves(2, 1, squareList);
        if(rightTop != null) {
            validMoves.add(rightTop);
        }
        //moving knight right bottom
        Pair rightBottom = calculateMoves(2, -1, squareList);
        if(rightBottom != null) {
            validMoves.add(rightBottom);
        }
        //moving knight bottom left
        Pair bottomLeft = calculateMoves(-1, -2, squareList);
        if(bottomLeft != null) {
            validMoves.add(bottomLeft);
        }
        //moving knight bottom right
        Pair bottomRight = calculateMoves(1, -2, squareList);
        if(bottomRight != null) {
            validMoves.add(bottomRight);
        }
        //brute force for check logic
        if(controlCheckLogic) {
            validMoves.removeIf(move -> Board.checkIfChecked(this.getColor(), move, this.getPosition(), squareList));
        }
        return validMoves;
    }
}
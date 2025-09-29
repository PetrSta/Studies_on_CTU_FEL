package cz.cvut.fel.pjv.chess.utils;

import java.util.ArrayList;
import java.util.List;

public class RookMovement {
    //support method for getValidMoves
    private static boolean testPossibleMove(Pair possibleMove, Colors pieceColor, Square[][] squareList) {
        //function to check if intended move square is free or if there is opponents piece
        // in other cases move will not be added to possible moves
        Square possibleSquare = squareList[possibleMove.getX()][possibleMove.getY()];
        //check if square is empty or taking is possible
        if(possibleSquare.pieceInstance == null) {
            return true;
        } else {
            return possibleSquare.pieceInstance.getColor() != pieceColor;
        }
    }

    //method that simply checks if potential move would go out of chessboard
    private static boolean checkBorder(Pair potentialMove) {
        return potentialMove.getX() <= 7 && potentialMove.getX() >= 0
                && potentialMove.getY() <= 7 && potentialMove.getY() >= 0;
    }

    //support method for getValidMoves to calculate possible moves
    private static List<Pair> calculateMoves(Pair piecePosition, Colors pieceColor, int signX, int signY,
                                             int limit, Square[][] squareList) {

        List<Pair> temporaryValidMoves = new ArrayList<>();
        for(int movement = 1; movement <= limit; movement++) {
            Pair potentialMove = new Pair(piecePosition.getX() + (movement * signX),
                    piecePosition.getY() + (movement * signY));
            if(checkBorder(potentialMove)) {
                if(testPossibleMove(potentialMove, pieceColor, squareList)) {
                    temporaryValidMoves.add(potentialMove);
                } if(squareList[potentialMove.getX()][potentialMove.getY()].pieceInstance != null) {
                    //piece in the way was found
                    break;
                }
            }
        }
        return temporaryValidMoves;
    }

    public static List<Pair> getRookStyleMoves(Pair piecePosition, Colors pieceColor, Square[][] squareList) {
        //moving rook right
        int rightLimit = 7 - piecePosition.getX();
        List<Pair> validMoves = calculateMoves(piecePosition, pieceColor, 1, 0, rightLimit, squareList);
        //moving rook up
        int upLimit = 7 - piecePosition.getY();
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, 0, 1, upLimit, squareList));
        //moving rook left
        int leftLimit = piecePosition.getX();
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, -1, 0, leftLimit, squareList));
        //moving rook down
        int downLimit = piecePosition.getY();
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, 0, -1, downLimit, squareList));
        return validMoves;
    }
}

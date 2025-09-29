package cz.cvut.fel.pjv.chess.utils;

import java.util.ArrayList;
import java.util.List;

public class bishopMovement {
    //support function for getBishopStyleMoves
    private static boolean testPossibleMove(Pair possibleMove, Colors pieceColor, Square[][] squareList) {
        //function to check if intended move square is free or if there is opponents piece
        //in other cases move will not be added to possible moves
        Square possibleSquare = squareList[possibleMove.getX()][possibleMove.getY()];
        //check if square is empty or taking is possible
        if(possibleSquare.pieceInstance == null) {
            return true;
        } else {
            return possibleSquare.pieceInstance.getColor() != pieceColor;
        }
    }

    //support function for getBishopStyleMoves -> checks if bishop is still in board
    private static boolean checkBorder(Pair potentialMove) {
        return potentialMove.getX() <= 7 && potentialMove.getX() >= 0
                && potentialMove.getY() <= 7 && potentialMove.getY() >= 0;
    }

    //support function for getBishopStyleMoves -> moves the bishop and returns list of valid moves before check logic
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

    //main function to get Bishop style moves
    public static List<Pair> getBishopStyleMoves(Pair piecePosition, Colors pieceColor, Square[][] squareList) {
        //find how many moves are possible for the bishop - to limit cycles
        int possibleMovesUp = 7 - piecePosition.getY();
        int possibleMovesDown = piecePosition.getY();
        int possibleMovesRight = 7 - piecePosition.getX();
        int possibleMovesLeft = piecePosition.getX();
        //moving bishop top left
        int topLeftLimit = Math.min(possibleMovesUp, possibleMovesLeft);
        List<Pair> validMoves = calculateMoves(piecePosition, pieceColor, -1, 1, topLeftLimit, squareList);
        //moving bishop top right
        int topRightLimit = Math.min(possibleMovesUp, possibleMovesRight);
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, 1, 1, topRightLimit, squareList));
        //moving bishop bottom left
        int bottomLeftLimit = Math.min(possibleMovesDown, possibleMovesLeft);
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, -1, -1, bottomLeftLimit, squareList));
        //moving bishop bottom right
        int bottomRightLimit = Math.min(possibleMovesDown, possibleMovesRight);
        validMoves.addAll(calculateMoves(piecePosition, pieceColor, 1, -1, bottomRightLimit, squareList));
        //brute force for check logic
        return validMoves;
    }
}

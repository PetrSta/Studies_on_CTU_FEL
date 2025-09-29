package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightTest {
    //setting up king for test
    Pair knightPosition = new Pair(3, 3);
    Knight knight = new Knight(knightPosition, Colors.whiteColor);
    //testing board corner bishop
    Square[][] createTestBoard() {
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //Knight
        squareList[3][3].pieceInstance = knight;
        //test pieces
        squareList[1][2].pieceInstance = new Pawn(new Pair(1, 2), Colors.whiteColor);
        squareList[1][4].pieceInstance = new Pawn(new Pair(1, 4), Colors.blackColor);
        squareList[2][1].pieceInstance = new Pawn(new Pair(2, 1), Colors.blackColor);
        squareList[2][5].pieceInstance = new Pawn(new Pair(2, 5), Colors.whiteColor);
        squareList[3][1].pieceInstance = new Pawn(new Pair(3, 1), Colors.whiteColor);
        squareList[3][2].pieceInstance = new Pawn(new Pair(3, 2), Colors.blackColor);
        squareList[3][5].pieceInstance = new Pawn(new Pair(3, 5), Colors.blackColor);
        squareList[4][1].pieceInstance = new Pawn(new Pair(4, 1), Colors.blackColor);
        squareList[4][3].pieceInstance = new Pawn(new Pair(4, 3), Colors.whiteColor);
        squareList[4][5].pieceInstance = new Pawn(new Pair(4, 5), Colors.blackColor);
        squareList[5][2].pieceInstance = new Pawn(new Pair(5, 2), Colors.whiteColor);
        squareList[5][4].pieceInstance = new Pawn(new Pair(5, 4), Colors.blackColor);

        return squareList;
    }

    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(1, 4));
        expectedMoves.add(new Pair(2, 1));
        expectedMoves.add(new Pair(4, 1));
        expectedMoves.add(new Pair(4, 5));
        expectedMoves.add(new Pair(5, 4));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = knight.getValidMoves(testBoard, false);
        //print results and test
        System.out.println("validMoves size = " + validMoves.size());
        System.out.println("expectedMoves size = " + expectedMoves.size());
        for(Pair move : validMoves) {
            System.out.println("Move Start:");
            System.out.println("x = " + move.getX());
            System.out.println("y = " + move.getY());
            System.out.println("Move end.");
        }
        assertEquals(validMoves.size(), expectedMoves.size());
        assertTrue(validMoves.containsAll(expectedMoves));
    }
}
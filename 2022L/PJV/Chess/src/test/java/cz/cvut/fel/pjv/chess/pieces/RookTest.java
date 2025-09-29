package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookTest {
    //setting up bishop for test
    Pair rookPosition = new Pair(3, 3);
    Rook rook = new Rook(rookPosition, Colors.whiteColor);
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
        //Rook
        squareList[3][3].pieceInstance = rook;
        //test pawns
        squareList[0][3].pieceInstance = new Pawn(new Pair(0, 3), Colors.blackColor);
        squareList[3][5].pieceInstance = new Pawn(new Pair(3, 5), Colors.whiteColor);
        squareList[4][3].pieceInstance = new Pawn(new Pair(4, 3), Colors.blackColor);
        return squareList;
    }

    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(0, 3));
        expectedMoves.add(new Pair(1, 3));
        expectedMoves.add(new Pair(2, 3));
        expectedMoves.add(new Pair(3, 4));
        expectedMoves.add(new Pair(3, 2));
        expectedMoves.add(new Pair(3, 1));
        expectedMoves.add(new Pair(3, 0));
        expectedMoves.add(new Pair(4, 3));

        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = rook.getValidMoves(testBoard, false);
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
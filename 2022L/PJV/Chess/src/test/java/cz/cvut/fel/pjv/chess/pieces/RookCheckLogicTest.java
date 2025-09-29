package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Square;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookCheckLogicTest {
    //setting up bishop for test
    Pair rookPosition = new Pair(6, 4);
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
        squareList[6][4].pieceInstance = rook;
        //test pieces
        squareList[3][3].pieceInstance = new King(new Pair(3, 3), Colors.whiteColor);
        squareList[4][0].pieceInstance = new Rook(new Pair(4, 0), Colors.blackColor);
        squareList[6][6].pieceInstance = new Bishop(new Pair(6, 6), Colors.blackColor);
        squareList[1][6].pieceInstance = new King(new Pair(1, 6), Colors.blackColor);
        Board.setWhiteKingPosition(new Pair(3, 3));
        Board.setBlackKingPosition(new Pair(1, 6));
        return squareList;
    }

    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(6, 6));
        expectedMoves.add(new Pair(4, 4));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = rook.getValidMoves(testBoard, true);
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
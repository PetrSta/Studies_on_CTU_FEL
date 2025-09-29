package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Square;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopCheckLogicTest {
    //setting up bishop for test
    Pair bishopPosition = new Pair(2, 4);
    Bishop bishop = new Bishop(bishopPosition, Colors.whiteColor);
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
        //bishop
        squareList[2][4].pieceInstance = bishop;
        //test pieces
        squareList[4][1].pieceInstance = new King(new Pair(4, 1), Colors.whiteColor);
        squareList[4][6].pieceInstance = new Rook(new Pair(4, 6), Colors.blackColor);
        squareList[4][7].pieceInstance = new King(new Pair(4, 7), Colors.blackColor);
        Board.setWhiteKingPosition(new Pair(4, 1));
        Board.setBlackKingPosition(new Pair(4, 7));
        return squareList;
    }

    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(4, 2));
        expectedMoves.add(new Pair(4, 6));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = bishop.getValidMoves(testBoard, true);
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
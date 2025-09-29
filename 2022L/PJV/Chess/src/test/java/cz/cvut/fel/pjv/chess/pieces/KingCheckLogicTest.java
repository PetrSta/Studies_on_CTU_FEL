package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Square;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KingCheckLogicTest {
    //setting up king for test
    Pair kingPosition = new Pair(3, 3);
    King king = new King(kingPosition, Colors.whiteColor);
    //testing board
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
        //King
        squareList[3][3].pieceInstance = king;
        //test pieces
        squareList[2][3].pieceInstance = new Pawn(new Pair(2, 3), Colors.blackColor);
        squareList[3][4].pieceInstance = new Pawn(new Pair(3, 4), Colors.blackColor);
        squareList[4][4].pieceInstance = new Pawn(new Pair(4, 4), Colors.whiteColor);
        squareList[1][0].pieceInstance = new Bishop(new Pair(1, 0), Colors.blackColor);
        squareList[0][6].pieceInstance = new Bishop(new Pair(0, 6), Colors.blackColor);
        squareList[4][7].pieceInstance = new Rook(new Pair(4, 7), Colors.blackColor);
        squareList[7][7].pieceInstance = new King(new Pair(7, 7), Colors.blackColor);
        Board.setWhiteKingPosition(new Pair(3, 3));
        Board.setBlackKingPosition(new Pair(7, 7));
        return squareList;
    }

    //king thinks he can take covered pawn and step on attacked square
    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(3, 4));
        expectedMoves.add(new Pair(2, 2));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = king.getValidMoves(testBoard, true);
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
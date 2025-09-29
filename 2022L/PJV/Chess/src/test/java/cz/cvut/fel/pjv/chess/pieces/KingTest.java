package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.board.Board.*;
import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KingTest {
    //setting up king for test
    Pair kingPosition = new Pair(3, 3);
    Pair castleKingPosition = new Pair(4, 0);
    Pair checkmateKingPosition = new Pair(4, 0);
    King king = new King(kingPosition, Colors.whiteColor);
    King castleKing = new King(castleKingPosition, Colors.whiteColor);
    King checkmateKing = new King(checkmateKingPosition, Colors.whiteColor);
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
        squareList[2][2].pieceInstance = new Pawn(new Pair(2, 2), Colors.whiteColor);
        squareList[2][3].pieceInstance = new Pawn(new Pair(2, 3), Colors.whiteColor);
        squareList[3][4].pieceInstance = new Pawn(new Pair(3, 4), Colors.whiteColor);
        return squareList;
    }

    Square[][] createCastleTestBoard() {
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
        squareList[4][0].pieceInstance = castleKing;
        //test pieces
        squareList[0][0].pieceInstance = new Rook(new Pair(0, 0), Colors.whiteColor);
        return squareList;
    }

    Square[][] createCheckmateTestBoard() {
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
        squareList[4][0].pieceInstance = checkmateKing;
        //test pieces
        squareList[0][0].pieceInstance = new Rook(new Pair(0, 0), Colors.blackColor);
        squareList[1][1].pieceInstance = new Rook(new Pair(1, 1), Colors.blackColor);
        return squareList;
    }

    @org.junit.jupiter.api.Test
    void getValidMoves() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(2, 4));
        expectedMoves.add(new Pair(3, 2));
        expectedMoves.add(new Pair(4, 4));
        expectedMoves.add(new Pair(4, 3));
        expectedMoves.add(new Pair(4, 2));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = king.getValidMoves(testBoard, false);
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

    @org.junit.jupiter.api.Test
    void castleLogic() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(3, 0));
        expectedMoves.add(new Pair(3, 1));
        expectedMoves.add(new Pair(4, 1));
        expectedMoves.add(new Pair(5, 0));
        expectedMoves.add(new Pair(5, 1));
        expectedMoves.add(new Pair(2, 0));
        //creating board and asking piece for possible moves
        Square[][] castleBoard = createCastleTestBoard();
        Board.boardToString(castleBoard);
        List<Pair> validMoves = castleKing.getValidMoves(castleBoard, true);
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

    @org.junit.jupiter.api.Test
    void checkmateLogic() {
        //creating board and asking piece for possible moves
        Square[][] checkmateBoard = createCheckmateTestBoard();
        Board checkmateBoardInstance = new Board();
        List<Pair> validMoves = castleKing.getValidMoves(checkmateBoard, true);
        //print results and test
        System.out.println("validMoves size = " + validMoves.size());
        for(Pair move : validMoves) {
            System.out.println("Move Start:");
            System.out.println("x = " + move.getX());
            System.out.println("y = " + move.getY());
            System.out.println("Move end.");
        }
        assertEquals(validMoves.size(), 0);
    }
}
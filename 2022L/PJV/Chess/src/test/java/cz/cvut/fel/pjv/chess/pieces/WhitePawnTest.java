package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhitePawnTest {
    //setting up pawn move from basic position
    Pair pawnFirstMovePosition = new Pair(3, 1);
    Pawn firstMovePawn = new Pawn(pawnFirstMovePosition, Colors.whiteColor);
    //setting up pawn for move in general situation
    Pair pawnGeneralMovePosition = new Pair(3, 3);
    Pawn generalMovePawn = new Pawn(pawnGeneralMovePosition, Colors.whiteColor);
    //setting up pawn for last row check
    Pair lastRowPawnPosition = new Pair(1, 7);
    Pawn lastRowPawn = new Pawn(lastRowPawnPosition, Colors.whiteColor);

    Square[][] createFirstMoveBoard() {
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //tested pawn
        squareList[3][1].pieceInstance = firstMovePawn;
        //test pawns
        squareList[4][2].pieceInstance = new Pawn(new Pair(4, 2), Colors.blackColor);
        squareList[4][3].pieceInstance = new Pawn(new Pair(4, 3), Colors.blackColor);
        squareList[2][2].pieceInstance = new Pawn(new Pair(2, 2), Colors.whiteColor);
        return squareList;
    }

    Square[][] createFirstMoveBoardModified() {
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //tested pawn
        squareList[3][1].pieceInstance = firstMovePawn;
        //test pawns
        squareList[3][2].pieceInstance = new Pawn(new Pair(3, 2), Colors.blackColor);
        squareList[4][2].pieceInstance = new Pawn(new Pair(4, 2), Colors.blackColor);
        squareList[4][3].pieceInstance = new Pawn(new Pair(4, 3), Colors.blackColor);
        squareList[2][2].pieceInstance = new Pawn(new Pair(2, 2), Colors.whiteColor);
        return squareList;
    }

    Square[][] createGeneralMoveBoard() {
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //tested pawn
        squareList[3][3].pieceInstance = generalMovePawn;
        //test pawns
        squareList[4][4].pieceInstance = new Pawn(new Pair(4, 4), Colors.blackColor);
        squareList[2][4].pieceInstance = new Pawn(new Pair(2, 4), Colors.whiteColor);
        return squareList;
    }

    Square[][] createLastRowBoard() {
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //tested pawn
        squareList[1][7].pieceInstance = lastRowPawn;
        return squareList;
    }

    @Test
    void getValidMovesFirstMove() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(4, 2));
        expectedMoves.add(new Pair(3, 2));
        expectedMoves.add(new Pair(3, 3));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createFirstMoveBoard();
        List<Pair> validMoves = firstMovePawn.getValidMoves(testBoard, false);
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

    @Test
    void getValidMovesFirstMoveModified() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(4, 2));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createFirstMoveBoardModified();
        List<Pair> validMoves = firstMovePawn.getValidMoves(testBoard, false);
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

    @Test
    void getValidMovesGeneralMove() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(4, 4));
        expectedMoves.add(new Pair(3, 4));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createGeneralMoveBoard();
        List<Pair> validMoves = generalMovePawn.getValidMoves(testBoard, false);
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

    @Test
    void getValidMovesLastRowMove() {
        //creating board and asking piece for possible moves
        Square[][] testBoard = createLastRowBoard();
        List<Pair> validMoves = lastRowPawn.getValidMoves(testBoard, false);
        //test
        assertEquals(0, validMoves.size());
    }
}
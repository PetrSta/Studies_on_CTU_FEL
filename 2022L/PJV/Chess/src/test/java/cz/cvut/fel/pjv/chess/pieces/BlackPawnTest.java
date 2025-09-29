package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlackPawnTest {
    //setting up pawn for move from basic position
    Pair pawnFirstMovePosition = new Pair(3, 6);
    Pawn firstMovePawn = new Pawn(pawnFirstMovePosition, Colors.blackColor);
    //setting up pawn for move in general situation
    Pair pawnGeneralMovePosition = new Pair(5, 5);
    Pawn generalMovePawn = new Pawn(pawnGeneralMovePosition, Colors.blackColor);
    //setting up pawn for last row check
    Pair lastRowPawnPosition = new Pair(1, 0);
    Pawn lastRowPawn = new Pawn(lastRowPawnPosition, Colors.blackColor);

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
        squareList[3][6].pieceInstance = firstMovePawn;
        //test pieces
        squareList[4][4].pieceInstance = new Pawn(new Pair(4, 4), Colors.whiteColor);
        squareList[4][5].pieceInstance = new Pawn(new Pair(4, 5), Colors.whiteColor);
        squareList[2][5].pieceInstance = new Pawn(new Pair(2, 5), Colors.blackColor);
        return squareList;
    }

    Square[][] createFirstMoveBoardModified() {
        Square[][] squareList = new Square[8][8];
        //empty board
        //empty board
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing the pieces
        //tested pawn
        squareList[3][6].pieceInstance = firstMovePawn;
        //test pieces
        squareList[3][5].pieceInstance = new Pawn(new Pair(3, 5), Colors.blackColor);
        squareList[4][4].pieceInstance = new Pawn(new Pair(4, 4), Colors.whiteColor);
        squareList[4][5].pieceInstance = new Pawn(new Pair(4, 5), Colors.whiteColor);
        squareList[2][5].pieceInstance = new Pawn(new Pair(2, 5), Colors.blackColor);
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
        squareList[5][5].pieceInstance = generalMovePawn;
        //test pieces
        squareList[6][4].pieceInstance = new Pawn(new Pair(6, 4), Colors.whiteColor);
        squareList[4][4].pieceInstance = new Pawn(new Pair(4, 4), Colors.blackColor);
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
        squareList[1][1].pieceInstance = lastRowPawn;
        return squareList;
    }

    @Test
    void getValidMovesFirstMove() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(3, 5));
        expectedMoves.add(new Pair(3, 4));
        expectedMoves.add(new Pair(4, 5));
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
        expectedMoves.add(new Pair(4, 5));
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
        expectedMoves.add(new Pair(6, 4));
        expectedMoves.add(new Pair(5, 4));
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
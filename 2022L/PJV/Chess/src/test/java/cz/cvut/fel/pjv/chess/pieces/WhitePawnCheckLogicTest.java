package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WhitePawnCheckLogicTest {
    //setting up pawn for testing
    Pair pawnPosition = new Pair(3, 4);
    Pawn testPawn = new Pawn(pawnPosition, Colors.whiteColor);

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
        //tested pawn
        squareList[3][4].pieceInstance = testPawn;
        //test pieces
        squareList[2][5].pieceInstance = new Knight(new Pair(2, 5), Colors.blackColor);
        squareList[4][4].pieceInstance = new King(new Pair(4, 4), Colors.whiteColor);
        squareList[4][1].pieceInstance = new King(new Pair(4, 1), Colors.blackColor);
        Board.setWhiteKingPosition(new Pair(4, 4));
        Board.setBlackKingPosition(new Pair(4, 1));
        return squareList;
    }

    @Test
    void getValidMovesFirstMove() {
        //creating list of moves that should be possible
        List<Pair> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Pair(2, 5));
        //creating board and asking piece for possible moves
        Square[][] testBoard = createTestBoard();
        List<Pair> validMoves = testPawn.getValidMoves(testBoard, true);
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
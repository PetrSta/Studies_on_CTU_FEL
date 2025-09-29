package cz.cvut.fel.pjv.chess.board;

import cz.cvut.fel.pjv.chess.pieces.*;
import cz.cvut.fel.pjv.chess.utils.*;

import java.util.*;

import static cz.cvut.fel.pjv.chess.utils.Colors.whiteColor;
import static cz.cvut.fel.pjv.chess.utils.Colors.blackColor;

public class Board {
    //position indexes are different by 1 in comparison to real chessboard

    //variables
    private static Pair blackKingPosition;
    private static Pair whiteKingPosition;
    private static Pair lastMove;
    private static List<Pieces> takenPieces = new ArrayList<>();

    public static void setBlackKingPosition(Pair blackKingPosition) {
        Board.blackKingPosition = blackKingPosition;
    }

    public static void setWhiteKingPosition(Pair whiteKingPosition) {
        Board.whiteKingPosition = whiteKingPosition;
    }

    public static Pair getBlackKingPosition() {
        return blackKingPosition;
    }

    public static Pair getWhiteKingPosition() {
        return whiteKingPosition;
    }

    public static void setLastMove(Pair lastMove) {
        Board.lastMove = lastMove;
    }

    public static Pair getLastMove() {
        return lastMove;
    }

    public static void boardToString(Square[][] board) {
        for(int y = 7; y >= 0; y--) {
            for(int x = 0; x < 8; x++) {
                if(board[x][y].pieceInstance == null) {
                    if(x == 7) {
                        System.out.print(Annotation.empty + "  " + "\n");
                    } else {
                        System.out.print(Annotation.empty + "  ");
                    }
                } else {
                    if(x == 7) {
                        System.out.printf(board[x][y].pieceInstance.getAnnotation().toString() + "\n");
                    } else {
                        System.out.printf(board[x][y].pieceInstance.getAnnotation().toString() + "  ");
                    }
                }

            }
        }
        System.out.println(" ");
    }

    public static Square[][] createStarterPosition() {
        //list designed to hold position and piece instance
        Square[][] squareList = new Square[8][8];
        //empty board
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Pair position = new Pair(x, y);
                squareList[x][y] = new Square(position, null);
            }
        }
        //placing white pieces
        squareList[0][0].pieceInstance = new Rook(new Pair(0, 0), whiteColor);
        squareList[7][0].pieceInstance = new Rook(new Pair(7, 0), whiteColor);
        squareList[1][0].pieceInstance = new Knight(new Pair(1, 0), whiteColor);
        squareList[6][0].pieceInstance = new Knight(new Pair(6, 0), whiteColor);
        squareList[2][0].pieceInstance = new Bishop(new Pair(2, 0), whiteColor);
        squareList[5][0].pieceInstance = new Bishop(new Pair(5, 0), whiteColor);
        squareList[3][0].pieceInstance = new Queen(new Pair(3, 0), whiteColor);
        squareList[4][0].pieceInstance = new King(new Pair(4, 0), whiteColor);
        whiteKingPosition = new Pair(4, 0);
        for(int i = 0; i < 8; i ++) {
            squareList[i][1].pieceInstance = new Pawn(new Pair(i, 1), whiteColor);
        }
        //placing black pieces
        squareList[0][7].pieceInstance = new Rook(new Pair(0, 7), blackColor);
        squareList[7][7].pieceInstance = new Rook(new Pair(7, 7), blackColor);
        squareList[1][7].pieceInstance = new Knight(new Pair(1, 7), blackColor);
        squareList[6][7].pieceInstance = new Knight(new Pair(6, 7), blackColor);
        squareList[2][7].pieceInstance = new Bishop(new Pair(2, 7), blackColor);
        squareList[5][7].pieceInstance = new Bishop(new Pair(5, 7), blackColor);
        squareList[3][7].pieceInstance = new Queen(new Pair(3, 7), blackColor);
        squareList[4][7].pieceInstance = new King(new Pair(4, 7), blackColor);
        blackKingPosition = new Pair(4, 7);
        for(int i = 0; i < 8; i ++) {
            squareList[i][6].pieceInstance = new Pawn(new Pair(i, 6), blackColor);
        }
        return squareList;
    }

    private static void placePiece(Pieces piece, Square[][] squareList) {
        Square placingPosition = squareList[piece.getPosition().getX()][piece.getPosition().getY()];
        placingPosition.pieceInstance = piece;
    }

    private static Square[][] movePiece (Pair pieceSquare, Pair moveSquare, Square[][] squareList) {
        Square startSquare = squareList[pieceSquare.getX()][pieceSquare.getY()];
        Square endSquare = squareList[moveSquare.getX()][moveSquare.getY()];
        Colors pieceColor = startSquare.pieceInstance.getColor();
        //store taken piece because of brute testing for check
        if(endSquare.pieceInstance != null) {
            takenPieces.add(endSquare.pieceInstance);
        }
        //move the piece
        if(startSquare.pieceInstance != null) {
            Annotation pieceAnnotation = startSquare.pieceInstance.getAnnotation();
            if(pieceAnnotation.equals(Annotation.Pawn)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new Pawn(moveSquare, pieceColor);
            } else if(pieceAnnotation.equals(Annotation.Knight)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new Knight(moveSquare, pieceColor);
            } else if(pieceAnnotation.equals(Annotation.Bishop)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new Bishop(moveSquare, pieceColor);
            } else if(pieceAnnotation.equals(Annotation.Rook)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new Rook(moveSquare, pieceColor);
                Rook rook = (Rook)endSquare.pieceInstance;
                rook.setHasMoved(true);
            } else if(pieceAnnotation.equals(Annotation.Queen)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new Queen(moveSquare, pieceColor);
            } else if(pieceAnnotation.equals(Annotation.King)) {
                startSquare.pieceInstance = null;
                endSquare.pieceInstance = new King(moveSquare, pieceColor);
                King king = (King)endSquare.pieceInstance;
                king.setHasMoved(true);
            }
        }
        return squareList;
    }

    private static Square[][] promotion(Pair pieceSquare, Pair moveSquare, Square[][] squareList) {
        //variables
        Square startSquare = squareList[pieceSquare.getX()][pieceSquare.getY()];
        Square endSquare = squareList[moveSquare.getX()][moveSquare.getY()];
        Colors pieceColor = startSquare.pieceInstance.getColor();
        //for now pick random piece -> later link to GUI input, probably will even earlier switch to console input
        Random random = new Random();
        Annotation[] pieceChoiceOption = {Annotation.Knight, Annotation.Bishop, Annotation.Rook, Annotation.Queen};
        int randomIndex = random.nextInt(pieceChoiceOption.length);
        Annotation pieceChoice = pieceChoiceOption[randomIndex];
        //depending on input promote pawn
        if(pieceChoice.equals(Annotation.Knight)) {
            startSquare.pieceInstance = null;
            endSquare.pieceInstance = new Knight(moveSquare, pieceColor);
        } else if(pieceChoice.equals(Annotation.Bishop)) {
            startSquare.pieceInstance = null;
            endSquare.pieceInstance = new Bishop(moveSquare, pieceColor);
        } else if(pieceChoice.equals(Annotation.Rook)) {
            startSquare.pieceInstance = null;
            endSquare.pieceInstance = new Rook(moveSquare, pieceColor);
        } else if(pieceChoice.equals(Annotation.Queen)) {
            startSquare.pieceInstance = null;
            endSquare.pieceInstance = new Queen(moveSquare, pieceColor);
        } else {
            System.out.println("Error: wrong piece annotation.");
        }
        return squareList;
    }

    //support method for updateBoard
    private static Square[][] castleMove(Pair pieceSquare, Pair moveSquare, int y, Square[][] squareList) {
        //check if kingSide castle or queenSide castle is happening
        if(pieceSquare.equals(new Pair(4, y)) && moveSquare.equals(new Pair(2, y))) {
            //move the rook and king
            return updateBoard(new Pair(0, y), new Pair(3, y), movePiece(pieceSquare, moveSquare, squareList));
        } else if(pieceSquare.equals(new Pair(4, y)) && moveSquare.equals(new Pair(6, y))) {
            //move the rook and king
            return updateBoard(new Pair(7, y), new Pair(5, y), movePiece(pieceSquare, moveSquare, squareList));
        }
        return squareList;
    }

    public static Square[][] updateBoard(Pair pieceSquare, Pair moveSquare, Square[][] squareList) {
        //set lastMove
        setLastMove(moveSquare);
        //variables
        Square startSquare = squareList[pieceSquare.getX()][pieceSquare.getY()];
//        Square endSquare = squareList[moveSquare.getX()][moveSquare.getY()];
        boolean promotion = false;
        //check if pawn reached end line
        if(startSquare.pieceInstance != null && startSquare.pieceInstance.getAnnotation().equals(Annotation.Pawn)) {
            if(startSquare.pieceInstance.getColor().equals(blackColor) && moveSquare.getY() == 0) {
                promotion = true;
            } else if(startSquare.pieceInstance.getColor().equals(whiteColor) && moveSquare.getY() == 7) {
                promotion = true;
            }
        }
//        boolean enPassant = false;
//        //check for enPassant
//        if(startSquare.pieceInstance != null && startSquare.pieceInstance.getAnnotation().equals(Annotation.Pawn)) {
//            //pawn acts as if taking a piece but the square is empty -> enPassant
//            if(endSquare.pieceInstance == null && pieceSquare.getX() != moveSquare.getX()) {
//                enPassant = true;
//            }
//        }
        //if king was moved update his position for board
        if(startSquare.pieceInstance != null && startSquare.pieceInstance.getAnnotation().equals(Annotation.King)) {
            //check which color the king is
            if(startSquare.pieceInstance.getColor().equals(blackColor)) {
                setBlackKingPosition(moveSquare);
                //check if castle is happening
                if((pieceSquare.equals(new Pair(4, 7)) && moveSquare.equals(new Pair(2, 7))) ||
                        (pieceSquare.equals(new Pair(4, 7)) && moveSquare.equals(new Pair(6, 7)))) {
                    return castleMove(pieceSquare, moveSquare, 7, squareList);
                }
            } else {
                setWhiteKingPosition(moveSquare);
                if((pieceSquare.equals(new Pair(4, 0)) && moveSquare.equals(new Pair(2, 0))) ||
                        (pieceSquare.equals(new Pair(4, 0)) && moveSquare.equals(new Pair(6, 0)))) {
                    return castleMove(pieceSquare, moveSquare, 0, squareList);
                }
            }
        }
//        if(enPassant) {
//            //not working !!!
//            System.out.println("enPassant!!!");
//            takenPieces.add(squareList[moveSquare.getX()][pieceSquare.getY()].pieceInstance);
//            return movePiece(pieceSquare, moveSquare, squareList);
//
//            //not working !!!
//        }
        if(!promotion) {
            return movePiece(pieceSquare, moveSquare, squareList);
        } else {
            return promotion(pieceSquare, moveSquare, squareList);
        }
    }

    //brute force function to check if move is possible (removes check)
    public static boolean checkIfChecked(Colors playerColor, Pair testedMove, Pair testedPiecePosition,
                                         Square[][] squareList) {
        //variables
        boolean underCheck = true;
        //save lastMove before testing
        Pair savedLastMove = null;
        if(lastMove != null) {
            savedLastMove = new Pair(lastMove.getX(), lastMove.getY());
        }
        
//        System.out.println(testedMove.getX() + " " + testedMove.getY());
//        System.out.println("Piece position " + testedPiecePosition.getX() + " " + testedPiecePosition.getY());

        Square[][] updatedBoard = updateBoard(testedPiecePosition, testedMove, squareList);
        
//        if changes in board needs to be checked
//        System.out.println("MOVE FOR CHECK LOGIC IS: " + testedMove.getX() + " " + testedMove.getY());
//        boardToString(updatedBoard);

        //cannot use this -> unending cycle -> try adding boolean to prevent it
        List<Pair> allMoves = new ArrayList<>();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(updatedBoard[x][y].pieceInstance != null && updatedBoard[x][y].pieceInstance.getColor() != playerColor) {
                    if(updatedBoard[x][y].pieceInstance.getAnnotation().equals(Annotation.King)) {
                        King king = (King)updatedBoard[x][y].pieceInstance;
                        king.setCastleLogic(false);
                        allMoves.addAll(king.getValidMoves(updatedBoard, false));
                    } else {
                        allMoves.addAll(updatedBoard[x][y].pieceInstance.getValidMoves(updatedBoard, false));
                    }
                }
            }
        }
        if(allMoves.isEmpty()) {
            underCheck = false;
        }
//        System.out.println("WHITE KING POSITION IS: " + whiteKingPosition.getX() + " " + whiteKingPosition.getY());
        for(Pair move : allMoves){
//            System.out.println("TESTED MOVE IS: " + move.getX() + " " + move.getY());
            if(playerColor == whiteColor) {
                underCheck = move.equals(whiteKingPosition);
            }
            else {
                underCheck = playerColor == blackColor && move.equals(blackKingPosition);
            }
            if(underCheck) {
                break;
            }
        }
        //returning square list and lastMove to correct form
        if(savedLastMove != null) {
            setLastMove(savedLastMove);
        }
        updateBoard(testedMove, testedPiecePosition, squareList);
        for(Pieces piece : takenPieces) {
            placePiece(piece, squareList);
        }
        takenPieces.clear();
//        System.out.println("RETURN VALUE IS: " + underCheck);
        return underCheck;
    }

    public static List<Moves> getValidMoves(Colors playerColor, Square[][] squareList) {
        //create "dictionary" to hold both piece position as key and possible move as value
        List<Moves> validMoves = new ArrayList<>();
        //create temporary List to hold possible moves of piece to later add to validMoves
        List<Pair> tmpValidMoves = new ArrayList<>();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(squareList[x][y].pieceInstance != null && squareList[x][y].pieceInstance.getColor() == playerColor) {
                    tmpValidMoves.addAll(squareList[x][y].pieceInstance.getValidMoves(squareList, true));
                }
                for(Pair move : tmpValidMoves) {
                    validMoves.add(new Moves(new Pair(x, y), move));
                }
            }
        }
        // checkmate logic -> currently would not work for custom position because it needs moves to start working
        return validMoves;
    }

    private void checkClock() {
        //TODO
    }
}

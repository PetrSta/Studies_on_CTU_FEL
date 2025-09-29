package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;
import cz.cvut.fel.pjv.chess.utils.Annotation;

import java.util.ArrayList;
import java.util.List;

//add 1more test for pawn, possible issue found

public class Pawn implements Pieces {

    private Pair position;
    private final Colors color;
    public final Annotation annotation = Annotation.Pawn;

    public Pawn(Pair position, Colors color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public Pair getPosition() {
        return position;
    }

    @Override
    public void updatePosition(Pair position) {
        this.position = position;
    }

    @Override
    public Colors getColor() {
        return color;
    }

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }

    //support method for getValidMoves
    private boolean testPossibleMove(Pair possibleMove, Square[][] squareList, boolean taking) {
        //function to check if intended move square is free or if there is opponents piece
        // in other cases move will not be added to possible moves
        Square possibleSquare = squareList[possibleMove.getX()][possibleMove.getY()];
        //check if taking is possible
        if(possibleSquare.pieceInstance != null && possibleSquare.pieceInstance.getColor() != this.getColor() && taking) {
            return true;
        }
        //check if square is empty
        else {
            return possibleSquare.pieceInstance == null && !taking;
        }
    }

    private List<Pair> enPassant(Square[][] squareList, int startYLine, int endYLine) {
        List<Pair> enPassantMoves = new ArrayList<>();
        Square lastMovePieceSquare = squareList[Board.getLastMove().getX()][Board.getLastMove().getY()];
        if(Board.getLastMove() != null && Board.getLastMove().getY() == startYLine) {
            if(lastMovePieceSquare.pieceInstance != null &&
                    lastMovePieceSquare.pieceInstance.getAnnotation().equals(Annotation.Pawn)) {
                if(Board.getLastMove().getX() == this.getPosition().getX() - 1) {
                    enPassantMoves.add(new Pair(this.getPosition().getX() - 1 ,endYLine));
                }
                if(Board.getLastMove().getX() == this.getPosition().getX() + 1) {
                    enPassantMoves.add(new Pair(this.getPosition().getX() + 1 ,endYLine));
                }
            }
        }
        return enPassantMoves;
    }

    //support method for getValidMoves to calculate possible moves
    private List<Pair> calculateMoves(int signY, Square[][] squareList, boolean firstMove) {
        List<Pair> temporaryValidMoves = new ArrayList<>();
        //pawn move for first time in the game, check before calling in getValidMoves
        Pair preCheckMove = new Pair(this.position.getX(), this.position.getY() + signY);
        if(firstMove) {
            //first pawn move - pawn can move 2 squares
            Pair preCheckMove_ = new Pair(this.position.getX(), this.position.getY() + (2 * signY));
            if(testPossibleMove(preCheckMove, squareList, false)) {
                if(testPossibleMove(preCheckMove_, squareList, false)) {
                    temporaryValidMoves.add(preCheckMove_);
                }
            }
        } else {
            //depending on color, simple move forward by 1 square, color is check before calling in getValidMoves
            if(testPossibleMove(preCheckMove, squareList, false)) {
                temporaryValidMoves.add(preCheckMove);
            }
            //check if taking is possible for the pawn
            if(this.position.getX() >= 0 && this.position.getX() <= 6) {
                //depending on color, check if pawn can take any piece
                Pair preCheckMove_ = new Pair(this.position.getX() + 1, this.position.getY() + signY);
                if (testPossibleMove(preCheckMove_, squareList, true)) {
                    temporaryValidMoves.add(preCheckMove_);
                }
            }
            if(this.position.getX() >= 1 && this.position.getX() <= 7) {
                Pair preCheckMove__ = new Pair(this.position.getX() - 1, this.position.getY() + signY);
                if(testPossibleMove(preCheckMove__, squareList, true)) {
                    temporaryValidMoves.add(preCheckMove__);
                }
            }
        }
        return temporaryValidMoves;
    }

    //no en passant
    @Override
    public List<Pair> getValidMoves(Square[][] squareList, boolean controlCheckLogic) {
        //initialize variables
        List<Pair> validMoves = new ArrayList<>();
        if(this.color == Colors.whiteColor && this.position.getY() == 1) {
            validMoves.addAll(calculateMoves(1, squareList, true));
        }
        if(this.color == Colors.blackColor && this.position.getY() == 6) {
            validMoves.addAll(calculateMoves(-1, squareList, true));
        }
        if(this.color == Colors.whiteColor && this.position.getY() <= 6) {
            validMoves.addAll(calculateMoves(1, squareList, false));
        }
        if(this.color == Colors.blackColor && this.position.getY() >= 1) {
            validMoves.addAll(calculateMoves(-1, squareList, false));
        }
//        //enPassant logic
//        if(this.getColor().equals(Colors.whiteColor) && this.getPosition().getY() == 5) {
//            validMoves.addAll(enPassant(squareList, 4, 5));
//        } else if(this.getColor().equals(Colors.blackColor) && this.getPosition().getY() == 4) {
//            validMoves.addAll(enPassant(squareList, 3, 2));
//        }
        //brute force for check logic
        if(controlCheckLogic) {
            validMoves.removeIf(move -> Board.checkIfChecked(this.getColor(), move, this.getPosition(), squareList));
        }
        return validMoves;
    }
}

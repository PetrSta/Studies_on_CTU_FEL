package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.utils.*;

import java.util.ArrayList;
import java.util.List;

public class King implements Pieces {
    //must be false if castle is to be played
    private boolean hasMoved = false;

    private Pair position;
    private final Colors color;
    private boolean castleLogic = true;

    public final Annotation annotation = Annotation.King;

    public King(Pair position, Colors color) {
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
        hasMoved = true;
    }

    public void setCastleLogic(boolean castleLogic) {
        this.castleLogic = castleLogic;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
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
    private boolean testPossibleMove(Pair possibleMove, Square[][] squareList) {
        //function to check if intended move square is free or if there is opponents piece
        // in other cases move will not be added to possible moves
        Square possibleSquare = squareList[possibleMove.getX()][possibleMove.getY()];
        //check if square is empty or taking is possible
        return possibleSquare.pieceInstance == null || possibleSquare.pieceInstance.getColor() != this.getColor();
    }

    private Pair calculateMoves(int signX, int signY, Square[][] squareList) {
        Pair tmpMove = new Pair(this.position.getX() + signX, this.position.getY() + signY);
        if(tmpMove.getX() <= 7 && tmpMove.getX() >= 0 && tmpMove.getY() <= 7 && tmpMove.getY() >= 0) {
            if(testPossibleMove(tmpMove, squareList)) {
                return tmpMove;
            }
        }
        return null;
    }

    private void testForFreeSquares(Square[][] squareList, int y, List<Pair> castleMoves, Colors oppositeColor, boolean longCastle) {
        List<Pair> allMoves = new ArrayList<>();
        for(int yy = 0; yy < 8; yy++) {
            for(int xx = 0; xx < 8; xx++) {
                if(squareList[xx][yy].pieceInstance != null && squareList[xx][yy].pieceInstance.getColor().equals(oppositeColor)) {
                    if(squareList[xx][yy].pieceInstance.getAnnotation().equals(Annotation.King)) {
                        King king = (King)squareList[xx][yy].pieceInstance;
                        king.setCastleLogic(false);
                        allMoves.addAll(king.getValidMoves(squareList, false));
                    } else {
                        allMoves.addAll(squareList[xx][yy].pieceInstance.getValidMoves(squareList, false));
                    }
                }
            }
        }
        boolean free = true;
        for(Pair move : allMoves) {
            if(longCastle) {
                if (move.getX() == 4 && move.getY() == y ||
                        move.getX() == 3 && move.getY() == y ||
                        move.getX() == 2 && move.getY() == y) {
                    free = false;
                    break;
                }
            } else {
                if (move.getX() == 4 && move.getY() == y ||
                        move.getX() == 5 && move.getY() == y ||
                        move.getX() == 6 && move.getY() == y) {
                    free = false;
                    break;
                }
            }
        }
        if(free) {
            if(longCastle) {
                castleMoves.add(new Pair(2, y));
            } else {
                castleMoves.add(new Pair(6, y));
            }
        }
    }

    private List<Pair> castleLogic(Square[][] squareList, int y) {
        List<Pair> castleMoves = new ArrayList<>();
        Rook longCastleRook;
        Rook shortCastleRook;
        //get the opposite color to the king
        Colors oppositeColor;
        if(this.getColor().equals(Colors.whiteColor)) {
            oppositeColor = Colors.blackColor;
        } else {
            oppositeColor = Colors.whiteColor;
        }

        //if rook is on starting square of rook and is the correct color
        if(squareList[0][y].pieceInstance != null &&
                squareList[0][y].pieceInstance.getAnnotation().equals(Annotation.Rook) &&
                squareList[0][y].pieceInstance.getColor().equals(this.getColor())) {
            //check if the rook has moved this  game
            longCastleRook = (Rook)squareList[0][y].pieceInstance;
            if(!longCastleRook.getHasMoved()) {
                //check if space between king and rook is clear
                if(squareList[1][y].pieceInstance == null && squareList[2][y].pieceInstance == null
                        && squareList[3][y].pieceInstance == null) {
                    //check if squares that king moves over are under attack or if king is under attack
                    testForFreeSquares(squareList, y, castleMoves, oppositeColor, true);
                }
            }
        }
        //if rook is on starting square of rook and is the correct color
        if(squareList[7][y].pieceInstance != null &&
                squareList[7][y].pieceInstance.getAnnotation().equals(Annotation.Rook) &&
                squareList[0][y].pieceInstance.getColor().equals(this.getColor())) {
            //check if the rook has moved this  game
            shortCastleRook = (Rook)squareList[7][y].pieceInstance;
            if(!shortCastleRook.getHasMoved()) {
                //check if space between king and rook is clear
                if(squareList[5][y].pieceInstance == null && squareList[6][y].pieceInstance == null) {
                    //check if squares that king moves over are under attack or if king is under attack
                    testForFreeSquares(squareList, y, castleMoves, oppositeColor, false);
                }
            }
        }
        return castleMoves;
    }

    @Override
    public List<Pair> getValidMoves(Square[][] squareList, boolean controlCheckLogic) {
        //initialize variables
        List<Pair> validMoves = new ArrayList<>();
        //moving king top left
        Pair topLeft =  calculateMoves(-1, 1, squareList);
        if(topLeft != null) {
            validMoves.add(topLeft);
        }
        //moving king top
        Pair top =  calculateMoves(0, 1, squareList);
        if(top != null) {
            validMoves.add(top);
        }
        //moving king top right
        Pair topRight =  calculateMoves(1, 1, squareList);
        if(topRight != null) {
            validMoves.add(topRight);
        }
        //moving king left
        Pair left =  calculateMoves(-1, 0, squareList);
        if(left != null) {
            validMoves.add(left);
        }
        //moving king right
        Pair right =  calculateMoves(1, 0, squareList);
        if(right != null) {
            validMoves.add(right);
        }
        //moving king bottom left
        Pair bottomLeft =  calculateMoves(-1, -1, squareList);
        if(bottomLeft != null) {
            validMoves.add(bottomLeft);
        }
        //moving king bottom
        Pair bottom =  calculateMoves(0, -1, squareList);
        if(bottom != null) {
            validMoves.add(bottom);
        }
        //moving king bottom right
        Pair bottomRight =  calculateMoves(1, -1, squareList);
        if(bottomRight != null) {
            validMoves.add(bottomRight);
        }
        //castle logic
        if(!this.hasMoved && castleLogic) {
            //depending on king color call function with correct arguments
            if(this.getColor().equals(Colors.whiteColor) && this.getPosition().equals(new Pair(4, 0))) {
                validMoves.addAll(castleLogic(squareList, 0));
            } else if(this.getColor().equals(Colors.blackColor) && this.getPosition().equals(new Pair(4, 7))) {
                validMoves.addAll(castleLogic(squareList, 7));
            }
        }
        //brute force for check logic
        if(controlCheckLogic) {
            validMoves.removeIf(move -> Board.checkIfChecked(this.getColor(), move, this.getPosition(), squareList));
        }
        return validMoves;
    }
}

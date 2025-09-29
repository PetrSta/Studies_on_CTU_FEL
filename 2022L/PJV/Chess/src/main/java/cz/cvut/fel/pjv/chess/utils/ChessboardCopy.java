package cz.cvut.fel.pjv.chess.utils;

import cz.cvut.fel.pjv.chess.pieces.*;

public class ChessboardCopy {
    //method that takes chessboard representation to create exact copy of it
    public static Square[][] copyChessboard(Square[][] chessboardToCopy) {
        Square[][] copiedChessboard = new Square[8][8];
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Pair position = new Pair(x, y);
                if(chessboardToCopy[x][y].pieceInstance == null) {
                    copiedChessboard[x][y] = new Square(position, null);
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.Pawn)) {
                    if(chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new Pawn(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new Pawn(position, Colors.blackColor));
                    }
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.Knight)) {
                    if (chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new Knight(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new Knight(position, Colors.blackColor));
                    }
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.Bishop)) {
                    if (chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new Bishop(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new Bishop(position, Colors.blackColor));
                    }
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.Rook)) {
                    if (chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new Rook(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new Rook(position, Colors.blackColor));
                    }
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.Queen)) {
                    if (chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new Queen(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new Queen(position, Colors.blackColor));
                    }
                } else if(chessboardToCopy[x][y].pieceInstance.getAnnotation().equals(Annotation.King)) {
                    if (chessboardToCopy[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                        copiedChessboard[x][y] = new Square(position, new King(position, Colors.whiteColor));
                    } else {
                        copiedChessboard[x][y] = new Square(position, new King(position, Colors.blackColor));
                    }
                }
            }
        }
        return copiedChessboard;
    }
}

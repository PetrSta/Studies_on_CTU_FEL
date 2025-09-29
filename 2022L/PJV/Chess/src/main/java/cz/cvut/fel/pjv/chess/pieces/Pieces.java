package cz.cvut.fel.pjv.chess.pieces;

import cz.cvut.fel.pjv.chess.utils.Annotation;
import cz.cvut.fel.pjv.chess.utils.Square;
import cz.cvut.fel.pjv.chess.utils.Colors;
import cz.cvut.fel.pjv.chess.utils.Pair;

import java.util.List;

public interface Pieces {

    Pair getPosition();

    void updatePosition(Pair position);

    Colors getColor();

    Annotation getAnnotation();

    //only added basic moves, no en passant, castle, promotion, also need to add check
    // also: if moving piece would result in check (illegal move), and block possibility of taking king, king can move into check
    //unit tests will need to be updated

    //simple interaction with other pieces added

    //currently, working on unit tests for pieces, and rewriting getValidMoves methods
    List<Pair> getValidMoves(Square[][] squareList, boolean controlCheckLogic);

}

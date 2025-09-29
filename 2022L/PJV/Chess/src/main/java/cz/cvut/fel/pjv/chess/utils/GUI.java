package cz.cvut.fel.pjv.chess.utils;

import cz.cvut.fel.pjv.chess.board.Board;
import cz.cvut.fel.pjv.chess.pieces.King;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class GUI extends JFrame {
    private final JPanel gui = new JPanel(new BorderLayout());
    private static JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessboard;
    private static final String COLUMNS = "ABCDEFGH";
    private static Pair starterSquare = null;
    private static boolean whiteMove = true;
    private static Square[][] starterBoard =  Board.createStarterPosition();


    public final JComponent getChessBoard() {
        return chessboard;
    }

    GUI() {
        initializeGui();
    }

    //method to create button 2d array based on chessboard status
    private static void createChessBoardButtons(ActionListener actionListener) {
        //create empty board
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                JButton button = new JButton();
                button.setMargin(buttonMargin);
                // add size to our buttons -> 64 x 64
                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                button.setIcon(icon);
                if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
                    button.setBackground(Color.GRAY);
                } else {
                    button.setBackground(Color.WHITE);
                }
                button.addActionListener(actionListener);
                chessBoardSquares[x][y] = button;
            }
        }
    }

    private static void setIconsForButtons(Square[][]chessBoardRepresentation) throws IOException {
        //search the chessboard representation and add icons to buttons depending on that -> pieces shown visually
        for(int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if(chessBoardRepresentation[x][y].pieceInstance != null) {
                    if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.King)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhiteKing.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackKing.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    } else if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.Queen)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhiteQueen.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackQueen.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    } else if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.Rook)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhiteRook.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackRook.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    } else if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.Bishop)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhiteBishop.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackBishop.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    } else if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.Knight)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhiteKnight.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackKnight.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    } else if (chessBoardRepresentation[x][y].pieceInstance.getAnnotation().equals(Annotation.Pawn)) {
                        if(chessBoardRepresentation[x][y].pieceInstance.getColor().equals(Colors.whiteColor)) {
                            URL imageURL = GUI.class.getResource("/WhitePawn.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        } else {
                            URL imageURL = GUI.class.getResource("/BlackPawn.png");
                            ImageIcon icon = null;
                            if (imageURL != null) {
                                icon = new ImageIcon(ImageIO.read(imageURL));
                            }
                            chessBoardSquares[x][y].setIcon(icon);
                        }
                    }
                } else {
                    ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                    chessBoardSquares[x][y].setIcon(icon);
                }
            }
        }
    }

    private static void setBackGround(boolean defaultColoring, List<Pair> validMoves, Pair pieceSquare) {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(defaultColoring) {
                    if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
                        chessBoardSquares[x][y].setBackground(Color.GRAY);
                    } else {
                        chessBoardSquares[x][y].setBackground(Color.WHITE);
                    }
                } else {
                    for(Pair move : validMoves) {
                        int validX = move.getX();
                        int validY = move.getY();
                        if(validX == x && validY == y) {
                            chessBoardSquares[x][y].setBackground(Color.YELLOW);
                        }
                        int pieceX = pieceSquare.getX();
                        int pieceY = pieceSquare.getY();
                        if(pieceX == x && pieceY == y) {
                            chessBoardSquares[x][y].setBackground(Color.BLUE);
                        }
                    }
                }
            }
        }
    }

    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(10, 10, 10, 10));
        chessboard = new JPanel(new GridLayout(0, 9));
        chessboard.setBorder(new LineBorder(Color.WHITE));
        gui.add(chessboard);
        // add column annotation
        for (int x = 0; x < 8; x++) {
            chessboard.add(new JLabel(COLUMNS.substring(x, x + 1), SwingConstants.CENTER));
        }
        chessboard.add(new JLabel(""));
        // add row annotation and squares
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 9; x++) {
                if(x == 8) {
                    chessboard.add(new JLabel("" + (y + 1), SwingConstants.CENTER));
                } else {
                    chessboard.add(chessBoardSquares[x][y]);
                }
            }
        }
    }

    public final JComponent getGui() {
        return gui;
    }

    private static List<Pair> pieceMovementForGUI(JButton button, List<Pair> validMoves) {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(button == chessBoardSquares[x][y]) {
                    if(validMoves.isEmpty()) {
                        if(starterBoard[x][y].pieceInstance == null) {
                            break;
                        } else if(whiteMove && starterBoard[x][y].pieceInstance
                                .getColor().equals(Colors.whiteColor) || !whiteMove && starterBoard[x][y]
                                        .pieceInstance.getColor().equals(Colors.blackColor)){
                            Square[][] copiedBoard = ChessboardCopy.copyChessboard(starterBoard);
                            if(starterBoard[x][y].pieceInstance.getAnnotation().equals(Annotation.King)) {
                                King king = (King)starterBoard[x][y].pieceInstance;
                                king.setCastleLogic(true);
                                validMoves = king.getValidMoves(copiedBoard, true);
                            } else {
                                validMoves = starterBoard[x][y].pieceInstance.getValidMoves(copiedBoard, true);
                            }
                            starterSquare = new Pair(x, y);
                            setBackGround(false, validMoves, starterSquare);
                        } else {
                            setBackGround(true, validMoves, starterSquare);
                            break;
                        }
                    } else if(starterSquare != null) {
                        Pair movementSquare = new Pair(x, y);
                        if(validMoves.contains(movementSquare)) {
                            starterBoard = Board.updateBoard(starterSquare, movementSquare, starterBoard);
                            try {
                                setIconsForButtons(starterBoard);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            whiteMove = !whiteMove;
                        }
                        setBackGround(true, validMoves, starterSquare);
                        validMoves.clear();
                    }
                }
            }
        }
        return validMoves;
    }

    private static void checkmateControl() {
        Colors opponent;
        Colors player;
        if(whiteMove) {
            opponent = Colors.whiteColor;
            player = Colors.blackColor;
        } else {
            opponent = Colors.blackColor;
            player = Colors.whiteColor;
        }
        if(Board.getValidMoves(opponent, ChessboardCopy.copyChessboard(starterBoard)).isEmpty()) {
            List<Moves> allMoves = Board.getValidMoves(player, ChessboardCopy.copyChessboard(starterBoard));
            boolean checkmate = false;
            for(Moves move : allMoves) {
                if(opponent.equals(Colors.blackColor) &&
                        move.getMoveSquare().equals(Board.getBlackKingPosition())) {
                    checkmate = true;
                    break;
                } else if(opponent.equals(Colors.whiteColor) &&
                        move.getMoveSquare().equals(Board.getWhiteKingPosition())) {
                    checkmate = true;
                    break;
                }
            }
            JFrame resultWindow = new JFrame();
            JLabel resultLabel = new JLabel(" ", SwingConstants.CENTER);
            resultWindow.setLayout(new GridLayout());
            if(checkmate) {
                if(player.equals(Colors.whiteColor)) {
                    resultLabel.setText("White wins!");
                } else {
                    resultLabel.setText("Black wins!");
                }
            } else {
                resultLabel.setText("Draw");
            }
            resultLabel.setFont(new Font("Verdana", Font.BOLD, 25));
            resultWindow.add(resultLabel);
            resultWindow.setSize(200, 100);
            resultWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            resultWindow.setVisible(true);
        }
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //create starter position

                //action listener
                ActionListener actionListener = new ActionListener() {
                    List<Pair> validMoves = new ArrayList<>();
                    JButton button;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        button = (JButton)e.getSource();
                        validMoves = pieceMovementForGUI(button, validMoves);
                        checkmateControl();
                    }
                };
                //action listener end

                //create buttons for GUI
                createChessBoardButtons(actionListener);
                try {
                    setIconsForButtons(starterBoard);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GUI chessboard = new GUI();
                JFrame frame = new JFrame("Chess");
                BufferedImage icon;
                try {
                    icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/chessIcon.png")));
                    frame.setIconImage(icon);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                frame.add(chessboard.getGui());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                frame.pack();
                // ensures the minimum size is known
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
                //add action listeners here

            }
        };
        SwingUtilities.invokeLater(r);
    }
}

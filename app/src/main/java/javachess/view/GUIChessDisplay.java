package javachess.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javachess.audio.AudioPlayer;
import javachess.events.*;
import javachess.model.*;
import javachess.events.Event;
import javachess.pieces.*;
import javachess.player.BotPlayer;
import javachess.player.HumanPlayer;
import javachess.translation.Message;


/**
 * Class representing the main window of the chess game.
 * This class is responsible for displaying the chessboard, pieces, and handling user interactions.
 */
public class GUIChessDisplay extends JFrame implements Observer, EventVisitor {

    CaseLabel[][] tabJL;
    Map<Piece, ImageIcon> pieceIcons = new HashMap<>();

    Position mouseClick;
    PieceColor lastColorClicked;
    final Game game;

    public GUIChessDisplay(Game game) {
        super("Java Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(GUIChessDisplay.class.getResource("/classic/wK.png")));
        setPreferredSize(new Dimension(800, 800));

        // Fills the pieceIcons map with the pieces and their corresponding icons
        for(PieceColor pieceColor : PieceColor.values()){
            for(PieceType type: PieceType.values()) {
                Piece piece = createPiece(type, pieceColor);
                String filename = piece.findFile();
                String entireFilename = '/' + game.configParser.getValue("CHESS_PIECE_SET", "Classic").toLowerCase() + '/' + filename;
                ImageIcon icon = new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(entireFilename))).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                pieceIcons.put(piece, icon);
            }
        }

        this.game = game;
        game.addObserver(this);
        tabJL = new CaseLabel[8][8];

        JPanel jp = new JPanel(new GridLayout(8, 8));
        setContentPane(jp);


        // We iterate the columns and then the lines (helps to have the right orientation)
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                CaseLabel jl = new CaseLabel();
                tabJL[i][j] = jl;
                jl.setOpaque(true);
                jl.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                jl.setPreferredSize(new Dimension(75, 75));

                final int ii = i, jj = j;
                jl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(game.getCurrentPlayer() instanceof HumanPlayer humanPlayer) {
                            Piece piece = game.getBoard().getCells().get(new Position(ii, jj)).getPiece();
                            if(mouseClick == null){
                                // check if first click was on a piece of another colour
                                if (piece != null) {
                                    if(humanPlayer.getColor() == piece.getColor()){
                                        colorAvailableCells(game, ii, jj);
                                        mouseClick = new Position(ii, jj);
                                        lastColorClicked = piece.getColor();
                                    }
                                }
                            } else {
                                Position mouseSecondClick = new Position(ii, jj);
                                if((piece != null && piece.getColor() == lastColorClicked) || mouseClick.equals(mouseSecondClick)){
                                    // emulate the first click
                                    clearRingsFromBoard(game);
                                    if(!mouseClick.equals(mouseSecondClick)){
                                        mouseClick = null;
                                        mouseClicked(null);
                                    } else {
                                        mouseClick = null;
                                    }
                                } else {
                                    if (!mouseClick.equals(mouseSecondClick)) {
                                        humanPlayer.setMove(mouseClick, mouseSecondClick);
                                    }
                                    clearRingsFromBoard(game);
                                    mouseClick = null;
                                }
                            }
                        }
                    }
                });

                jp.add(jl);
            }
        }

        // Set the labels for the columns and rows
        for(int i = 0; i < 8; i++){
            tabJL[i][7].setLowerRightCornerLabel(String.valueOf((char) ('a' + i)));
            tabJL[0][i].setUpperLeftCornerLabel(String.valueOf(8-i));
        }

        updateBoard();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                Component component = e.getComponent();
//                if (component instanceof JFrame frame) {
//                    int size = min(frame.getWidth(), frame.getHeight());
//                    frame.setSize(size, size);
//                }
//            }
//        });
    }

    /**
     * Displays a ring around the available cells for the selected piece.
     * The ring is gray for empty cells and red for cells with a piece (pieces that can be captured).
     * @param game The game instance
     * @param ii x coordinate of the piece
     * @param jj y coordinate of the piece
     */
    private void colorAvailableCells(Game game, int ii, int jj) {
        Piece piece = game.getBoard().getCells().get(new Position(ii, jj)).getPiece();
        if (piece != null && piece.getColor() == game.getCurrentPlayer().getColor()) {
            ArrayList<Cell> validCells = game.getBoard().getValidCellsForBoard(piece);
            for (Cell cell : validCells) {
                Position pos = game.getBoard().getCells().getReverse(cell);
                if(game.getBoard().getCells().get(pos).getPiece() != null){
                    tabJL[pos.getX()][pos.getY()].setDrawCircle(true, Color.RED);
                } else {
                    tabJL[pos.getX()][pos.getY()].setDrawCircle(true, Color.GRAY);
                }
            }
            tabJL[ii][jj].setDrawCircle(true, new Color(85, 198, 255, 128));
        }
    }

    private void clearRingsFromBoard(Game game) {
        for (Position p : game.getBoard().getCells().keySet()) {
            tabJL[p.getX()][p.getY()].setDrawCircle(false);
        }
    }

    private ImageIcon getScaledIcon(ImageIcon icon, int size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    private void changePlayerWindowTitle(PieceColor color){
        setTitle("Chess Master - " + game.languageService.getMessage(Message.TO_PLAY, Map.of("color", color.toString())));
    }

    public Piece createPiece(PieceType type, PieceColor pieceColor) {
        return switch (type) {
            case PAWN -> new Pawn(pieceColor);
            case ROOK -> new Rook(pieceColor);
            case KNIGHT -> new Knight(pieceColor);
            case BISHOP -> new Bishop(pieceColor);
            case QUEEN -> new Queen(pieceColor);
            case KING -> new King(pieceColor);
        };
    }

    /**
     * Updates the chessboard with the current state of the game.
     * This method is called whenever there is a change in the game state.
     */
    public void updateBoard() {
        BiMap<Position, Cell> cells = this.game.getBoard().getCells();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = cells.get(new Position(i, j)).getPiece();
                tabJL[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                if(piece != null){
                    tabJL[i][j].setIcon(pieceIcons.get(piece));
                } else {
                    tabJL[i][j].setIcon(null);
                }
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Event event){
            visit(event);
        }
    }

    @Override
    public void visit(CheckEvent event) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = game.getBoard().getCells().get(new Position(i, j)).getPiece();
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == game.getCurrentPlayer().getColor()) {
                    tabJL[i][j].setBackground(Color.RED);
                }
            }
        }
    }

    @Override
    public void visit(CheckMateEvent event) {
        JOptionPane.showMessageDialog(this, game.languageService.getMessage(Message.CHECKMATE, Map.of("player", event.getWinnerColor().toString())));
        this.dispose();
    }

    @Override
    public void visit(UpdateBoardEvent event) {
        updateBoard();
    }

    @Override
    public void visit(PromotionEvent event) {
        Position piecePosition = event.getFrom();
        Cell pawnCell = game.getBoard().getCells().get(piecePosition);
        Piece pawn = pawnCell.getPiece();
        // we instance a new thread to avoid blocking the main thread
        new Thread(() -> {
            if(game.getCurrentPlayer() instanceof HumanPlayer){
                // sleep to avoid process collision
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                PieceColor color = event.getFrom().getX() == 0 ? PieceColor.WHITE : PieceColor.BLACK;
                ImageIcon[] options = Arrays.stream(new Piece[]{
                        new Queen(color),
                        new Rook(color),
                        new Bishop(color),
                        new Knight(color)
                }).map(piece -> pieceIcons.getOrDefault(piece, null)).toArray(ImageIcon[]::new);


                int choice = JOptionPane.showOptionDialog(this, game.languageService.getMessage(Message.PROMOTE, null), "Promotion",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);


                // Send back the promoted piece to the game
                game.promoteTo = switch (choice) {
                    case 1 -> new Rook(pawn.getColor(), pawnCell);
                    case 2 -> new Bishop(pawn.getColor(), pawnCell);
                    case 3 -> new Knight(pawn.getColor(), pawnCell);
                    default -> new Queen(pawn.getColor(), pawnCell);
                };
            } else {
                BotPlayer bp = (BotPlayer) game.getCurrentPlayer();
                game.promoteTo = bp.getPromoteTo(pawnCell);
            }
            synchronized (game){
                game.notify();
            }
        }).start();
    }

    @Override
    public void visit(DrawEvent event) {
        JOptionPane.showMessageDialog(this, game.languageService.getMessage(Message.DRAW) + "!" + event.getReason());
        this.dispose();
    }

    @Override
    public void visit(StalemateEvent event) {
        JOptionPane.showMessageDialog(this, game.languageService.getMessage(Message.STALEMATE));
        this.dispose();
    }

    @Override
    public void visit(ChangePlayerEvent event) {
        changePlayerWindowTitle(event.getColor());
    }

    @Override
    public void visit(SoundEvent event) {
        if(game.configParser.getValue("CHESS_SOUND_ENABLED", "true").equals("true")){
            AudioPlayer.playAudio(event.getSound());
        }
    }
}
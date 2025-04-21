package javachess;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javachess.events.CheckEvent;
import javachess.events.CheckMateEvent;
import javachess.events.PromotionEvent;
import javachess.events.UpdateBoardEvent;
import javachess.pieces.*;

import static java.lang.Math.min;

public class Window extends JFrame implements Observer, EventVisitor {

    JLabel[][] tabJL;
    Map<Piece, ImageIcon> pieceIcons = new HashMap<>();

    Position mouseClick;
    final Game game;

    public Window(Game game) {
        super("Java Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/white-king.png")));
        setPreferredSize(new Dimension(800, 800));

        for(PieceColor pieceColor : PieceColor.values()){
            for(PieceType type: PieceType.values()) {
                Piece piece = createPiece(type, pieceColor);
                String filename = piece.findFile();
                ImageIcon icon = new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource('/' + filename))).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                pieceIcons.put(piece, icon);
            }
        }

        this.game = game;
        game.addObserver(this);
        tabJL = new JLabel[8][8];

        JPanel jp = new JPanel(new GridLayout(8, 8));
        setContentPane(jp);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel jl = new JLabel();
                tabJL[i][j] = jl;
                jl.setOpaque(true);
                jl.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                jl.setPreferredSize(new Dimension(75, 75));

                final int ii = i, jj = j;
                jl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
//                        System.out.println("Clicked at " + ii + " " + jj);
                        if(mouseClick == null){
                            Piece piece = game.getBoard().getCells().get(new Position(ii, jj)).getPiece();
                            if (piece != null) {
                                ArrayList<Cell> validCells = piece.decorator.getValidCells();
//                                System.out.println(validCells);
                                for (Cell cell : validCells) {
                                    Position pos = game.getBoard().getCells().getReverse(cell);
                                    tabJL[pos.getX()][pos.getY()].setBackground(Color.YELLOW);
                                }
                            }
                            mouseClick = piece == null ? null : new Position(ii, jj);
                        } else {
                            Position mouseSecondClick = new Position(ii, jj);
                            if (!mouseClick.equals(mouseSecondClick)) {
                                game.getCurrentPlayer().setMove(mouseClick, mouseSecondClick);
                            }
                            for (Position p : game.getBoard().getCells().keySet()) {
                                tabJL[p.getX()][p.getY()].setBackground((p.getX() + p.getY()) % 2 == 0 ? Color.WHITE : Color.BLACK);
                            }
//                            game.getBoard().log();
                            mouseClick = null;
                        }
                    }
                });

                jp.add(jl);
            }
        }

        updateBoard();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component component = e.getComponent();
                if (component instanceof JFrame frame) {
                    int size = min(frame.getWidth(), frame.getHeight());
                    frame.setSize(size, size);
                }
            }
        });
    }

    private ImageIcon getScaledIcon(ImageIcon icon, int size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    private ImageIcon getScaledIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(path)));
        return getScaledIcon(icon, size);
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

    public void updateBoard() {
        BiMap<Position, Cell> cells = this.game.getBoard().getCells();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = cells.get(new Position(i, j)).getPiece();
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
        JOptionPane.showMessageDialog(this, "Check!");
    }

    @Override
    public void visit(CheckMateEvent event) {
        JOptionPane.showMessageDialog(this, "CheckMate! Player " + event.getWinnerColor() + " wins!");
    }

    @Override
    public void visit(UpdateBoardEvent event) {
        updateBoard();
    }

    @Override
    public void visit(PromotionEvent event) {
        PieceColor color = event.getFrom().getX() == 0 ? PieceColor.WHITE : PieceColor.BLACK;
        ImageIcon[] options = Arrays.stream(new Piece[]{
                new Queen(color),
                new Rook(color),
                new Bishop(color),
                new Knight(color)
        }).map(piece -> pieceIcons.getOrDefault(piece, null)).toArray(ImageIcon[]::new);
        int choice = JOptionPane.showOptionDialog(this, "Choose a piece to promote to:", "Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        Position piecePosition = event.getFrom();
        Cell pawnCell = game.getBoard().getCells().get(piecePosition);
        Piece pawn = pawnCell.getPiece();
        Piece promoteTo = switch (choice) {
            case 1 -> new Rook(pawn.getColor(), pawnCell);
            case 2 -> new Bishop(pawn.getColor(), pawnCell);
            case 3 -> new Knight(pawn.getColor(), pawnCell);
            default -> new Queen(pawn.getColor(), pawnCell);
        };

        // Send back the promoted piece to the game
        game.promoteTo = promoteTo;

        synchronized (game) {
            game.notifyAll();
        }
    }
}
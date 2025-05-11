package javachess;

import javachess.parser.Instruction;
import javachess.parser.Parser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChessGameMenu extends JFrame {

    public ChessGameMenu() {
        setTitle("Chess Master - Main Menu");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/classic/wK.png")));

        // Background color
        getContentPane().setBackground(new Color(20, 20, 20));

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2),
                new EmptyBorder(30, 50, 30, 50)
        ));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title label
        JLabel titleLabel = new JLabel("♛ Chess Master");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel);

        // Buttons
        mainPanel.add(createStyledButton("Play vs Player", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPlay(e);
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createStyledButton("Load from PGN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadPGN(e);
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createStyledButton("Settings", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSettings(e);
            }
        }));

        // Center panel in frame
        setLayout(new GridBagLayout());
        add(mainPanel);
    }

    private JButton createStyledButton(String text, AbstractAction action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(10, 20, 10, 20)
        ));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
            }
        });

        return button;
    }

private void onPlay(ActionEvent e) {
    // Avoid blocking the current thread, (while loop)
    new Thread(() -> {
        this.dispose();
        Game game = new Game();
        new Window(game);
        game.playGame();
    }).start();
}

    private void onLoadPGN(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open PGN File");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            JOptionPane.showMessageDialog(this, "Loading PGN from:\n" + filePath);
            Parser parser = new Parser();
            String gameTxt = parser.getGameFromPath(filePath);
            parser.splitParts(gameTxt);

            new Thread(() -> {
                this.dispose();
                Board board = new Board();
                Game game = new Game(board);
                for (Instruction moveInstr : parser.getMoves()) {
                    Move move = board.getMoveFromInstructions(moveInstr);
                    game.setMove(move.getFrom(), move.getTo(), false);
                    game.actualPlayer++;
                }
                new Window(game);
                game.playGame();
            }).start();
        }
    }

    private void onSettings(ActionEvent e) {
        SettingsPanel settingsPanel = new SettingsPanel(this);
        settingsPanel.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChessGameMenu().setVisible(true);
        });
    }
}

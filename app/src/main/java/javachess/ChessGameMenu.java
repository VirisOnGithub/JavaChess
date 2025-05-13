package javachess;

import javachess.parser.Instruction;
import javachess.parser.Parser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main menu for the chess GUI game.
 * This class creates a JFrame that serves as the main menu for the chess game.
 * It includes buttons to start a new game, load a game from a PGN file, and access settings.
 */
public class ChessGameMenu extends JFrame {
    private final LanguageService languageService = new LanguageService();


    public ChessGameMenu() {
        languageService.setLanguage(new ConfigParser().getLanguage());

        setTitle("Chess Master - " + languageService.getMessage(Message.MAIN_MENU));
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
        mainPanel.add(createStyledButton(languageService.getMessage(Message.PLAY_VS_PLAYER), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPlay();
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createStyledButton(languageService.getMessage(Message.LOAD_FROM_PGN), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadPGN();
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createStyledButton(languageService.getMessage(Message.SETTINGS), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSettings();
            }
        }));

        // Center panel in frame
        setLayout(new GridBagLayout());
        add(mainPanel);
    }

    /**
     * Custom button for the menu
     * @param text The text to display on the button
     * @param action The action to perform when the button is clicked
     * @return A JButton with custom styling
     */
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

    /**
     * Action performed when the "Play" button is clicked.
     * This method starts a new game and disposes of the menu window.
     *
     */
    private void onPlay() {
        // Avoid blocking the current thread, (while loop)
        new Thread(() -> {
            this.dispose();
            Game game = new Game();
            new Window(game);
            game.playGame();
        }).start();
    }

    /**
     * Action performed when the "Load PGN" button is clicked.
     * This method opens a file chooser dialog to select a PGN file and loads the game from it.
     *
     */
    private void onLoadPGN() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(languageService.getMessage(Message.OPEN_PGN_FILE));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            Parser parser = new Parser();
            String gameTxt = parser.getGameFromPath(filePath);
            parser.splitParts(gameTxt);

            // Avoid blocking the current thread, (while loop)
            new Thread(() -> {
                this.dispose();
                Board board = new Board();
                Game game = new Game(board);
                for (Instruction moveInstr : parser.getMoves()) {
                    Move move = board.getMoveFromInstructions(moveInstr);
                    game.setMove(move, false);
                    game.actualPlayer++;
                }
                new Window(game);
                game.playGame();
            }).start();
        }
    }

    /**
     * Action performed when the "Settings" button is clicked.
     * This method opens the settings panel.
     *
     */
    private void onSettings() {
        this.dispose();
        SettingsPanel settingsPanel = new SettingsPanel(this);
        settingsPanel.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGameMenu().setVisible(true));
    }
}

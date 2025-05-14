package javachess.view;

import javachess.model.*;
import javachess.parser.ConfigParser;
import javachess.parser.Instruction;
import javachess.parser.Parser;
import javachess.player.BotPlayer;
import javachess.translation.LanguageService;
import javachess.translation.Message;

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
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(GUIChessDisplay.class.getResource("/classic/wK.png")));

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
        if(BotPlayer.testConnection()){
            mainPanel.add(createStyledButton(languageService.getMessage(Message.PLAY_VS_COMPUTER), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onPlayVsComputer();
                }
            }));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        mainPanel.add(createStyledButton(languageService.getMessage(Message.LOAD_FROM_PGN), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadPGN();
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createStyledButton(languageService.getMessage(Message.LOAD_FROM_FEN), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadFEN();
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
            new GUIChessDisplay(game);
            game.playGame();
        }).start();
    }

    /**
     * Action performed when the "Play vs Computer" button is clicked.
     * This method starts a new game against the computer and disposes of the menu window.
     *
     */
    private void onPlayVsComputer() {
        // create a radio button to select the difficulty
        String[] difficulties = {languageService.getMessage(Message.EASY), languageService.getMessage(Message.NORMAL), languageService.getMessage(Message.HARD)};
        JRadioButton[] difficultyButtons = new JRadioButton[difficulties.length];
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < difficulties.length; i++) {
            difficultyButtons[i] = new JRadioButton(difficulties[i]);
            difficultyButtons[i].setBackground(new Color(30, 30, 30));
            difficultyButtons[i].setForeground(Color.WHITE);
            difficultyButtons[i].setSelected(i == 1); // Default to NORMAL
            group.add(difficultyButtons[i]);
            panel.add(difficultyButtons[i]);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int result = JOptionPane.showConfirmDialog(this, panel, languageService.getMessage(Message.SELECT_DIFFICULTY), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return; // User canceled
        }
        int difficulty = 1; // Default to NORMAL
        for (int i = 0; i < difficultyButtons.length; i++) {
            if (difficultyButtons[i].isSelected()) {
                difficulty = i;
                break;
            }
        }

        int depth = difficulty * 6 + 1; // Set depth based on difficulty
        System.out.println("Selected difficulty: " + difficulties[difficulty] + " (depth: " + depth + ")");

        // Avoid blocking the current thread, (while loop)
        new Thread(() -> {
            this.dispose();
            Game game = new Game(true, depth);
            new GUIChessDisplay(game);
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
                    game.incrementPlayer();
                }
                new GUIChessDisplay(game);
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

    /**
     * Action performed when the "Load FEN" button is clicked.
     */
    private void onLoadFEN() {
        String fen = JOptionPane.showInputDialog(this, languageService.getMessage(Message.LOAD_FROM_FEN));
        if (fen != null && !fen.isEmpty()) {
            // Avoid blocking the current thread, (while loop)
            new Thread(() -> {
                this.dispose();
                Game game = new Game();
                game.fromFEN(fen);
                new GUIChessDisplay(game);
                game.playGame();
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGameMenu().setVisible(true));
    }
}

package javachess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SettingsPanel class that provides a GUI for configuring game settings.
 * This class allows users to select the piece set, language, and sound settings.
 */
public class SettingsPanel extends JDialog {

    private JComboBox<String> pieceSetDropdown;
    private JComboBox<String> languageDropdown;
    private JCheckBox soundToggle;
    private final ConfigParser configParser;

    public SettingsPanel(JFrame parent) {
        // Initialize the dialog
        super(parent, "Settings", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Initialize the configuration parser
        configParser = new ConfigParser("settings.conf");

        // Main panel setup
        JPanel mainPanel = createMainPanel();

        // Add components to the main panel
        addTitleLabel(mainPanel);
        addLanguageDropdown(mainPanel);
        addPieceSetDropdown(mainPanel);
        addSoundToggle(mainPanel);
        addSaveButton(mainPanel);

        // Add the main panel to the dialog
        add(mainPanel);

        // Create conf file if it doesn't exist
        createConfFileIfNotExists();
    }

    private void createConfFileIfNotExists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("settings.conf", true))) {
            // Check if the file is empty
            if (writer.toString().isEmpty()) {
                writer.write("CHESS_PIECE_SET=Classic\n");
                writer.write("CHESS_SOUND_ENABLED=true\n");
                writer.write("CHESS_LANGUAGE=English\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the main panel with background and layout settings.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    /**
     * Adds the title label to the main panel.
     */
    private void addTitleLabel(JPanel panel) {
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 215, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0);
        panel.add(titleLabel, gbc);
    }

    /**
     * Adds the language dropdown to the main panel.
     */
    private void addLanguageDropdown(JPanel panel) {
        JLabel languageLabel = createLabel("Language: ");

        // Load the current language from the config file
        String currentLanguage = configParser.getValue("CHESS_LANGUAGE", "English");

        languageDropdown = new JComboBox<>(new String[]{"English", "French"});
        languageDropdown.setSelectedItem(currentLanguage);
        styleDropdown(languageDropdown);

        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languagePanel.setBackground(new Color(30, 30, 30));
        languagePanel.add(languageLabel);
        languagePanel.add(languageDropdown);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 10, 0);
        panel.add(languagePanel, gbc);
    }

    /**
     * Adds the piece set dropdown to the main panel.
     */
    private void addPieceSetDropdown(JPanel panel) {
        JLabel pieceSetLabel = createLabel("Piece Set: ");

        // Load the current piece set from the config file
        String currentPieceSet = configParser.getValue("CHESS_PIECE_SET", "Classic");

        pieceSetDropdown = new JComboBox<>(new String[]{"Classic", "Cardinal", "California"});
        pieceSetDropdown.setSelectedItem(currentPieceSet);
        styleDropdown(pieceSetDropdown);

        JPanel pieceSetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pieceSetPanel.setBackground(new Color(30, 30, 30));
        pieceSetPanel.add(pieceSetLabel);
        pieceSetPanel.add(pieceSetDropdown);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 10, 0);
        panel.add(pieceSetPanel, gbc);
    }

    /**
     * Adds the sound toggle checkbox to the main panel.
     */
    private void addSoundToggle(JPanel panel) {
        JLabel soundLabel = createLabel("Sound: ");

        // Load the current sound setting from the config file
        boolean isSoundEnabled = Boolean.parseBoolean(configParser.getValue("CHESS_SOUND_ENABLED", "true"));

        soundToggle = new JCheckBox("Enable Music");
        soundToggle.setSelected(isSoundEnabled);
        styleCheckbox(soundToggle);

        JPanel soundPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        soundPanel.setBackground(new Color(30, 30, 30));
        soundPanel.add(soundLabel);
        soundPanel.add(soundToggle);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 20, 0);
        panel.add(soundPanel, gbc);
    }

    /**
     * Adds the save button to the main panel.
     */
    private void addSaveButton(JPanel panel) {
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(50, 50, 50));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(this::onSave);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(saveButton, gbc);
    }

    /**
     * Handles the save action when the save button is clicked.
     */
    private void onSave(ActionEvent e) {
        String selectedPieceSet = (String) pieceSetDropdown.getSelectedItem();
        String selectedLanguage = (String) languageDropdown.getSelectedItem();
        boolean isSoundEnabled = soundToggle.isSelected();

        // Save settings to the config file
        configParser.setValue("CHESS_PIECE_SET", selectedPieceSet);
        configParser.setValue("CHESS_SOUND_ENABLED", String.valueOf(isSoundEnabled));
        configParser.setValue("CHESS_LANGUAGE", selectedLanguage);

        try {
            configParser.save();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save settings.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Close the dialog
        dispose();
    }

    /**
     * Creates a styled label for the settings panel.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    /**
     * Styles a dropdown to match the UI theme.
     */
    private void styleDropdown(JComboBox<String> dropdown) {
        dropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dropdown.setBackground(new Color(50, 50, 50));
        dropdown.setForeground(Color.WHITE);
    }

    /**
     * Styles a checkbox to match the UI theme.
     */
    private void styleCheckbox(JCheckBox checkbox) {
        checkbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkbox.setBackground(new Color(30, 30, 30));
        checkbox.setForeground(Color.WHITE);
        checkbox.setFocusPainted(false);
    }
}
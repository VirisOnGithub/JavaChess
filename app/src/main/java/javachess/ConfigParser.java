package javachess;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to parse a configuration file.
 */
public class ConfigParser {
    private final Map<String, String> configMap;
    private final String filePath;

    public ConfigParser(String filePath) {
        this.filePath = filePath;
        this.configMap = new HashMap<>();
        load();
    }

    public ConfigParser() {
        this("settings.conf");
    }

    /**
     * Loads the configuration file into memory.
     */
    private void load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // No config file exists yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current configuration to the file.
     */
    public void save() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
    }

    /**
     * Gets a value from the configuration file.
     */
    public String getValue(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue);
    }

    /**
     * Sets a value in the configuration file.
     */
    public void setValue(String key, String value) {
        configMap.put(key, value);
    }

    public Language getLanguage() {
        String language = getValue("CHESS_LANGUAGE", "English");
        return switch (language) {
            case "French" -> Language.FRENCH;
            default -> Language.ENGLISH;
        };
    }
}
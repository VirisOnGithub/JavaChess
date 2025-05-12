package javachess.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
    private static final String baseURL = "https://images.chesscomfiles.com/chess-themes/sounds/_WAV_/default/";
    private static final String[] files = { "game-start", "game-end", "capture", "castle", "move-self", "move-check", "promote", "illegal" };
    private static final String resourcesPath = "app/src/main/resources/audios";

    public static void main(String[] args) {
        initAudios();
    }

    /**
     * Initializes the audio files by checking if they exist in the 'resources' folder.
     * @return true if all files are present, false otherwise.
     */
    public static boolean initAudios() {
        for (String file : files) {
            String fileNameWithExtension = file + ".wav";
            if (!fileExists(resourcesPath + fileNameWithExtension)) {
                System.out.println("Downloading " + file + "...");
                try {
                    downloadFile(fileNameWithExtension);
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Error downloading " + file + ": " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a file exists in the specified path.
     * @param fileNameWithExtension the name of the file with its extension.
     * @return true if the file exists, false otherwise.
     */
    private static boolean fileExists(String fileNameWithExtension) {
        File file = new File(fileNameWithExtension);
        if (file.exists()) {
            System.out.println(fileNameWithExtension + " already exists.");
            return true;
        } else {
            System.out.println(fileNameWithExtension + " does not exist.");
            return false;
        }
    }

    /**
     * Downloads a file from the specified URL and saves it to the 'resources' folder.
     * @param fileName the name of the file to be downloaded (without extension).
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if the URI syntax is incorrect.
     */
    private static void downloadFile(String fileName) throws IOException, URISyntaxException {
        File resourcesDir = new File(resourcesPath);

        // Create the resources directory if it doesn't exist
        if (!resourcesDir.exists()) {
            if (!resourcesDir.mkdirs()) {
                throw new IOException("Unable to create folder: " + resourcesPath);
            }
        }

        // Complete path for the file
        String fileNameWithExtension = fileName.endsWith(".wav") ? fileName : fileName + ".wav";
        File outputFile = new File(resourcesDir, fileNameWithExtension);

        // File URL
        URL fileURL = new URI(baseURL + fileNameWithExtension).toURL();

        // File download
        try (ReadableByteChannel rbc = Channels.newChannel(fileURL.openStream());
         FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }
}
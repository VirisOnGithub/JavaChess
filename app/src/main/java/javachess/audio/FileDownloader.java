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

private static void downloadFile(String fileName) throws IOException, URISyntaxException {
    // Chemin vers le dossier resources
    File resourcesDir = new File(resourcesPath);

    // Crée le dossier resources s'il n'existe pas
    if (!resourcesDir.exists()) {
        if (!resourcesDir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier resources : " + resourcesPath);
        }
    }

    // Chemin complet du fichier dans le dossier resources
    String fileNameWithExtension = fileName.endsWith(".wav") ? fileName : fileName + ".wav";
    File outputFile = new File(resourcesDir, fileNameWithExtension);

    // URL du fichier à télécharger
    URL fileURL = new URI(baseURL + fileNameWithExtension).toURL();

    // Téléchargement du fichier
    try (ReadableByteChannel rbc = Channels.newChannel(fileURL.openStream());
         FileOutputStream fos = new FileOutputStream(outputFile)) {
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
}
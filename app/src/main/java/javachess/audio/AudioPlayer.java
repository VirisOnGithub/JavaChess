package javachess.audio;

import javax.sound.sampled.*;
import java.io.InputStream;

public class AudioPlayer {
    /**
     * Plays an audio file from the 'resources' folder.
     * @param fileName The name of the audio file (without extension) to be played.
     */
    public static void playAudio(String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String resourcePath = "audios/" + fileName + ".wav";
                    InputStream audioStream = AudioPlayer.class.getClassLoader().getResourceAsStream(resourcePath);

                    if (audioStream != null) {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);

                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        clip.drain();
                        clip.close();
                        audioInputStream.close();
                    } else {
                        System.err.println("Audio file not found: " + resourcePath);
                    }
                } catch (Exception e) {
                    System.err.println("Error playing audio: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
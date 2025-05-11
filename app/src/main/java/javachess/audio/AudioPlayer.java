package javachess.audio;

import javax.sound.sampled.*;
import java.io.InputStream;

public class AudioPlayer {
    public static void playAudio(String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String resourcePath = "audios/" + fileName + ".wav";
                    InputStream audioStream = AudioPlayer.class.getClassLoader().getResourceAsStream(resourcePath);

                    if (audioStream != null) {
//                        System.out.println("Audio file found: " + resourcePath);
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);

                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
//                        System.out.println("Playing audio: " + resourcePath);
                        clip.start();
                        // Wait for the audio to finish playing
                        clip.drain();
                        clip.close();
                        audioInputStream.close();
//                        System.out.println("Audio playback finished.");
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
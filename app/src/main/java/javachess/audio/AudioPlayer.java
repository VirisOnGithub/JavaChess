package javachess.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioPlayer {
    // Keep track of active clips to prevent garbage collection
    private static final Map<Integer, Clip> activeClips = new HashMap<>();
    private static final AtomicInteger clipCounter = new AtomicInteger(0);

    /**
     * Plays an audio file from the 'resources' folder.
     * Multiple sounds can play simultaneously.
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
                        // Wrap with BufferedInputStream to support mark/reset
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(audioStream);

                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);

                        // Generate unique ID for this clip
                        final int clipId = clipCounter.getAndIncrement();

                        // Store reference to prevent garbage collection
                        activeClips.put(clipId, clip);

                        // Add listener to clean up resources when clip finishes
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                try {
                                    clip.close();
                                    audioInputStream.close();
                                    activeClips.remove(clipId);
                                } catch (Exception e) {
                                    // Handle cleanup error
                                }
                            }
                        });

                        // Start playing without waiting
                        clip.start();

                        // Notice: We've removed clip.drain() to avoid waiting

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
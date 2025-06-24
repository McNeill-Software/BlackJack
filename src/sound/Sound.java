package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private final File file;
    private final AudioInputStream audioStream;
    private final Clip clip;

    public Sound(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        file = new File(filePath);
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}

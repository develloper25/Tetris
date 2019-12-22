import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.net.URL;

public class TetrisMainSound {

	/** THE URL OF THE FILE*/
	private File file;
	
	/** THE CLIP */
	private Clip clip;
	
	/** AUDIO INPUT STREAM */
	private AudioInputStream audioInputStream;
	
	public TetrisMainSound() {
		initSound();
	}
	
	/** this method inits the sound */
	private void initSound() {
		file = new File("./TetrisTheme.wav");
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file.toURI().toURL());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	protected void playSound() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}

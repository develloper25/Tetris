import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TetrisMainSound {

	/** THE FILE NAME */
	private String fileName;
	
	/** THE CLIP */
	private Clip clip;
	
	/** INPUT STREAM */
	private InputStream inputStream;
	
	/** INPUT STREAM */
	private InputStream bufferedIn;
	
	/** AUDIO INPUT STREAM */
	private AudioInputStream audioStream;
	
	public TetrisMainSound() {
		initSound();
	}
	
	/** this method inits the sound */
	private void initSound() {
		setFileName("TetrisTheme.wav"); 
		try {
			inputStream = getClass().getResourceAsStream(fileName);
			bufferedIn = new BufferedInputStream(inputStream);
			clip = AudioSystem.getClip();
			audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			clip.open(audioStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	protected void playSound() {
		try {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

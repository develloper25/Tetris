import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TetrisMainPanel extends JPanel {

	/** Button for single player Mode */
	private JButton singlePlayerButton;

	/** Button for multi player Mode */
	private JButton multiPlayerButton;

	/** Button for highscore */
	private JButton highscoreButton;

	/** Button for the settings menue */
	private JButton settingsButton;

	/** constructor for the Main Panel which calls the initButtons method */
	public TetrisMainPanel() {
		initButtons() ;
	}

	/**
	 * init the buttons and adds them to the panel
	 */
	private void initButtons() {
		singlePlayerButton = new JButton("Singleplayer");
		multiPlayerButton = new JButton("Multiplayer");
		highscoreButton = new JButton("Highscore");
		settingsButton = new JButton("Settings");
		// set the bounds
		singlePlayerButton.setBounds(115, 90, 150, 30);
		multiPlayerButton.setBounds(115, 150, 150, 30);
		highscoreButton.setBounds(115, 210, 150, 30);
		settingsButton.setBounds(115, 270, 150, 30);
		// add the buttons to the panel
		this.add(singlePlayerButton);
		this.add(multiPlayerButton);
		this.add(highscoreButton);
		this.add(settingsButton);
		this.setLayout(null);
	}



	/**
	 * overrides paintComponent of JPanel here all the graphics is drawn
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackroundImage(g);
	}

	/**
	 * draws the background of our MainMenue
	 * 
	 * @param Graphics g
	 */
	private void drawBackroundImage(Graphics g) {
		Image img = null;
		try {
			img = ImageIO.read(new File("./images.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * 
	 * @return singlePlayerButton
	 */
	public JButton getSinglePlayerButton() {
		return singlePlayerButton;
	}

	/**
	 * 
	 * @param singlePlayerButton
	 */
	public void setSinglePlayerButton(JButton singlePlayerButton) {
		this.singlePlayerButton = singlePlayerButton;
	}

	/**
	 * 
	 * @return multiPlayerButton
	 */
	public JButton getMultiPlayerButton() {
		return multiPlayerButton;
	}

	/**
	 * 
	 * @param multiPlayerButton
	 */
	public void setMultiPlayerButton(JButton multiPlayerButton) {
		this.multiPlayerButton = multiPlayerButton;
	}

	/**
	 * 
	 * @return highscoreButton
	 */
	public JButton getHighscoreButton() {
		return highscoreButton;
	}

	/**
	 * 
	 * @param highscoreButton
	 */
	public void setHighscoreButton(JButton highscoreButton) {
		this.highscoreButton = highscoreButton;
	}

	/**
	 * 
	 * @return settingsButton
	 */
	public JButton getSettingsButton() {
		return settingsButton;
	}

	/**
	 * 
	 * @param settingsButton
	 */
	public void setSettingsButton(JButton settingsButton) {
		this.settingsButton = settingsButton;
	}

}

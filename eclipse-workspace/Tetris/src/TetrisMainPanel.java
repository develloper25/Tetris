import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TetrisMainPanel extends JPanel {

	/** A logger for better debugging */
	private static final Logger log = Logger.getLogger(TetrisMainPanel.class.getName());
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
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream("images.jpg"));
		} catch (IOException e) {
			log.info("can´t find or open images.jpg");
			e.printStackTrace() ;
		}
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		// draw special christmas image
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream("santa.png"));
		} catch (IOException e) {
			log.info("can´t find or open santa.png");
			e.printStackTrace();
		}
		g.drawImage(img, 100, 0, getWidth() -50, getHeight()-50, null);
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

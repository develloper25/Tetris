import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.Timer;

public class TetrisMainWindow extends JFrame {
	
	/** The main Panel of our Tetris Game */
	private static TetrisMainPanel mainMenuePanel;

	/** The main Frame of our Tetris Game */
	private static TetrisMainWindow mainClass;
	
	
	public static void main(String[] args) {
		mainMenuePanel = new TetrisMainPanel();
		mainClass = new TetrisMainWindow();
		mainClass.getContentPane().add(mainMenuePanel);
	}

	/**
	 * the constructor for our mainWindow
	 */
	public TetrisMainWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Tetris");
		setVisible(true);
		setSize(400, 425);
		setLocationRelativeTo(null);
		ButtonListener listener = new ButtonListener();
		initButtonListener(listener);
		setResizable(false);
	}

	/**
	 * the main menue listener class
	 * @author Matthias 
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(getMainMenuePanel().getSinglePlayerButton())) {
				initSinglePlayerGame();
			} else if (e.getSource().equals(getMainMenuePanel().getMultiPlayerButton())) {

			}
		}
	}
	
	
	

	/**
	 * inits the single player Game
	 */
	private void initSinglePlayerGame() {
		getMainClass().getContentPane().remove(getMainMenuePanel());
		TetrisGameField field = new TetrisGameField(10, 20, 15, 500);
		int idFirstStone = field.getRandomNumberForStone(7);
		int innerRectangleId = field.getRandomNumberForStone(5);
		TetrisStone stone = new TetrisStone(idFirstStone,innerRectangleId);
		field.setStone(stone);
		getMainClass().getContentPane().add(field);
		field.setFocusable(true);
		field.requestFocusInWindow();
		field.addKeyListener(field);
		getMainClass().getContentPane().revalidate();
		
		new Thread() {
			public void run() {
				mainLoop(field);
			}
		}.start();
	}
	

	/**
	 * this is the main loop of our game
	 * @param field
	 */
	public void mainLoop(TetrisGameField field) {

		boolean gameIsRunning = true;

		 ActionListener actListner = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					doEveryTick(field);
				}
				 
			 };
		
		Timer timer = new Timer(field.getSpeed(), actListner);
		field.setTimer(timer);
		timer.start();
		
		while (gameIsRunning) {
			try {
				if(field.isTimerLayDown()) {
					Timer layDownTimer = new Timer(50,actListner);
					initLayDownTimer(field, layDownTimer);
				}
				// give the Thread time to load
				Thread.sleep(1);
				repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * inits the layDownTimer to lay down one stone
	 * cause we can´t just set the delay 
	 * @param field
	 * @param layDownTimer
	 */
	private void initLayDownTimer(TetrisGameField field, Timer layDownTimer) {
		field.getTimer().stop();
		field.setTimer(layDownTimer);
		field.setTimerLayDown(false);
		layDownTimer.start();
	}
	
	/**
	 * this method gets called every time the timer ticks
	 * @param field
	 */
	private void doEveryTick(TetrisGameField field) {
			if (field.touchesAnotherStone(0)) {
				field.layDownAndCreateNewStone();
			} else {
				if (field.touchesGround()) {
					field.layDownAndCreateNewStone();
				}else {
					field.moveStoneOneField(0, field.getStone().getStoneParts());
				}
			}
	}


	/**
	 * creates a Listener and adds it to the buttons
	 */
	private void initButtonListener(ButtonListener listener) {
		getMainMenuePanel().getSinglePlayerButton().addActionListener(listener);
		getMainMenuePanel().getMultiPlayerButton().addActionListener(listener);
		getMainMenuePanel().getHighscoreButton().addActionListener(listener);
		getMainMenuePanel().getSettingsButton().addActionListener(listener);
	}

	/**
	 * 
	 * @return
	 */
	public static TetrisMainPanel getMainMenuePanel() {
		return mainMenuePanel;
	}

	/**
	 * 
	 * @param mainMenuePanel
	 */
	public static void getMainMenuePanel(TetrisMainPanel mainMenuePanel) {
		TetrisMainWindow.mainMenuePanel = mainMenuePanel;
	}

	/**
	 * 
	 * @return
	 */
	public static TetrisMainWindow getMainClass() {
		return mainClass;
	}

	/**
	 * 
	 * @param mainClass
	 */
	public static void setMainClass(TetrisMainWindow mainClass) {
		TetrisMainWindow.mainClass = mainClass;
	}


}

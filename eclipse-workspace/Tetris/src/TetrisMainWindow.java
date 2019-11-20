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
	
	/** A logger for better debugging */
	private static final Logger log = Logger.getLogger(TetrisMainWindow.class.getName());

	/** The main Panel of our Tetris Game */
	private static TetrisMainPanel mainMenuePanel;

	/** The main Frame of our Tetris Game */
	private static TetrisMainWindow mainClass;
	
	/** Our Game Field */
	private TetrisGameField field;

	
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
	
	private class FieldKeyListener implements KeyListener {
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				if (getField().movePossible(1) && !getField().touchesAnotherStone(1)) {
					getField().moveStoneOneField(1, getField().getStone().getStoneParts());
				}
			} else if (key == KeyEvent.VK_RIGHT) {
				if (getField().movePossible(2) && !getField().touchesAnotherStone(2)) {
					getField().moveStoneOneField(2, getField().getStone().getStoneParts());
				}
			} else if (key == KeyEvent.VK_UP) {
					if(!getField().touchesAnotherStone(3) && !getField().touchesGroundAfterMoving()) {
						getField().rotateStone();
						
					}
					ArrayList<Rectangle> stoneParts = field.getStone().getStoneParts();
					
					log.info("X1:" + String.valueOf(stoneParts.get(0).getX()));
					log.info("X2:" + String.valueOf(stoneParts.get(1).getX()));
					log.info("X3:" + String.valueOf(stoneParts.get(2).getX()));
					log.info("X4:" + String.valueOf(stoneParts.get(3).getX()));

			} else if (key == KeyEvent.VK_DOWN) {
				getField().getTimer().setDelay(50);
				getField().setLayDownKeyPressed(true);
			}else if(key == KeyEvent.VK_P) {
				if(!getField().isGamePaused()) {
					getField().setGamePaused(true); 
					getField().getTimer().stop();
				}else {
					getField().setGamePaused(false);
					getField().getTimer().restart();
				}
			}

		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	}
	

	/**
	 * inits the single player Game
	 */
	private void initSinglePlayerGame() {
		getMainClass().getContentPane().remove(getMainMenuePanel());
		field = new TetrisGameField(10, 20, 15, 500);
		int idFirstStone = getField().getRandomNumberForStone(7);
		int innerRectangleId = getField().getRandomNumberForStone(5);
		TetrisStone stone = new TetrisStone(idFirstStone,innerRectangleId);
		getMainClass().setFocusable(true);
		getMainClass().addKeyListener(new FieldKeyListener());
		getField().setStone(stone);
		getMainClass().getContentPane().add(getField());
		getMainClass().requestFocusInWindow();
		getMainClass().getContentPane().revalidate();
		
		new Thread() {
			public void run() {
				mainLoop(field);
			}
		}.start();
	}
	

	/** 
	 * 
	 * 
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
				Thread.sleep(10);
				repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * 
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

	/**
	 * 
	 * @return
	 */
	public TetrisGameField getField() {
		return field;
	}

	/**
	 * 
	 * @param field
	 */
	public void setField(TetrisGameField field) {
		this.field = field;
	}
}

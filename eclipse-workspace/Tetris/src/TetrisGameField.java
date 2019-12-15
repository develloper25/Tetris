import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;


public class TetrisGameField extends JPanel implements KeyListener {

	/** A logger for better debugging */
	private static final Logger log = Logger.getLogger(TetrisGameField.class.getName());
	
	/** xMax value */
	private int xMax;

	/** yMax value*/
	private int yMax;

	/** the width and height of a stone part rectangle */
	private int kWidthAndHeight;

	/** speed of the stones */
	private int speed;

	/** the width of our field*/
	private int gameFieldWidth;

	/** the height of our field */
	private int gameFieldHeight;

	/** the upper edge x start coordinates */
	private int upperEdgeXcoord;
	
	/** the upper edge y start coordinates */
	private int upperEdgeYcoord;
	
	/** our current stone */
	private TetrisStone stone;

	/** the stones which already felt down */
	private ArrayList<TetrisStone> stonesFallen = new ArrayList<TetrisStone>();
	
	/** a copy of the stone parts */
	private ArrayList<Rectangle> stonePartsCopy = new ArrayList<Rectangle>();

	/** a list which contains the integer values of the rows full*/
	private ArrayList<Integer> rowsFull = new ArrayList<Integer>();
	
	/** our timer */
	private Timer timer;
	
	/** defines if the game is paused */
	private boolean gamePaused;
	
	/** the lay down key pressed value */
	private boolean layDownKeyPressed ;
	
	/** the lay down timer value */
	private boolean timerLayDown;
	
	/** Our Game field which indicates what stone felt down and where*/
	private boolean [][] gameField;

	/** Our Game field values */
	private TetrisGameFieldCoord[][] valuesOfField;
	
	
	
	/**
	 * constructor for a new Game Field
	 * @param xMax
	 * @param yMax
	 * @param kWidth
	 * @param speed
	 */
	public TetrisGameField(int xMax, int yMax, int kWidth, int speed) {
		this.xMax = xMax;
		this.yMax = yMax;
		this.kWidthAndHeight = kWidth;
		this.speed = speed;
		this.setLayout(null);
		initGameField();
	}

	
	/** 
	 * inits the Game Field values
	 * 
	 */
	private void initGameField() {
		setGameFieldWidth(xMax * getKwidthAndHeight() );
		setGameFieldHeight(yMax * getKwidthAndHeight() );
		setUpperEdgeXcoord(60);
		setUpperEdgeYcoord(70);
		setTimerLayDown(false);
		setLayDownKeyPressed(false);
		setGamePaused(false);
		gameField = new boolean[getyMax()][getxMax()];
		valuesOfField = new TetrisGameFieldCoord[getyMax()][getxMax()];
		initGameFieldCoords();
	}
	
	
	private void initGameFieldCoords() {
		int xStart = 61;
		int yStart = 296;
		
			for(int k = 0; k < getyMax();k++) {
				for(int i = 0; i < getxMax();i++) {
					TetrisGameFieldCoord coordinates = new TetrisGameFieldCoord();
					coordinates.setxCoord(xStart);
					coordinates.setyCoord(yStart);
					valuesOfField[k][i]= coordinates;
					xStart += 16;
			}
			xStart = 61;
			yStart -= 16;
		}
	}

	/**
	 * the main painting method where all the drawing takes place
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		g2d.setColor(Color.GRAY);
		// Spielfeld Rand zeichnen links
		g2d.fillRect(40, getUpperEdgeYcoord(), 20, gameFieldHeight);
		// Spielfeld Rand zeichnen rechts
		g2d.fillRect(gameFieldWidth + getUpperEdgeXcoord(), getUpperEdgeYcoord(), 20, gameFieldHeight);
		// Spielfeld Rand zeichnen unten
		Color brown = new Color(205, 133, 63);
		g2d.setColor(brown);
		g2d.fillRect(40, gameFieldHeight + getUpperEdgeYcoord(), gameFieldWidth + getUpperEdgeXcoord() - 20, 2);
		g2d.setColor(Color.white);
		// Spielfeld füllen
		g2d.fillRect(getUpperEdgeXcoord(), getUpperEdgeYcoord(), gameFieldWidth - 1, gameFieldHeight - 1);
		// Stein zeichnen
		paintStone(g2d);
		// paint stones which already felt down
		paintStonesFallen(g2d);
	}

	/**
	 * this method paints the stones which already felt down
	 * 
	 * @param g2d
	 */
	private void paintStonesFallen(Graphics2D g2d) {
		for (int i = 0; i < getStonesFallen().size(); i++) {
			TetrisStone currentStone = getStonesFallen().get(i);
			for (int k = 0; k < currentStone.getStoneParts().size(); k++) {
				g2d.setColor(currentStone.getColor());
				Rectangle actRect = currentStone.getStoneParts().get(k);
				g2d.fill(actRect);
				paintStoneOutline(actRect, g2d);
				paintStoneInnerRectangles(actRect, g2d, currentStone.getInnerRectangleId());
			}
		}
	}

	/**
	 * this method paints the actual stone
	 * 
	 * @param g2d
	 */
	private void paintStone(Graphics2D g2d) {
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		for (int i = 0; i < stoneParts.size(); i++) {
			Rectangle actRect = stoneParts.get(i);
			// just draw the parts which are inside the field
			if (actRect.getY() > 26) {
				g2d.setColor(getStone().getColor());
				g2d.fill(actRect);
				paintStoneOutline(actRect, g2d);
				paintStoneInnerRectangles(actRect, g2d,getStone().getInnerRectangleId());
			}
		}
	}

	/**
	 * draws the outline of the stones
	 * 
	 * @param actRect the actual rectangle of the list
	 * @param g2d     our graphics object
	 */
	private void paintStoneOutline(Rectangle actRect, Graphics2D g2d) {
		// outline color is always black
		g2d.setColor(Color.black);
		int xVal = (int) (actRect.getX() - 1);
		int yVal = (int) (actRect.getY() - 1);
		int width = (int) (actRect.getWidth() + 1);
		int height = (int) (actRect.getHeight() + 1);
		g2d.drawRect(xVal, yVal, width, height);
	}
	
	/** 
	 * 
	 * @param actRect
	 * @param g2d
	 * @param innerRectangleId
	 */
	private void paintStoneInnerRectangles(Rectangle actRect,Graphics2D g2d,int innerRectangleId) {
		if (innerRectangleId == 1) {
			drawInnerRectanglesWhiteAndBlack(actRect, g2d);
		} else if (innerRectangleId == 2) {
			drawInnerRectanglesLitteBlack(actRect, g2d);
		} else if (innerRectangleId == 3) {
			drawInnerRectanglesFilledWhite(actRect, g2d);
		} else if (innerRectangleId == 4) {
			drawInnerRectanglesFilledBlack(actRect, g2d);
		} 
		// if innerRectangleId == 5 do nothing
	}
	
	/**
	 * this method draws the inner Rectangles for the stones
	 * which are white and black using just simple lines
	 * @param actRect
	 * @param g2d
	 */
	private void drawInnerRectanglesWhiteAndBlack(Rectangle actRect,Graphics2D g2d) {
		// get Values of current Rectangle (x and y)
		int xValRect = (int)actRect.getX();
		int yValRect = (int)actRect.getY();
		// draw our rectangles now
		g2d.setColor(Color.white);
		g2d.drawLine(xValRect + 3, yValRect + 3, xValRect + 10, yValRect + 3);
		g2d.drawLine(xValRect + 3, yValRect + 3, xValRect + 3, yValRect + 10);
		g2d.setColor(Color.black);
		g2d.drawLine(xValRect + 3, yValRect + 10, xValRect + 10, yValRect + 10);
		g2d.drawLine(xValRect + 10, yValRect + 4, xValRect + 10, yValRect + 10);
	}
	
	/**
	 * 
	 * @param actRect
	 * @param g2d
	 */
	private void drawInnerRectanglesLitteBlack(Rectangle actRect,Graphics2D g2d) {
		g2d.setColor(Color.black);
		int xVal = (int) (actRect.getX() + 5);
		int yVal = (int) (actRect.getY() + 5);
		int width = (int) (actRect.getWidth() - 10);
		int height = (int) (actRect.getHeight() - 10);
		g2d.fillRect(xVal, yVal, width, height);
	}
	
	/**
	 * 
	 * @param actRect
	 * @param g2d
	 */
	private void drawInnerRectanglesFilledWhite(Rectangle actRect,Graphics2D g2d) {
		g2d.setColor(Color.black);
		int xVal = (int) (actRect.getX() + 3);
		int yVal = (int) (actRect.getY() + 3);
		int width = (int) (actRect.getWidth() - 7);
		int height = (int) (actRect.getHeight() - 7);
		g2d.drawRect(xVal,yVal,width,height);
		g2d.setColor(Color.white);
		g2d.fillRect(xVal + 1,yVal +1 ,width -1,height - 1);
	}
	
	/**
	 * this method draws the rectangles which are just black
	 * @param actRect
	 * @param g2d
	 */
	private void drawInnerRectanglesFilledBlack(Rectangle actRect,Graphics2D g2d) {
		g2d.setColor(Color.black);
		int xVal = (int) (actRect.getX() + 3);
		int yVal = (int) (actRect.getY() + 3);
		int width = (int) (actRect.getWidth() - 6);
		int height = (int) (actRect.getHeight() - 6);
		g2d.fillRect(xVal, yVal, width, height);
	}
	
	/**
	 * 
	 * @return if the stone touches the ground
	 */
	public boolean touchesGround() {
		boolean touches = false;
		for (int i = 0; i < getStone().getStoneParts().size(); i++) {
			Rectangle currentRect = getStone().getStoneParts().get(i);
			if (currentRect.getY() >= getGameFieldHeight() + 56) {
				touches = true;
				break;
			}
		}
		return touches;
	}

	/**
	 * @throws CloneNotSupportedException
	 * 
	 */
	protected void layDownStone() throws CloneNotSupportedException {
		TetrisStone stoneFallen = null;
		try {
			 stoneFallen = (TetrisStone) getStone().clone();
		}catch(CloneNotSupportedException ex) {
			throw new CloneNotSupportedException("Can´t create a clone of stone : " + ex);
		}
		getStonesFallen().add(stoneFallen);
	} 

	/**
	 * This method resets the stone parts
	 */
	protected void resetStone() {
		// reset our values for the rotation
		getStone().setStoneRotated(false);
		getStone().setStoneRotateDirection(0);
		// copy the parts into a new list
		ArrayList<Rectangle> stonePartsFallen = new ArrayList<>(getStone().getStoneParts());
		getStone().getStoneParts().clear();
		int size = getStonesFallen().size();
		// set this list as the list for the last stone which felt down
		getStonesFallen().get(size - 1).setStoneParts(stonePartsFallen);
	}

	/**
	 * this method determines if you can move the stone left or right if the stone
	 * is outside the field it returns false
	 * 
	 * @param loc the location where the stone should be moved
	 * @return if the move is possible
	 */
	protected boolean movePossible(int loc) {
		boolean moveIsPossible = true;
		int count = 0;
		for (int i = 0; i < getStone().getStoneParts().size(); i++) {
			Rectangle currentRect = getStone().getStoneParts().get(i);
			if (loc == 1 && currentRect.getX() <= 61) {
				count++;
			} else if (loc == 2 && currentRect.getX() > 189) {
				count++;
			}
		}
		if (count > 0) {
			moveIsPossible = false;
		}
		return moveIsPossible;
	}

	/**
	 * 
	 * @param loc
	 * @return
	 */
	protected boolean touchesAnotherStone(int loc) {
		boolean touchesStone = false;

		saveStoneParts();
		
		ArrayList<Rectangle> stoneParts = new ArrayList<Rectangle>(getStone().getStoneParts());
		
		if(loc != 3) {
			moveStoneOneField(loc, stoneParts);
		}else {
			rotateStone();
		}
		
		for (int i = 0; i < getStonesFallen().size(); i++) {
			ArrayList<Rectangle> stoneFallenParts = getStonesFallen().get(i).getStoneParts();
			if (partsTouchesParts(loc, stoneFallenParts)) {
				touchesStone = true;
				break;
			}
		}
		// move the stone back after watching
		setStoneBackToCopy();
		if(loc == 3) {
			resetStoneRotateValues();
		}
		
		return touchesStone;

	}

	/**
	 * 
	 * @param loc
	 * @param stoneFallenParts
	 * @return
	 */
	private boolean partsTouchesParts(int loc, ArrayList<Rectangle> stoneFallenParts) {
		boolean touches = false;
		ArrayList<Rectangle> stoneParts = new ArrayList<Rectangle>(getStone().getStoneParts());
		
		for (int l = 0; l < stoneFallenParts.size(); l++) {
			for (int m = 0; m < stoneParts.size(); m++) {
				if (stoneFallenParts.get(l).intersects(stoneParts.get(m))) {
					touches = true;	
					break;
				}
			}
		}
		return touches;
	}
	
	/**
	 * this method watches if the stone touches the ground after moving
	 * @return touchesGround
	 */
	protected boolean touchesGroundAfterMoving() {
		boolean touchesGround = false;
		saveStoneParts();
		rotateStone();
		
		if(touchesGround()) {
			touchesGround = true;
		}
		setStoneBackToCopy();
		resetStoneRotateValues();
		
		return touchesGround;
	}
	
	/**
	 * resets the stone rotate values after watching
	 */
	private void resetStoneRotateValues() {
		boolean rotated = getStone().isStoneRotated();
		
		if(rotated == true) {
			getStone().setStoneRotated(false);
		}else {
			getStone().setStoneRotated(true);
		}
		
		int rotateDirection = getStone().getStoneRotateDirection();
		
		if(rotateDirection > 0) {
			getStone().setStoneRotateDirection(rotateDirection - 1);
		}
	}

	/**
	 * this method rotates the stone if the up key was pressed
	 * each stone id has their own rotate method
	 */
	protected void rotateStone() {
		
		int width = getStone().getStoneWidth();
		int height = getStone().getStoneHeight();
		
		if(getStone().getId() == 1) {
			rotateIstone(width, height);
		}else if(getStone().getId() == 3) {
			rotateTstone(width, height);
		}else if(getStone().getId() == 4) {
			rotateSstone(width, height);
		} else if(getStone().getId() == 5) {
			rotateZstone(width, height);
		} else if(getStone().getId() == 6) {
			rotateJstone(width, height);
		}else if(getStone().getId() == 7) {
			rotateLstone(width, height);
		}
		
		if(!getStone().isStoneRotated()) {
			getStone().setStoneRotated(true);
		}else if(getStone().isStoneRotated()) {
			getStone().setStoneRotated(false);
		} 
		// the i stone has his own move border method, o stone does not need any logic here
		if(getStone().getId() != 1 && getStone().getId() != 2) {
			moveStoneFromBorder();
			// just 3 stones have a stoneRotateDirection we need to count 
			if(getStone().getId() == 3 || getStone().getId() == 6 || getStone().getId() == 7 ) {
				int stoneRotateDirection = getStone().getStoneRotateDirection();
				// finally set the direction, if it is 4 reset it to 0
				if(stoneRotateDirection == 4) {
					getStone().setStoneRotateDirection(0);
				}else {
					getStone().setStoneRotateDirection(stoneRotateDirection + 1);
				}
			}
		}
		
	}
	
	/**
	 * this method rotates the I Stone (id = 1)
	 * @param xVal
	 * @param yVal
	 */
	private void rotateIstone(int stonePartWidth, int stonePartHeight) {
		int xVal = 0;
		int yVal = 0;
		
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		
		if (!getStone().isStoneRotated()) {
			rotateStonePart(stonePartWidth, stonePartHeight, 3, 1, 2);
			rotateStonePart(stonePartWidth, stonePartHeight, 1, 3, 2);
			xVal = (int) stoneParts.get(2).getX() + getKwidthAndHeight() * 2;
			yVal = (int) stoneParts.get(2).getY();
		} else if (getStone().isStoneRotated()) {
			rotateStonePart(stonePartWidth, stonePartHeight, 3, 0, 2);
			rotateStonePart(stonePartWidth, stonePartHeight, 1, 2, 2);
			xVal = (int) stoneParts.get(2).getX();
			yVal = (int) stoneParts.get(2).getY() - getKwidthAndHeight() * 2;
		}
		getStone().getStoneParts().get(0).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
		
		moveIStoneFromBorder();

	}
	
	
	/**
	 * this method rotates the T stone (id = 3)
	 * @param xVal
	 * @param yVal
	 * @param width
	 * @param height
	 */
	private void rotateTstone(int width, int height) {
	
		int stoneRotateDirection = getStone().getStoneRotateDirection();
	
		if (stoneRotateDirection == 0) {
			rotateStonePart(width, height, 0, 3, 1);
			rotateStonePart(width, height, 3, 0, 1);
			rotateStonePart(width, height, 2, 1, 1);
		} else if (stoneRotateDirection == 1) {
			rotateStonePart(width, height, 2, 2, 1);
			rotateStonePart(width, height, 3, 1, 1);
			rotateStonePart(width, height, 0, 0, 1);
		} else if (stoneRotateDirection == 2) {
			rotateStonePart(width, height, 2, 3, 1);
			rotateStonePart(width, height, 3, 2, 1);
			rotateStonePart(width, height, 0, 1, 1);
		} else if (stoneRotateDirection == 3) {
			rotateStonePart(width, height, 2, 0, 1);
			rotateStonePart(width, height, 3, 3, 1);
			rotateStonePart(width, height, 0, 2, 1);
		}
	}
	
	/**
	 * this method rotates the s stone (id = 4)
	 * @param stonePartWidth
	 * @param stonePartHeight
	 */
	private void rotateSstone(int stonePartWidth, int stonePartHeight) {
		int xVal = 0;
		int yVal = 0;
		int count = 0;

		while (count < 2) {
			if (count == 0) {
				xVal = (int) getStone().getStoneParts().get(count).getX();
				if (!getStone().isStoneRotated()) {
					yVal = (int) getStone().getStoneParts().get(count).getY() + getKwidthAndHeight() * 2;
				} else {
					yVal = (int) getStone().getStoneParts().get(count).getY() - getKwidthAndHeight() * 2;
				}
			} else if (count == 1) {
				yVal = (int) getStone().getStoneParts().get(count).getY();
				if (!getStone().isStoneRotated()) {
					xVal = (int) getStone().getStoneParts().get(count).getX() + getKwidthAndHeight() * 2;
				} else {
					xVal = (int) getStone().getStoneParts().get(count).getX() - getKwidthAndHeight() * 2;
				}
			}
			getStone().getStoneParts().get(count).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
			count++;
		}
	}
	
	/**
	 * this method rotates the Z stone (id = 5)
	 * 
	 * @param stonePartWidth
	 * @param stonePartHeight
	 */
	private void rotateZstone(int stonePartWidth, int stonePartHeight) {
		int xVal = 0;
		int yVal = 0;
		int count = 0;

		while (count < 2) {
			if (count == 0) {
				xVal = (int) getStone().getStoneParts().get(count).getX();
				if (!getStone().isStoneRotated()) {
					yVal = (int) getStone().getStoneParts().get(count).getY() + getKwidthAndHeight() * 2;
				} else {
					yVal = (int) getStone().getStoneParts().get(count).getY() - getKwidthAndHeight() * 2;
				}
				getStone().getStoneParts().get(count).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
			} else if (count == 1) {
				yVal = (int) getStone().getStoneParts().get(3).getY();
				if (!getStone().isStoneRotated()) {
					xVal = (int) getStone().getStoneParts().get(3).getX() + getKwidthAndHeight() * 2;
				} else {
					xVal = (int) getStone().getStoneParts().get(3).getX() - getKwidthAndHeight() * 2;
				}
				getStone().getStoneParts().get(3).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
			}
			count++;
		}
	}
	
	/**
	 * this method rotates the J stone (id = 6)
	 * @param stonePartWidth
	 * @param stonePartHeight
	 */
	private void rotateJstone(int stonePartWidth, int stonePartHeight) {
		int xVal = 0;
		int yVal = 0;
		
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		
		int stoneRotateDirection = getStone().getStoneRotateDirection();
		
		if(stoneRotateDirection == 0) {
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 1, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 3, 1);
			xVal = (int)stoneParts.get(3).getX();
			yVal = (int)stoneParts.get(3).getY() + getKwidthAndHeight() * 2;
		}else if(stoneRotateDirection == 1) {
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 2, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 0, 1);
			xVal = (int)stoneParts.get(3).getX() - getKwidthAndHeight() * 2;
			yVal = (int)stoneParts.get(3).getY();
		}else if(stoneRotateDirection == 2) {
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 1, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 3, 1);
			xVal = (int)stoneParts.get(3).getX();
			yVal = (int)stoneParts.get(3).getY() - getKwidthAndHeight() * 2;
		}else if(stoneRotateDirection == 3) {
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 2, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 0, 1);
			xVal = (int)stoneParts.get(3).getX() + getKwidthAndHeight() * 2;
			yVal = (int)stoneParts.get(3).getY() ;
		}

		getStone().getStoneParts().get(3).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
		
	}
	
	
	/**
	 * this method rotates the L stone (id = 7)
	 * @param stonePartWidth
	 * @param stonePartHeight
	 */
	private void rotateLstone(int stonePartWidth, int stonePartHeight) {
		int xVal = 0;
		int yVal = 0;
		
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		
		int stoneRotateDirection = getStone().getStoneRotateDirection();
		
		if(stoneRotateDirection == 0) {
			xVal = (int)stoneParts.get(3).getX() + getKwidthAndHeight() * 2;
			yVal = (int)stoneParts.get(3).getY();
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 3, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 1, 1);
		}else if(stoneRotateDirection == 1) {
			xVal = (int)stoneParts.get(3).getX();
			yVal = (int)stoneParts.get(3).getY() + getKwidthAndHeight() * 2;
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 2, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 0, 1);
		}else if(stoneRotateDirection == 2) {
			xVal = (int)stoneParts.get(3).getX() - getKwidthAndHeight() * 2;
			yVal = (int)stoneParts.get(3).getY() ;
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 3, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 1, 1);
		}else if(stoneRotateDirection == 3) {
			xVal = (int)stoneParts.get(3).getX();
			yVal = (int)stoneParts.get(3).getY() - getKwidthAndHeight() * 2;
			rotateStonePart(stonePartWidth, stonePartHeight, 2, 0, 1);
			rotateStonePart(stonePartWidth, stonePartHeight, 0, 2, 1);
		}
		getStone().getStoneParts().get(3).setRect(xVal, yVal, stonePartWidth, stonePartHeight);
		
	}
	
	/**
	 * this method rotates a Stone Part to the direction
	 * and sets it´s original part
	 * @param width
	 * @param height
	 * @param stonePartToRotate
	 * @param direction 0 stands for middle down, 1 stands for left, 2 stands for middle up
	 * 3 stands for right down 
	 * @param stoneRotationPoint the rotation Point of the stone as stonePartRectangle
	 */
	private void rotateStonePart( int width, int height,int stonePartToRotate,int direction,int stoneRotationPoint) {
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		int xVal = 0;
		int yVal = 0;
		if(direction == 0) {
			 xVal = (int)stoneParts.get(stoneRotationPoint).getX();
			 yVal = (int)stoneParts.get(stoneRotationPoint).getY() + getKwidthAndHeight();
		}else if(direction == 1) {
			xVal = (int)stoneParts.get(stoneRotationPoint).getX() - getKwidthAndHeight();
			yVal = (int)stoneParts.get(stoneRotationPoint).getY();
		}else if(direction == 2) {
			xVal = (int)stoneParts.get(stoneRotationPoint).getX();
			yVal = (int)stoneParts.get(stoneRotationPoint).getY() - getKwidthAndHeight();
		}else if(direction == 3) {
			xVal = (int)stoneParts.get(stoneRotationPoint).getX() + getKwidthAndHeight();
			yVal = (int)stoneParts.get(stoneRotationPoint).getY();
		}
		getStone().getStoneParts().get(stonePartToRotate).setRect(xVal, yVal, width, height);
	}
	

	/**
	 * this method moves any other stone than the I stone from the border
	 * (I stone has his own method)
	 */
	private void moveStoneFromBorder() {
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		int i = 0;
		boolean hasToMoveFromBorder = false;
		
		while(i < stoneParts.size() && !hasToMoveFromBorder) {
			if(stoneParts.get(i).getX() < 61) {
				moveStoneOneField(2, stoneParts);
				hasToMoveFromBorder = true;
			}else if(stoneParts.get(i).getX() > 205) {
				moveStoneOneField(1, stoneParts);
				hasToMoveFromBorder = true;
			}
			i++;
		}
	}
	
	/** 
	 * this method saves a copy of the stoneParts
	 * in a new Rectange list
	 */
	private void saveStoneParts() {
		ArrayList<Rectangle> saveParts = new ArrayList<Rectangle>();
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		for(int i = 0; i < getStone().getStoneParts().size();i++) {
			int xVal = (int)stoneParts.get(i).getX();
			int yVal = (int)stoneParts.get(i).getY();
			int width = (int)stoneParts.get(i).getWidth();
			int height = (int)stoneParts.get(i).getHeight();
			Rectangle rect = new Rectangle(xVal,yVal,width,height);
			saveParts.add(rect);
		}
		if(!getStonePartsCopy().isEmpty()) {
			getStonePartsCopy().clear();
		}
		setStonePartsCopy(saveParts);
	}
	
	/**
	 * this method sets the stone back to the saved location
	 */
	private void setStoneBackToCopy() {
		ArrayList<Rectangle> savedParts = getStonePartsCopy();
		if(!getStone().getStoneParts().isEmpty()) {
			getStone().getStoneParts().clear();
		}
		for(int i = 0; i < savedParts.size();i++) {
			int xVal = (int)savedParts.get(i).getX();
			int yVal = (int)savedParts.get(i).getY();
			int width = (int)savedParts.get(i).getWidth();
			int height = (int)savedParts.get(i).getHeight();
			Rectangle rect = new Rectangle(xVal,yVal,width,height);
			getStone().getStoneParts().add(rect);
		}
	}
	
	/**
	 * this method moves the I Stone from the border 
	 * while rotating
	 */
	private void moveIStoneFromBorder() {
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		if(stoneParts.get(stoneParts.size() - 1).getX() <= 45 && (!getStone().isStoneRotated())) {
			moveStoneOneField(2, stoneParts);
		}else if(stoneParts.get(stoneParts.size() - 1).getX() >= 189 && (!getStone().isStoneRotated())) {
			moveStoneOneField(1, stoneParts);
			moveStoneOneField(1, stoneParts);
		}else if(stoneParts.get(stoneParts.size() - 1).getX() >= 173 && (!getStone().isStoneRotated())) {
			moveStoneOneField(1, stoneParts);
		}
	}
	
	/**
	 * moves the stone one field
	 * 
	 * @param loc the location where the stone should move 0 stands for down 1
	 *            stands for left 2 stands for right 3 stands for up
	 */
	protected void moveStoneOneField(int loc, ArrayList<Rectangle> stoneParts) {
		for (int i = 0; i < stoneParts.size(); i++) {
			Rectangle currentRect = stoneParts.get(i);
			if (loc == 0) {
				currentRect.setLocation((int) currentRect.getX(), (int) currentRect.getY() + getKwidthAndHeight());
			} else if (loc == 1) {
				currentRect.setLocation((int) currentRect.getX() - getKwidthAndHeight(), (int) currentRect.getY());
			} else if (loc == 2) {
				currentRect.setLocation((int) currentRect.getX() + getKwidthAndHeight(), (int) currentRect.getY());
			} else if (loc == 3) {
				currentRect.setLocation((int) currentRect.getX(), (int) currentRect.getY() - getKwidthAndHeight());
			}
		}
	}
	
	/**
	 * this method remembers the position of every stone which felt down
	 */
	private void rememberStoneFallenPosition() {
		ArrayList<Rectangle> stoneParts = getStone().getStoneParts();
		
		for(int i = 0; i < getyMax(); i++) {
			for(int k = 0; k < getxMax();k++) {
				int xFieldCoord = getValuesOfField()[i][k].getxCoord();
				int yFieldCoord = getValuesOfField()[i][k].getyCoord();
				
				for(int m = 0; m < stoneParts.size();m++) {
					if( xFieldCoord == stoneParts.get(m).getX() && yFieldCoord == stoneParts.get(m).getY()) {
						gameField[i][k]=true;
					}
				}
			}
		}
	}
	
	/**
	 * this method indiactes which rows are full
	 */
	private void findFullRows() {
		
		int columnCount = 0;
		
		ArrayList<Integer> rows = new ArrayList<Integer>();
		
		for(int i = 0; i < getyMax();i++) {
			for(int k = 0; k < getxMax();k++) {
				if(getGameField()[i][k] == true) {
					columnCount ++;
				}
			}
			//now a row is full
			if(columnCount == getxMax() ) {
				// remember which row it is !!!
				rows.add(i);
			}
			columnCount = 0;
		}
		setRowsFull(rows);
	}

	/**
	 * this method deletes the full rows
	 */
	private void deleteFullRows() {
		
		for(int s = getRowsFull().size() - 1; s >= 0;s--) {
			
			int row = getRowsFull().get(s);
			int yVal = getValuesOfField()[row][0].getyCoord();
			
			for(int k = 0; k < stonesFallen.size();k++) {
				TetrisStone stoneFallen = stonesFallen.get(k);
				for(int m = stoneFallen.getStoneParts().size() - 1; m >= 0; m--) {
					if(stoneFallen.getStoneParts().get(m).getY() == yVal) {
						// remove the rectangles full
						stoneFallen.getStoneParts().remove(m);
					}
				}
			}
		}
	}
	
	/** 
	 * this method resets the full rows and the gameField values
	 *  
	 */
	private void resetRowsFull() {
		for(int m = getRowsFull().size() -1; m >= 0;m--) {
			int row = getRowsFull().get(m);
			//reset the row itself
			for(int col = 0;col < getxMax();col++) {
				gameField[row][col] = false;
			}
			//reset all values bigger than the row one line down
			setGameFieldDown(row);
		}
		// finally empty the list
		getRowsFull().clear();
	}
	
	/**
	 * this method sets the GameField boolean values one line down
	 * and the actual row GameField values if they are true to false
	 * @param row
	 */
	void setGameFieldDown(int row) {
		for (int numRow = row; numRow < getyMax(); numRow++) {
			for (int col = 0; col < getxMax(); col++) {
				if (getGameField()[numRow][col] == true) {
						gameField[numRow][col] = false;
						// set it one line down
						gameField[numRow -1][col] = true;
				}
			}
		}
	}
	
	/**
	 * this method sets the stone values one line down 
	 */
	private void setStonesDown() {
		
		for(int count = getRowsFull().size() - 1; count >= 0;count --) {
			int row = getRowsFull().get(count);
			int yValRow = getValuesOfField()[row][0].getyCoord();
			
			for(int i = 0; i < getStonesFallen().size();i++) {
				TetrisStone stoneFallen = getStonesFallen().get(i);
				
				for(int k = 0; k < stoneFallen.getStoneParts().size();k++) {
					ArrayList<Rectangle> stoneParts = stoneFallen.getStoneParts();
					
					if(stoneParts.get(k).getY() < yValRow) {
						int xValStonePart = (int)stoneParts.get(k).getX();
						int yValStonePart = (int)stoneParts.get(k).getY();
						stoneParts.get(k).setLocation(xValStonePart, yValStonePart + getKwidthAndHeight());
					}
				}
			}
		}
	}
	
	
	
	/**
	 * this method lays the current stone down and creates a new one
	 * 
	 * @param moveUp defines if the stone needs to be movedUp before is hits the
	 *               ground for example
	 */
	protected void layDownAndCreateNewStone( ) {
		try {
			if(isLayDownKeyPressed()) {
				getTimer().setDelay(getSpeed());
				setLayDownKeyPressed(false);
			}
			rememberStoneFallenPosition();
			findFullRows();
			layDownStone();
			resetStone();
			if(getRowsFull().size() > 0) {
				deleteFullRows();
				setStonesDown();
				resetRowsFull();
			}
			int randNumberStone = getRandomNumberForStone(7);
			int randInnerRectangleNumber = getRandomNumberForStone(5);
			getStone().createStone(randNumberStone,randInnerRectangleNumber);
			
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return the randNumber which is the id which indicates the stone
	 */
	protected int getRandomNumberForStone(int max) {
		int randNumber = (int) (Math.random() * max + 1);
		return randNumber;
	}

	

		
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			if (movePossible(1) && !touchesAnotherStone(1)) {
				moveStoneOneField(1, getStone().getStoneParts());
			}
		} else if (key == KeyEvent.VK_RIGHT) {
			if (movePossible(2) && !touchesAnotherStone(2)) {
				moveStoneOneField(2, getStone().getStoneParts());
			}
		} else if (key == KeyEvent.VK_UP) {
			if (!touchesAnotherStone(3) && !touchesGroundAfterMoving()) {
				rotateStone();
			}
			ArrayList<Rectangle> stoneParts = getStone().getStoneParts();

					log.info("X1:" + String.valueOf(stoneParts.get(0).getX()));
					log.info("X2:" + String.valueOf(stoneParts.get(1).getX()));
					log.info("X3:" + String.valueOf(stoneParts.get(2).getX()));
					log.info("X4:" + String.valueOf(stoneParts.get(3).getX()));

		} else if (key == KeyEvent.VK_DOWN) {
			setLayDownKeyPressed(true);
			setTimerLayDown(true);
		} else if (key == KeyEvent.VK_P) {
			if (!isGamePaused()) {
				setGamePaused(true);
				getTimer().stop();
			} else {
				setGamePaused(false);
				getTimer().restart();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	

	/** returns the xMax value */
	public int getxMax() {
		return xMax;
	}

	/** sets the xMax value */
	public void setxMax(int xMax) {
		this.xMax = xMax;
	}

	/** */
	public int getyMax() {
		return yMax;
	}

	/** */
	public void setyMax(int yMax) {
		this.yMax = yMax;
	}

	/** */
	public int getKwidthAndHeight() {
		return kWidthAndHeight;
	}

	/** */
	public void setKwidthAndHeight(int kWidthAndHeight) {
		this.kWidthAndHeight = kWidthAndHeight;
	}

	/** 
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/** 
	 * sets the game speed
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * 
	 * @return gameFieldWidth
	 * the width of the game field
	 */
	public int getGameFieldWidth() {
		return gameFieldWidth;
	}

	/**
	 * 
	 * @param gameFieldWidth
	 */
	public void setGameFieldWidth(int gameFieldWidth) {
		this.gameFieldWidth = gameFieldWidth;
	}

	/**
	 * 
	 * @return gameFieldHeight
	 */
	public int getGameFieldHeight() {
		return gameFieldHeight;
	}

	/**
	 * 
	 * @param gameFieldHeight
	 */
	public void setGameFieldHeight(int gameFieldHeight) {
		this.gameFieldHeight = gameFieldHeight;
	}

	/**
	 * 
	 * @return
	 */
	public int getUpperEdgeXcoord() {
		return upperEdgeXcoord;
	}

	/**
	 * 
	 * @param upperEdgeXStartCoord
	 */
	public void setUpperEdgeXcoord(int upperEdgeXcoord) {
		this.upperEdgeXcoord = upperEdgeXcoord;
	}

	/**
	 * 
	 * @return
	 */
	public int getUpperEdgeYcoord() {
		return upperEdgeYcoord;
	}

	/**
	 * 
	 * @param upperEdgeYStartCoord
	 */
	public void setUpperEdgeYcoord(int upperEdgeYcoord) {
		this.upperEdgeYcoord = upperEdgeYcoord;
	}

	
	/**
	 * (the current stone of the game field)
	 * @return stone
	 */
	public TetrisStone getStone() {
		return stone;
	}

	/**
	 * sets the current stone of the game field
	 * @param stone
	 */
	public void setStone(TetrisStone stone) {
		this.stone = stone;
	}

	/**
	 * the stones which already felt down
	 * @return stonesFallen
	 */
	public ArrayList<TetrisStone> getStonesFallen() {
		return stonesFallen;
	}

	/**
	 * sets the List of stones fallen
	 * @param stonesFallen
	 */
	public void setStonesFallen(ArrayList<TetrisStone> stonesFallen) {
		this.stonesFallen = stonesFallen;
	}

	/**
	 * 
	 * @return the current timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * 
	 * @param timer
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Rectangle> getStonePartsCopy() {
		return stonePartsCopy;
	}

	/**
	 * 
	 * @param stonePartsCopy
	 */
	public void setStonePartsCopy(ArrayList<Rectangle> stonePartsCopy) {
		this.stonePartsCopy = stonePartsCopy;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Integer> getRowsFull() {
		return rowsFull;
	}

	/**
	 * 
	 * @param rowsFull
	 */
	public void setRowsFull(ArrayList<Integer> rowsFull) {
		this.rowsFull = rowsFull;
	}


	/**
	 * 
	 * @return
	 */
	public boolean isGamePaused() {
		return gamePaused;
	}

	/**
	 * 
	 * @param gamePaused
	 */
	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}

	/**
	 * 
	 * @return layDownKeyPressed
	 */
	public boolean isLayDownKeyPressed() {
		return layDownKeyPressed;
	}

	/**
	 * sets the layDownKeyPressed value
	 * @param layDownKeyPressed
	 */
	public void setLayDownKeyPressed(boolean layDownKeyPressed) {
		this.layDownKeyPressed = layDownKeyPressed;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTimerLayDown() {
		return timerLayDown;
	}

	/**
	 * sets timerLayDown
	 * @param timerLayDown
	 */
	public void setTimerLayDown(boolean timerLayDown) {
		this.timerLayDown = timerLayDown;
	}


	public boolean[][] getGameField() {
		return gameField;
	}


	public void setGameField(boolean[][] gameField) {
		this.gameField = gameField;
	}


	public TetrisGameFieldCoord[][] getValuesOfField() {
		return valuesOfField;
	}


	public void setValuesOfField(TetrisGameFieldCoord[][] valuesOfField) {
		this.valuesOfField = valuesOfField;
	}



}

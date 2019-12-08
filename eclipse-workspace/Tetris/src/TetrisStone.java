import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TetrisStone implements Cloneable {

	// our stone color
	private Color color;

	// our number through we indicate the stone
	private int id;

	// All the stone parts are added here
	private ArrayList<Rectangle> stoneParts;

	// the x start coordinates of the stone
	private int xStartCoord;

	// the y start coordinates of the stone
	private int yStartCoord;

	// width of the stone
	private int stoneWidth;

	// height of the stone
	private int stoneHeight;

	// the id of the inner Rectangles
	private int innerRectangleId;

	// defines if the stone was rotated once
	private boolean stoneRotated;

	// defines if the stone was moved from the border
	private boolean movedFromBorder;

	// defines the direction we want to rotate the stone
	private int stoneRotateDirection;
	
	// the width and height of a stone part rectangle 
	private int kWidthAndHeight;
	
	// default x start coord value
	int defaultXstartCoord;
	
	// default y start coord value
	int defaultYstartCoord;
	
	/**
	 * every stone needs an id
	 * 
	 * @param id
	 */
	public TetrisStone(int id, int innerRectangleId) {
		stoneParts = new ArrayList<Rectangle>();
		this.id = id;
		this.innerRectangleId = innerRectangleId;
		createStone();
	}

	/**
	 * this method creates new stones out of the id
	 */
	private void createStone() {
		initStoneValues();
		
		if (getId() == 1) {
			createIstone();
		} else if (getId() == 2) {
			createOstone();
		} else if (getId() == 3) {
			createTstone();
		} else if (getId() == 4) {
			createSstone();
		} else if (getId() == 5) {
			createZstone();
		} else if (getId() == 6) {
			createJstone();
		} else if (getId() == 7) {
			createLstone();
		}
	}

	/**
	 * inits the default Stone values
	 */
	private void initStoneValues() {
		setStoneRotateDirection(0);
		setStoneHeight(14);
		setStoneWidth(14);
		setStoneRotated(false);
		setkWidthAndHeight(getStoneWidth() + 2); 
		setDefaultXstartCoord(125);
		setDefaultYstartCoord(24);
	}
	
	/**
	 * This method is needed when a new stone is created
	 * 
	 * @param id
	 */
	public void createStone(int id, int innerRectangleId) {
		setId(id);
		setInnerRectangleId(innerRectangleId);
		createStone();
	}


	/**
	 * The I rectangles been added inside here and the values for the I stone been
	 * set here
	 */
	private void createIstone() {
		setColor(Color.cyan);
		setxStartCoord(getDefaultXstartCoord());
		setyStartCoord(getDefaultYstartCoord());
		addParts(getxStartCoord(), getyStartCoord(), 4);
	}

	/**
	 * The O rectangles been added inside here
	 */
	private void createOstone() {
		setColor(Color.YELLOW);
		setxStartCoord(getDefaultXstartCoord());
		setyStartCoord(getDefaultYstartCoord()+ getkWidthAndHeight() * 2);
		addParts(getxStartCoord(), getyStartCoord(), 2);
		addParts(getxStartCoord() + getkWidthAndHeight(), getyStartCoord(), 2);
	}

	/**
	 * The T rectangles been added inside here
	 */
	private void createTstone() {
		setColor(Color.MAGENTA);
		setxStartCoord(getDefaultXstartCoord());
		setyStartCoord(getDefaultYstartCoord() + getkWidthAndHeight());
		addParts(getxStartCoord(), getyStartCoord(), 3);
		Rectangle rectangleRight = new Rectangle(getxStartCoord() + getkWidthAndHeight(), getyStartCoord() + getkWidthAndHeight(), 14, 14);
		getStoneParts().add(rectangleRight);
	}

	/**
	 * The S rectangles beend added inside here
	 */
	private void createSstone() {
		setColor(Color.GREEN);
		setxStartCoord(getDefaultXstartCoord());
		setyStartCoord(getDefaultYstartCoord() + getkWidthAndHeight());
		addParts(getxStartCoord(), getyStartCoord(), 2);
		addParts(getxStartCoord() + getkWidthAndHeight(), getyStartCoord() + getkWidthAndHeight(), 2);
	}

	/**
	 * The Z rectangles been added inside here
	 */
	private void createZstone() {
		setColor(Color.RED);
		setxStartCoord(getDefaultXstartCoord() + getkWidthAndHeight());
		setyStartCoord(getDefaultYstartCoord() + getkWidthAndHeight());
		addParts(getxStartCoord(), getyStartCoord(), 2);
		addParts(getxStartCoord() - getkWidthAndHeight(), getyStartCoord() + getkWidthAndHeight(), 2);
	}

	/**
	 * The J rectangles been added inside here
	 */
	private void createJstone() {
		setColor(Color.blue);
		setxStartCoord(getDefaultXstartCoord());
		setyStartCoord(getDefaultYstartCoord() + getkWidthAndHeight());
		addParts(getxStartCoord(), getyStartCoord(), 3);
		Rectangle rectangleRight = new Rectangle(getxStartCoord() + getkWidthAndHeight(), getyStartCoord(), 14, 14);
		getStoneParts().add(rectangleRight);
	}

	/**
	 * The L rectangles been added inside here
	 */
	private void createLstone() {
		Color orange = new Color(255, 165, 0);
		setColor(orange);
		setxStartCoord(141);
		setyStartCoord(getDefaultYstartCoord() + getkWidthAndHeight());
		addParts(getxStartCoord(), getyStartCoord(), 3);
		Rectangle rectangeLeft = new Rectangle(getxStartCoord() - getkWidthAndHeight(), getyStartCoord(), 14, 14);
		getStoneParts().add(rectangeLeft);
	}

	/**
	 * This method adds parts as rectangles to the stone
	 * 
	 * @param xVal
	 * @param yVal
	 * @param limit the limit which defines how much rectangles been added
	 */
	private void addParts(int xVal, int yVal, int limit) {
		for (int i = 0; i < limit; i++) {
			Rectangle rectangle = new Rectangle(xVal, yVal, 14, 14);
			getStoneParts().add(rectangle);
			yVal += getkWidthAndHeight();
		}
	}


	

	/** this method returns a clone of the stone */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * gets the Stone parts
	 */
	public ArrayList<Rectangle> getStoneParts() {
		return stoneParts;
	}

	/**
	 * sets all the stone parts
	 * 
	 * @param stoneParts
	 */
	public void setStoneParts(ArrayList<Rectangle> stoneParts) {
		this.stoneParts = stoneParts;
	}

	/**
	 * gets the color of the stone
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * sets the color of the stone
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * @return the id of the stone
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the id to the given value
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the X start coordinate of the stone
	 */
	public int getxStartCoord() {
		return xStartCoord;
	}

	/**
	 * 
	 * @param xStartCoord
	 */
	public void setxStartCoord(int xStartCoord) {
		this.xStartCoord = xStartCoord;
	}

	/**
	 * 
	 * @return
	 */
	public int getyStartCoord() {
		return yStartCoord;
	}

	/**
	 * 
	 * @param yStartCoord
	 */
	public void setyStartCoord(int yStartCoord) {
		this.yStartCoord = yStartCoord;
	}

	/**
	 * 
	 * @return
	 */
	public int getStoneWidth() {
		return stoneWidth;
	}

	/**
	 * 
	 * @param stoneWidth
	 */
	public void setStoneWidth(int stoneWidth) {
		this.stoneWidth = stoneWidth;
	}

	/**
	 * 
	 * @return
	 */
	public int getStoneHeight() {
		return stoneHeight;
	}

	/**
	 * 
	 * @param stoneHeight
	 */
	public void setStoneHeight(int stoneHeight) {
		this.stoneHeight = stoneHeight;
	}

	/**
	 * gets the innerRectanlgeId
	 * @return
	 */
	public int getInnerRectangleId() {
		return innerRectangleId;
	}

	/**
	 * sets the inner RectangleId ( 1 for black white rectangle,2 for 
	 * little black rectangles 3 for black filled rectangle, 
	 * 4 for black  rectangle with inner white rectangle)
	 * @param innerRectangleId
	 */
	public void setInnerRectangleId(int innerRectangleId) {
		this.innerRectangleId = innerRectangleId;
	}

	/**
	 * 
	 * @return stoneRotated
	 */
	public boolean isStoneRotated() {
		return stoneRotated;
	}

	/**
	 * sets the stoneRotated to false/true if it was rotated
	 * @param stoneRotated
	 */
	public void setStoneRotated(boolean stoneRotated) {
		this.stoneRotated = stoneRotated;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMovedFromBorder() {
		return movedFromBorder;
	}

	/**
	 * 
	 * @param movedFromBorder
	 */
	public void setMovedFromBorder(boolean movedFromBorder) {
		this.movedFromBorder = movedFromBorder;
	}

	/**
	 * 
	 * @return
	 */
	public int getStoneRotateDirection() {
		return stoneRotateDirection;
	}

	/** 
	 * 
	 * @param stoneRotateDirection
	 */
	public void setStoneRotateDirection(int stoneRotateDirection) {
		this.stoneRotateDirection = stoneRotateDirection;
	}

	public int getkWidthAndHeight() {
		return kWidthAndHeight;
	}

	public void setkWidthAndHeight(int kWidthAndHeight) {
		this.kWidthAndHeight = kWidthAndHeight;
	}

	public int getDefaultXstartCoord() {
		return defaultXstartCoord;
	}

	public void setDefaultXstartCoord(int defaultXstartCoord) {
		this.defaultXstartCoord = defaultXstartCoord;
	}

	public int getDefaultYstartCoord() {
		return defaultYstartCoord;
	}

	public void setDefaultYstartCoord(int defaultYstartCoord) {
		this.defaultYstartCoord = defaultYstartCoord;
	}

}

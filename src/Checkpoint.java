import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A class to represent checkpoints in the game.
 * @author Alexander Bousbaine
 *
 */
public class Checkpoint extends Entity{
	
	private RectangleShape visual, total;
	private double respawnX, respawnY;
	private boolean endPoint;
	private int checkpointNumber;
	private Texture checkTex, endTex;
	private Sprite checkFlag, endFlag;
	
	/**
	 * Constructor - initialises the size of the checkpoint object as well as the respawn coordinates associated with it 
	 * @param xPos - x position of checkpoint.
	 * @param yPos - y position of checkpoint.
	 * @param cNum - checkpoint number of the checkpoint, signifies how far through the level it appears (0 for the beginning, 1 for half-way, 2 for end).
	 * @param id - The id of the entity, obtained from the getNewEntityId() handler method.
	 * @param end - tells program whether or not this checkpoint represents the end of a level or not.
	 * @param h - handler instance, needed for superclass.
	 */
	public Checkpoint(int xPos, int yPos, int cNum, int id, boolean end, Handler h) {
		super(xPos, 2*Launcher.WIN_HEIGHT, 5, 3*Launcher.WIN_HEIGHT, h);
		
		checkpointNumber = cNum;
		
		endPoint = end;
		
		respawnX = getWorldX() - Player.X_START_OFFSET - 10*Main.UNIT;
		respawnY = (yPos * Main.UNIT) - h.getPlayer().getHeight();
		
		setId(id);
		
		visual = new RectangleShape(new Vector2f((float)getWidth(), (float)(10*Main.UNIT)));
		visual.setPosition((float)getWorldX(), (yPos * Main.UNIT));
		
		total = new RectangleShape(new Vector2f((float)getWidth(), (float)(getHeight())));
		total.setPosition((float)getWorldX(), (float)(getWorldY()));
		total.setFillColor(Color.CYAN);


		checkTex = new Texture();
		endTex = new Texture();
		try {
			checkTex.loadFromFile(Paths.get("src/Assets/Images/cPoint.png"));
			endTex.loadFromFile(Paths.get("src/Assets/Images/endFlag.png"));
			checkFlag = new Sprite(checkTex);
			endFlag = new Sprite(endTex);
			checkFlag.setScale((float)0.5, (float)0.5);
			endFlag.setScale((float)0.25, (float)0.25);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		checkFlag.setPosition((float)getWorldX(), (yPos * Main.UNIT));
		endFlag.setPosition((float)getWorldX(), (yPos * Main.UNIT));

	}
	
	/**
	 * Change respawn position data if y-value is sufficiently different.
	 * @param x - new x position.
	 * @param y - new y position.
	 */
	public void setRespawnPoint(double x, double y) {
		if(y < respawnX - 100 || y > respawnX + 100) {
			respawnX = x;
			respawnY = y;
		}
	}
	
	@Override
	public String getType() {
		return "checkpoint";
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(RenderWindow w) {
		if(endPoint) {
			w.draw(endFlag);
		}
		else {
			w.draw(checkFlag);
		}
	}
	
	//getters and setters
	
	/**
	 * Method to get whether this checkpoint is a level endpoint.
	 * @return true if endpoint, otherwise false.
	 */
	public boolean isEndPoint() {
		return endPoint;
	}
	
	/**
	 * Method to get the x respawn position associated with this checkpoint.
	 * @return x respawn position.
	 */
	public double getRespawnX() {
		return respawnX;
	}
	
	/**
	 * Method to get the y respawn position associated with this checkpoint.
	 * @return y respawn position.
	 */
	public double getRespawnY() {
		return respawnY;
	}
	
	/**
	 * Method to get the checkpoint number of this checkpoint.
	 * @return checkpoint number.
	 */
	public int getCheckpointNumber() {
		return checkpointNumber;
	}

}

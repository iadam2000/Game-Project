
import org.jsfml.graphics.RenderWindow;

/**
 * A class to represent the Enemy entity type.
 * @author Marta Adamska, Alexander Bousbaine
 *
 */
public class Enemy extends Entity {
	private Handler handler;
    private int startX, endX;
    //xFlag tells enemy whether or not they have reached the end of their route and should begin moving back (in the x direction).
    //yFlage does the same for the y direction.
    private boolean xFlag, inPlay;
    private double originX, originY, xSpeed;

    /**
     * Constructor - sets up position and image data for the enemy entity.
     * @param data - position data of the enemy.
     * @param id - id for this entity from the getNewEntityId() method.
     * @param h - handler instance to get access to handler methods.
     */
    public Enemy (int[] data, int id, Handler h) {
        super(data, h);
        handler = h;
        setId(id);
        
        xFlag = inPlay = true;
        
        startX = data[0] * Main.UNIT;
        endX = startX + (50 * Main.UNIT);
        
        originX = getWorldX();
        originY = getWorldY();
        
        setWidth(10*Main.UNIT);
        setHeight(10*Main.UNIT);
        
        xSpeed = (startX != endX) ? Main.UNIT : 0;
        
        loadTextureIntoSprite("src/Assets/Characters/enemy.png");
    }
    
    /**
     * Method to move the enemy back to its starting position.
     * Should be called when the player dies.
     */
    public void resetToOrigin() {
    	setWorldX(originX);
    	setWorldY(originY);
    }
    
    /**
     * Method to slow down the enemy's speed.
     */
    public void slow() {
    	xSpeed = Main.UNIT/2;
    }
    
    /**
     * Method to set the enemy's speed back to its normal value.
     */
    public void speedUp() {
    	xSpeed = Main.UNIT;
    }
    
    /**
     * Method to check whether or not the enemy should carry out its movements.
     */
    private void isInPlay() {
    	if((getWorldX() < (handler.getPlayerWorldX() - (Player.X_START_OFFSET * Main.UNIT))) && (getWorldX() > (handler.getPlayerWorldX() + 2*Launcher.WIN_WIDTH))) {
    		inPlay = false;
    		return;
    	}
    	inPlay = true;
    }
    
    @Override
    public void update() {

    	isInPlay();
    	
    	if(inPlay) {
	    	//only move in the x direction now
		    if(xFlag) {
		    	setWorldX(getWorldX() + xSpeed);
		    		
		    	if(getWorldX() >= endX) {
		    		xFlag = false;
		    		setWorldX(endX);
		    	}
		
		    }
		    else {
		    	setWorldX(getWorldX() - xSpeed);
		    		
		    	if(getWorldX() <= startX) {
		    		xFlag = true;
		    		setWorldX(startX);
		    	}
		    }
	    	
	    	updateSkinPos();
    	}
    }

    @Override
    public void render(RenderWindow w) {
    	w.draw(getSkin());
    }
    
    @Override
    public String getType() {
        return "obstacle";
    }
}
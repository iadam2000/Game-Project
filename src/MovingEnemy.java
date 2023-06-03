
import java.util.ArrayList;
import java.util.Random;

import org.jsfml.graphics.RenderWindow;

/**
 * A class to represent the moving enemy entity type
 * @author Marta Adamska, Alexander Bousbaine
 *
 */
public class MovingEnemy extends Entity {

    private Handler handler;
    private double speed;
    private double originX, originY;
    private boolean inPlay;

    //random num generated between 0 and 1000 (non-inclusive upper bound, so 1001)
    private Random random;
    private final int ceiling = 1001;
    private int[] probs;
    private int counter;

    // Increase thresholds to make mvEn more precise with movements. 
    // If less that 500, the thing will go (predominantly) backwards and if set to 500, the thing won't go anywhere
    private int thresholdX = 850, thresholdY = 800, roll = 0;

    /**
     * Method to create a moving enemy object.
     * These are enemies which will home in on the player as opposed to normal enemies which follow a set movement path.
     * @param data position data as an array to feed into {@link Entity#Entity(int[], Handler)}
     * @param i id of this entity.
     * @param h handler instance for superclass.
     */
    public MovingEnemy (int[] data, int i, Handler h) {
        super(data, h);
        handler = h;
        speed = counter = 0;
        random = new Random();
        probs = new int[10000];
        setId(i);
        
        setWidth(10 * Main.UNIT);
        setHeight(10 * Main.UNIT);
        
        //theory is, its faster to access an array than to generate a new random number
        for(int a = 0; a < probs.length; a++) {
        	probs[a] = random.nextInt(ceiling);
        }
        
        originX = getWorldX();
        originY = getWorldY();
        
        loadTextureIntoSprite("src/Assets/Characters/movingEnemy.png");
    }
    
    /**
     * Method to move this MovingEnemy object back to its original position (where it started the level).
     * Should be called whenever the player dies.
     */
    public void resetToOrigin() {
    	if(getId() == 260) {
    		System.out.println("resetting");
    	}
    	setWorldX(originX);
    	setWorldY(originY);
    }
    
    //Method to check whether or not this Entity should move (if too far away from player, no point in moving).
    private void isInPlay() {
    	if((getWorldX() < (handler.getPlayerWorldX() - (Player.X_START_OFFSET * Main.UNIT))) || (getWorldX() > (handler.getPlayerWorldX() + 2*Launcher.WIN_WIDTH))) {
    		inPlay = false;
    		return;
    	}
    	inPlay = true;
    }

    @Override
    public void update() {
    	isInPlay();
    	
    	if(inPlay) {
	    	if(speed != handler.getLevelSpeed() / 3) {
	    		speed = handler.getLevelSpeed() / 3;
	    	}
	    	
	        double pX = handler.getPlayerWorldX(), pY = handler.getPlayerWorldY();
	        double dx = getWorldX() - pX, dy = getWorldY() - pY;
	        double tot = Math.abs(dx) + Math.abs(dy);
		      
	        roll = probs[counter];
	        
	        if(roll < thresholdX) {
		        setWorldX(getWorldX() - (dx / tot) * speed);
		        
	        }
	        else {
	        	setWorldX(getWorldX() - (dx / tot) * -speed);
	        }
	        
	        if(roll < thresholdY) {
	        	setWorldY(getWorldY() - (dy / tot) * speed);
	        }
		    else {
		        setWorldY(getWorldY() - (dy / tot) * -speed);
		    }
	        	
	        this.updateSkinPos();
	        
	        checkCollision();
	        
	        counter = (counter+1) % probs.length; 
    	}
    }
    
    private void checkCollision() {
    	//get entities to check
    	 ArrayList<Entity> layerEntities = handler.getMainEntities();
    	 
    	 //go through them
         for (Entity e : layerEntities) {

        	 //if this intersects one (that isn't itself)
             if(intersects(e) && e != this) {
            	 if(!(e.getType().equals("power up"))) {

            		 switch (collidesWhereWith(e)) {
            		 	case Entity.BOTTOM:
            		 		setWorldY(e.getWorldY() - getHeight());
            		 		break;

	             		case Entity.TOP:
	             			System.out.println("top");
	 	            		setWorldY(e.getWorldY() + e.getHeight());
	 	            		break;

	             		case Entity.LEFT:
	 	            		setWorldX(e.getWorldX() + e.getWidth());
	 	            		break;

	             		case Entity.RIGHT:
	 	            		setWorldX(e.getWorldY() - getWidth());
	 	            		break;
             			}
            	 }
             }
         }
    }

    @Override
    public void render(RenderWindow w) {
    	w.draw(getSkin());
    }
    
    @Override
    public String getType() {
        return "moving enemy";
    }
}
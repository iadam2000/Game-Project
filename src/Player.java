import java.util.ArrayList;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * A class to represent the player.
 * @author Marta Adamska, Alexander Bousbaine, Zubayer Ahmed, Charlotte Brocksom
 *
 */

	// MAX JUMP HEIGHT IS ~150px (150.5px), or ~30 units.
	// WITH SLOW DOWN IT IS ~154.6px, or ~30.9 units.
	// WITH DOUBLE JUMP IT IS ~300px (301.2px), or ~60 units.

	// MAX JUMP LENGTH IS 42.9 UNITS AT BASE SPEED (60 units per second) AND 42.2 UNITS WHEN SLOWED DOWN AND 54.6 UNITS USING DOUBLE JUMP (AT BASE SPEED). 

	// Ducking player is 3/4 max height (goes to 15 units from 20).

	// If player activates slow down while jumping, makes them jump real high. Bug or feature?

public class Player extends Entity {
	
	public static final double X_START_OFFSET = 40 * Main.UNIT;
	public static final double Y_START_OFFSET = Launcher.WIN_HEIGHT - 40 * Main.UNIT;
	
    private double GRAVITY = 1.5;
    private double jumpStrength = 22;

    private double ySpeed, xSpeed, respawnX, respawnY;
    private boolean isJumping, isDucking, onGround, intersected, heightToggle, landingToggle, printJumpData, disableDeath, viewBoxes, died;

    private PowerUp storedPU, activePU;
    private Handler handler;
    private int runningNum, slowRun, numDeaths;
    
    //DEBUGGING STUFF
    
    private RectangleShape smallBox, smallBox2, bigBox;
    
    //used to calculate jump length - useful for level redesigns.
    private double jumpLen, jumpXStart, jumpXEnd, jumpBaseY, jumpPeakY, jumpHeight;

    public Player(int x, int y, int width, int height, Handler h) {
        super(x, y, width, height, h);
        handler = h;
        respawnX = 0;
        
        jumpPeakY = 0;
        
        numDeaths = 0;

        isJumping = isDucking = onGround = heightToggle = landingToggle = intersected = died = false;
        
        //make true to print jump length when player jumps
        printJumpData = false;
        disableDeath = false;
        viewBoxes = false;
        
        ySpeed = xSpeed = 0;

        storedPU = activePU = null;

        loadTextures(new String[] { "src/Assets/Characters/stickman1.png", "src/Assets/Characters/stickman2.png",
        		"src/Assets/Characters/stickman3.png", "src/Assets/Characters/stickman4.png", "src/Assets/Characters/stickman5.png",
        		"src/Assets/Characters/stickman6.png", "src/Assets/Characters/stickman7.png", "src/Assets/Characters/stickman8.png",
        		"src/Assets/Images/jump.png", "src/Assets/Images/duck.png" });

        setTextureTo(0);
        runningNum = 0;
        slowRun = 0;
        
        shrinkAABB();
        
        smallBox = new RectangleShape();
        smallBox.setFillColor(Color.RED);
        
        smallBox2 = new RectangleShape();
        smallBox2.setFillColor(Color.YELLOW);
        
        bigBox = new RectangleShape();
        bigBox.setFillColor(Color.GREEN);
        bigBox.setPosition(AABB.left, AABB.top);
        bigBox.setSize(new Vector2f(AABB.width, AABB.height));
    }

    /**
     * Method to double the player's jump height for the double jump power-up.
     */
    public void doubleJumpHeight() {
        // Newton says it should be 30 - but apparently no...
        // Could be to do with bad resolution of measurements.
        jumpStrength = 30.81;
    }

    /**
     * Method to reset jump strength after using double jump power-up.
     */
    public void resetJumpHeight() {
        jumpStrength = 22;
        GRAVITY = 1.5;
    }
    
    /**
     * Method to slow down the player's jump by reducing gravity and initial jump force.
     */
    public void slowDownJump() {
    	jumpStrength = 11;
    	GRAVITY = 0.378;
    	ySpeed/=2;
    }

    /**
     * Moves the player to the x and y position given
     *
     * @param xPos - x distance from bottom left-hand corner + starting offset.
     * @param yPos - worldY value to go to
     */
    public void resetToPosition(double xPos, double yPos) {
       
        ySpeed = 0;
        
        // current xPosition minus the difference between the xOffset from the start and the target x position
        setWorldX(getWorldX() - (handler.getXOffset() - xPos));
        // current y position minus the target y position + the offset (which is a negative)
        setWorldY((Launcher.WIN_HEIGHT*0.8) - (yPos + handler.getPlayer().getHeight()/2));

        if (activePU != null) {
            activePU.forceEnd();
            activePU = null;
        }
        if (storedPU != null) {
            handler.getPouch().emptyPouch();
            storedPU = null;
        }  
    }

    @Override
    public void render(RenderWindow w) {
    	//shows various collision boxes for debugging
    	if(viewBoxes) {
    		w.draw(bigBox);
	        if(bottomAABB != null && rightAABB != null) {
		        smallBox.setPosition(bottomAABB.left, bottomAABB.top);
		        smallBox.setSize(new Vector2f(bottomAABB.width, bottomAABB.height));
		        
		        smallBox2.setPosition(rightAABB.left, rightAABB.top);
		        smallBox2.setSize(new Vector2f(rightAABB.width, rightAABB.height));
		        
		        w.draw(smallBox);
		        w.draw(smallBox2);
	        }
	        w.draw(this.interBox);
	        bigBox.setPosition(AABB.left, AABB.top);
	        bigBox.setSize(new Vector2f(AABB.width, AABB.height));
    	}
    	
    	if(!died) {
    		w.draw(this.getSkin());
    	}
    }

    @Override
    public void update() {
    	if(!died) {
    		
    		//change x and y values by xSpeed and ySpeed
	        move();
	
	        // use power-up
	        if (handler.spacePressed() && activePU == null && storedPU != null) {
	            activePU = storedPU;
	            activePU.activate();
	            handler.emptyPouch();
	            storedPU = null;
	        }
	
	        // discard power-up
	        if (handler.shiftPressed()) {
	            handler.emptyPouch();
	            storedPU = null;
	        }
	
	        // check that power-up is over
	        if (activePU != null) {
	            handler.setTimerText("" + (int) Math.ceil(activePU.getTimeLeft()));
	            activePU = activePU.deleteIfFinished();
	        }
	
	        // jumping
	        if (handler.upPressed()) {
	        	jump();
                runningNum = 0;
	            slowRun = 0;
	        }
	
	        // ducking
	        if (handler.downPressed()) {
	            duck();
	            runningNum = 0;
	            slowRun = 0;
	        } 
	        else {
	            isDucking = false;
	            if (heightToggle) {
	            	setTextureTo(0);
	                revertFromDuck();
	                heightToggle = false;
	            }
	        }
	        
	        //collect jump height data
	        if(isJumping) {
	        	if(jumpPeakY == 0) {
	        		jumpPeakY = getWorldY();
	        	}
	        	if(getWorldY() < jumpPeakY) {
	        		jumpPeakY = getWorldY();
	        	}
	        }
	
	        applyGravity();
	        checkCollision();
	        updateSkinPos();
    	}
    }

    private void move() {
        setWorldX(getWorldX() + xSpeed);
        setWorldY(getWorldY() + ySpeed);
        
        handler.moveMarkerToPlayer();

        if(!isJumping && !isDucking) {
            run();
        }
    }

    private void run() {
        if (slowRun == 2) {
            if (runningNum == 4){
                setTextureTo(0);
                runningNum = 0;
            }
            else{
                runningNum = runningNum + 1;
                setTextureTo(runningNum);
            }
            slowRun = 0;
        }
        else {
            slowRun++;
        }
    }

    private void jump() {
        if (!isJumping && onGround) {
        	
        	handler.playSound(SoundManager.JUMP);
        	
        	jumpBaseY = getWorldY();
        	
            if (isDucking) {
                revertFromDuck();
                heightToggle = false;
            }
            
            jumpXStart = getWorldX();
            
            isJumping = landingToggle = true;
            onGround = isDucking = false;
            ySpeed = -jumpStrength;
            setTextureTo(8);
        }
    }

    private void duck() {
        if (!isJumping && !isDucking && onGround) {
            isDucking = true;
            heightToggle = true;
            
            handler.playSound(SoundManager.DUCK);
            
            setTextureTo(9);
            setWorldY(getWorldY() + getHeight() / 4);
            setHeight(getHeight() * 0.75);
            updateSkinSize();
        }
    }

    private void revertFromDuck() {
    	setHeight(getHeight() / 0.75);
    	setWorldY(getWorldY() - getHeight() / 4);
    	updateSkinSize();
    }

    private void applyGravity() {
        if (!onGround) {
            ySpeed += GRAVITY;
        }
    }
    
    private void die() {
    	numDeaths++;
    	handler.resetToPosition(respawnX, respawnY);
        handler.playSound(SoundManager.DIE);
        handler.moveMarkerToPlayer();
        handler.resetEnemies();
        died = true;        
    }
    
    /**
     * Method to reset the player's position to the last checkpoint they reached.
     */
    public void resetToCheckpoint() {
    	handler.resetToPosition(respawnX, respawnY);
        died = true;
    }

    /**
     * Helper method to check for any collisions the player has with any other
     * Entity instance.
     */
    private void checkCollision() {
        // get all instances in main.
        ArrayList<Entity> layerEntities = handler.getMainEntities();

        // for each of them...
        for (Entity e : layerEntities) {
            // check if player collides with them.
            if (intersects(e)) {
                // if they do collide
                intersected = true;
                
                if(viewBoxes) {
                	System.out.println("collides with: "+e.getType());
                }

                //if hits checkpoint - set respawn location information.
                if(e.getType().equals("checkpoint")) {
                	
                	Checkpoint p = (Checkpoint) e;
                	
                	if(!handler.isRespawnPoint(p)) {
	                	p.setRespawnPoint(getWorldX() - X_START_OFFSET - 10*Main.UNIT, (Launcher.WIN_HEIGHT*0.8) - getWorldY());
	                	
	                	respawnX = p.getRespawnX();
	                	respawnY = p.getRespawnY();
                	}
                	
                	handler.passCheckpoint(p);
                	
                	if(p.isEndPoint()) {
                		respawnX = 0;
                		respawnY = 0;
                	}
                }
                
                // if player collides with a platform.
                if (e.getType().equals("platform") || e.getType().equals("obstacle")) {
                	
                	int collisionLocation = collidesWhereWith(e);
                	
                	if(viewBoxes && collisionLocation != 2) {
                		System.out.println("Collides on side: "+collisionLocation);
                	}

                    // check if the collision is on the bottom of the player.
                    if (collisionLocation == Entity.BOTTOM) {
                        // if so, stop player falling and set vars.
                    	
                    	//set print jump len to true to print the jump length
                    	if(jumpXStart != 0 && printJumpData) {
	                    	jumpXEnd = getWorldX();
	                    	jumpLen = (jumpXEnd - jumpXStart);
	                    	
	                    	jumpHeight = jumpBaseY - jumpPeakY;
	                    	
	                    	System.out.println("----------------------------");
	                    	System.out.println("cX: "+getWorldX()+" cY: "+getWorldY());
	                    	System.out.println("Jump len (U/Px): "+(jumpLen/Main.UNIT)+" / "+jumpLen);
	                    	System.out.println("Jump Height: (U/Px): "+ (jumpHeight/Main.UNIT)+" / "+jumpHeight);
	                    	System.out.println("Gravity: "+GRAVITY);
	                    	System.out.println("Inital ySpeed: "+jumpStrength);
	                    	System.out.println("xSpeed (Px per frame): "+handler.getLevelSpeed());
	                    	System.out.println("----------------------------");
	                    	
	                    	jumpXStart = 0;
	                    	jumpPeakY = 0;
                    	}
                    	
                        ySpeed = 0;
                        onGround = true;
                        isJumping = false;
                        setWorldY(e.getWorldY() - getHeight());
                        if (landingToggle) {
                            setTextureTo(0);
                            landingToggle = false;
                        }
                    }

                    // Check if player collides with 'ceiling'.
                    // If so, get the player out of the ceiling and stop them moving back into it.
                    if(collisionLocation == Entity.TOP) {
                    	setWorldY(e.getWorldY() + e.getWidth());
                    	ySpeed = 0;
                    }
                    
                    // Check for kill collisions

                    // if collides with side of platform...
                    if (collisionLocation == Entity.RIGHT && !disableDeath) {
                    	die();
                    }
                    
                }
                // if it intersects with the types of entity listed below.
                if ((e.getType().equals("obstacle") || e.getType().equals("enemy")
                        || e.getType().equals("moving enemy")) && !disableDeath) {

                    // print out where it happened, then die.
                    //System.out.println("Death Collision with " + e.getType() + " @ ~x" + getWorldX() + ", ~y" + getWorldY());
                	die();
                }

                // if collides with a power-ups physical object (checks for interface which they all implement),
                // and there is no power-up currently stored.
                if (e instanceof PowerUpEntity && storedPU == null) {
                    if (!((PowerUpEntity) e).isTaken()) {
                        // set pouch contents
                        handler.sendToSpentList((PowerUpEntity) e);
                        handler.getPouch().setPouchContents(e);
                        storedPU = ((PowerUpEntity) e).getPowerUp();
                    }
                }
            }
        }
        // if player has not collided with anything.
        if (!intersected) {
            // player is not on the ground (as they haven't collided with it).
            onGround = false;
        }
        intersected = false;

        if (getWorldY() > Launcher.WIN_HEIGHT) {
            die();
        }
    }

    /**
     * Method to set the horizontal speed of the player.
     *
     * @param lvlSpeed
     */
    public void setXSpeed(double lvlSpeed) {
        xSpeed = lvlSpeed;
    }
    
    /**
     * Method to make the 'died' boolean variable false.
     * Should be used when the player respawns.
     */
    public void nowAlive() {
    	died = false;
    }
    
    /**
     * Method to check whether the player has died.
     * @return true if player is dead, otherwise false.
     */
    public boolean isDead() {
    	return died;
    }

    /**
     * Method to get the PowerUp instance that is currently in the active slot.
     * @return PowerUp instance that is currently in use.
     */
    public PowerUp getActivePU() {
        return activePU;
    }

    @Override
    public String getType() {
        return "player";
    }
    
    /**
     * Method to return the number of times the player has died.
     * @return number of times the player has died.
     */
    public int getNumDeaths() {
    	return numDeaths;
    }
    
    /**
     * Method to set the variable representing how many times the player has died.
     * @param d number of times the player has died.
     */
    public void setNumDeaths(int d) {
    	numDeaths = d;
    }
    
//    public void toggleDebug() {
//    	if(!disableDeath) {
//    		System.out.println("DEBUG:\ndeath disabled, jump data printing");
//    	}
//    	if(disableDeath) {
//    		System.out.println("no debug");
//    	}
//    	disableDeath = !disableDeath;
//    	printJumpData = !printJumpData;
//    	viewBoxes = !viewBoxes;
//    }
    
    /**
     * Method to get the player's y speed.
     * @return player's y speed.
     */
	public double getySpeed() {
		return ySpeed - 2 * GRAVITY;
	}
	
	/**
	 * Method to get the respawn x position of the player.
	 * @return x position respawn value.
	 */
	public double getRespawnX() {
		return respawnX;
	}
	
	/**
	 * Method to get the respawn y position of the player.
	 * @return respawn y position.
	 */
	public double getRespawnY() {
		return respawnY;
	}
	
	/**
	 * Method to set the respawn x position of the player.
	 * @param x x value of player's respawn point.
	 */
	public void setRespawnX(double x) {
		respawnX = x;
	}
	
	/**
	 * Method to set the respawn y position of the player.
	 * @param y y value of the player's respawn point.
	 */
	public void setRespawnY(double y) {
		respawnY = y;
	}
}
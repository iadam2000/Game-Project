

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * A class to represent the levels in our game
 * @author Marta Adamska, Alexander Bousbaine
 *
 */
public class Level extends GameStage {
	
	public static final double BASE_SPEED = (double)(Main.UNIT*1.2);
	
    private Handler handler;
    private Layer mainLayer, lowerLayer, upperLayer, layer1, layer2, layer3;
    private ProgressBar progBar;
    private PowerUpPouch pouch;
    private PowerUpTimerVis ptv;
    private Player player;
    private Checkpoint checkpoints[];
    private Checkpoint respawnPoint;
    private Background mainBackG, lowerBackG, upperBackG, backG1, backG2;
    
    private ArrayList<PowerUpEntity> spentPowerUps;
    private boolean completed, viewMode;
    private int xEnd, levelID, checkpointNum;
    private double lvlSpeed, prevSpeed, speedFactor;
    
    private double viewModeOriginX, viewModeOriginY, initYOffset;
    
    private RectangleShape nullLayer, playerMarker;
    private Text rightLabel, leftLabel, centreLabel, pauseMenu;


    /**
     * Constructor, takes in an ID to represent the level and a handler instance.
     * Instantiates layer objects, creates null layer "Black Rectangle".
     * @param lvlID - level ID.
     * @param h - handler instance - should be the instance declared in Main.
     */
    public Level(int lvlID, Handler h) {
    	super(lvlID);
    	
        handler = h;
        lvlSpeed = 0;
        levelID = lvlID;
        
        respawnPoint = null;
        checkpointNum = 0;
        checkpoints = null;
        
        spentPowerUps = new ArrayList<>();
        
        layer1 = new Layer(getID(), 1, h);
        layer2 = new Layer(getID(), 2, h);
        layer3 = new Layer(getID(), 3, h);
        
        lowerLayer = layer1;
        mainLayer = layer2;
        upperLayer = layer3;
        
        nullLayer = new RectangleShape(handler.getSubViewSize());
        nullLayer.setFillColor(Color.BLACK);

		backG1 = new Background(levelID, "S.png");
		backG2 = new Background(levelID, "M.png");

		lowerBackG = backG1;
		mainBackG = backG2;
		upperBackG = backG1;
        
        playerMarker = new RectangleShape(new Vector2f(40, Launcher.WIN_HEIGHT/4));
        playerMarker.setFillColor(Color.BLACK);
        playerMarker.setOutlineColor(Color.WHITE);
        playerMarker.setOutlineThickness(10);
        
        leftLabel = new Text("Lower Layer", handler.getFontBlackChancery(), 24);
        rightLabel = new Text("Upper Layer", handler.getFontBlackChancery(), 24);
        centreLabel = new Text("Middle Layer", handler.getFontBlackChancery(), 24);
     
        leftLabel.setColor(Color.BLACK);
        rightLabel.setColor(Color.BLACK);
        centreLabel.setColor(Color.BLACK);
        
        leftLabel.setStyle(Text.BOLD);
        rightLabel.setStyle(Text.BOLD);
        centreLabel.setStyle(Text.BOLD);

        leftLabel.setPosition(2*Main.UNIT, (float) (0.8*Launcher.WIN_HEIGHT));
        rightLabel.setPosition((float) (Launcher.WIN_WIDTH/2) + 2*Main.UNIT, (float) (0.8*Launcher.WIN_HEIGHT));
        centreLabel.setPosition(4*Main.UNIT, 8*Main.UNIT);
        
        pauseMenu = new Text("ESC -> Pause", handler.getFontBlackChancery(), 20);
        pauseMenu.setColor(Color.BLACK);
        pauseMenu.setPosition(Launcher.WIN_WIDTH / 2 - (pauseMenu.getLocalBounds().width/2), 0);
        
        ptv = new PowerUpTimerVis(handler);

    }

	/**
	 * Method to create the checkpoints
	 */
	private void setupCheckpoints() {
    	if(checkpoints == null) {
	    	checkpoints = new Checkpoint[mainLayer.getNumCheckpoints()];
	        for (int cptArrIdx = 0, datArrIdx = 0; cptArrIdx < mainLayer.getNumCheckpoints(); cptArrIdx++, datArrIdx += 2) {
	        	if (cptArrIdx < mainLayer.getNumCheckpoints() - 1) {
	        		handler.addEntity(checkpoints[cptArrIdx] = new Checkpoint(mainLayer.getCheckpointData().get(datArrIdx), mainLayer.getCheckpointData().get(datArrIdx+1), cptArrIdx+1, handler.getNewEntityId(), false, handler));
				}
	        	else {
	        		handler.addEntity(checkpoints[cptArrIdx] = new Checkpoint(mainLayer.getCheckpointData().get(datArrIdx), mainLayer.getCheckpointData().get(datArrIdx+1), cptArrIdx+1, handler.getNewEntityId(), true, handler));
				}
				handler.designateMain(checkpoints[cptArrIdx]);
			}
    	}
    	else {
    		for(Checkpoint c : checkpoints) {
    			handler.addEntity(c);
    			handler.designateMain(c);
    		}
    	}
    }
    
    /**
     * Method to add a power-up to the list of spent power-ups for recovery on level reset.
     */
    public void addPowerUpToSpentList(PowerUpEntity p) {
    	p.take();
    	spentPowerUps.add(p);
    }
    
    /**
     * Method to recover power-ups spent in the last run (that meaning, place them back on the level).
     */
    public void recoverPowerUps() {
    	for(PowerUpEntity e : spentPowerUps) {
    		e.place();
    	}
    	spentPowerUps.clear();
    }
    
    public void passCheckpoint(Checkpoint c) {
    	if(!isRespawnPoint(c)) {
    		doCheckpointWork(c);
    	}	
    }
    
    public boolean isRespawnPoint(Checkpoint c) {
    	if(respawnPoint == null) {
    		return false;
    	}

    	if((respawnPoint.getId() == c.getId())) {
    		return true;
    	}

    	return false;
    }
    
    private void doCheckpointWork(Checkpoint c) {
    	respawnPoint = c;
    	
    	checkpointNum++;
    	speedFactor += 0.1;
    	
    	updateSpeed(speedFactor);
    	handler.getPlayer().setXSpeed(lvlSpeed);
    	//gets correct lvlNum from ID
    	handler.setSaveState((levelID + (levelID - 1)), player.getRespawnX(), player.getRespawnY(), respawnPoint.getCheckpointNumber(), speedFactor, player.getNumDeaths());
    		
    	if(c.isEndPoint()) {
        	completed = true;
    	}
    }
    
    /**
	 * I had to add this method to the GameStage class -M
     * Method to finish setting up the level. Called when moving into a new level.
     * @param sf - the factor by which to multiply the base speed.
     */
    public void setupLevel(double sf) {
    	speedFactor = sf;
    	lvlSpeed = speedFactor * BASE_SPEED;
    	prevSpeed = lvlSpeed;
   	
		addEntitiesIntoManager();
		setLayerArrangement();
		
		xEnd = determineLength();
		progBar = new ProgressBar(xEnd, handler);
		
		pouch = new PowerUpPouch();

		player = handler.getPlayer();

		player.setXSpeed(lvlSpeed);
		
		handler.setCurrentLevel(this);
		
		handler.moveToMiddleLayer();
		handler.resetToPosition(player.getRespawnX(), player.getRespawnY());
		initYOffset = handler.getYOffset();
		
		//goes through one update tick for all entities in the upper and lower layers - 
		// makes them render instead of being invisible until the player visits the layer for the first time.
		handler.initialUpdate();
		
		if(player.isDead()) {
			player.nowAlive();
		}

		setupCheckpoints();
    }
    
    private int determineLength() {
    	int len = 0;

    	if(layer1.getLength() > len) {
    		len = layer1.getLength();
    	}
    	if(layer2.getLength() > len) {
    		len = layer2.getLength();
    	}
    	if(layer3.getLength() > len) {
    		len = layer3.getLength();
    	}
    	
    	return len;
    }
    
    private void centreOnPlayer() {	
    	//if in top quarter of screen
    	if(player.getSceneY() < (Launcher.WIN_WIDTH*0.8)/4) {
    		//follow player y movement
    		handler.scrollViews(0, player.getySpeed());
    	}
    	//if in bottom 3/4
    	else {
    		//if player is moving down, follow with camera
    		if(player.getySpeed() > 0 && handler.getYOffset() < 0) {
    			handler.scrollViews(0, player.getySpeed());
    		}
    	}
    	
    	//correct any over-correction in an approximate manner 
    	while(handler.getYOffset() > 0) {
    		handler.scrollViews(0, -0.5);
    	}
    }

    @Override
    public void update() {
    	if(!completed && !viewMode) {
    		
    		//System.out.println("sf: "+speedFactor+" leads to speed: "+lvlSpeed);
    		
			handler.scrollViews(lvlSpeed, 0);
			centreOnPlayer();
			playerMarker.move((float) lvlSpeed, 0);
			nullLayer.setPosition((float)handler.getXOffset(), (float)handler.getYOffset());
			
			progBar.update();
			
	    	handler.updateEntities();

	    	mainBackG.getBackground().setPosition((float)handler.getXOffset(), (float)handler.getYOffset());
			lowerBackG.getBackground().setPosition((float)handler.getXOffset(), (float)handler.getYOffset());
			upperBackG.getBackground().setPosition((float)handler.getXOffset(), (float)handler.getYOffset());
	    	
	    	if(handler.leftPressed()) {
				moveToLowerLayer();

			}
			if(handler.rightPressed()) {
				moveToUpperLayer();
				handler.playSound(3);
			}

			//no access to debug mode now
//			if(handler.gPressed()) {
//				viewMode = true;
//				viewModeOriginX = handler.getXOffset();
//				viewModeOriginY = handler.getYOffset();
//			}
    	}
    	
    	if(viewMode) {
    		if(handler.upPressed()) {
    			handler.scrollViews(0, - Main.UNIT);
    		}
    		if(handler.downPressed()) {
    			handler.scrollViews(0, Main.UNIT);
    		}
    		if(handler.leftPressed()) {
    			handler.scrollViews(-2 * Main.UNIT, 0);
    		}
    		if(handler.rightPressed()) {
    			handler.scrollViews(2 * Main.UNIT, 0);
    		}
    		
    		if(handler.enterPressed()) {
    			moveToUpperLayer();
    		}
    		if(handler.shiftPressed()) {
    			moveToLowerLayer();
    		}
    		
    		if(handler.spacePressed()) {
//    			handler.getPlayer().toggleDebug();
    		}
    		
    		// to re-activate dev-mode, uncomment below
//    		if(handler.gPressed()) {
//    			viewMode = false;
//    			handler.resetCameraToPlayer();
//    		}
    	}
    }

    private void renderCheckpoints(RenderWindow w) {
    	if(checkpoints != null) {
	    	for(Checkpoint c : checkpoints) {
	    		c.render(w);
	    	}
    	}
    }
    
    @Override
    public void render(RenderWindow w) {
		//render entities
		handler.setToMainView();
		//render background
		mainBackG.render(w);
		handler.renderMainEntities(w);
		renderCheckpoints(w);

		handler.setToLowerView();
		if (lowerLayer != null) {
			//render background
			lowerBackG.render(w);
			handler.renderLowerEntities(w);
			w.draw(playerMarker);
			renderCheckpoints(w);
		} 
		else {
			w.draw(nullLayer);
		}

		handler.setToUpperView();
		if (upperLayer != null) {
			//render background
			lowerBackG.render(w);
			handler.renderUpperEntities(w);
			w.draw(playerMarker);
			renderCheckpoints(w);
		} 
		else {
			w.draw(nullLayer);
		}

		//render UI elements
		// rendered last so that they appear on top of everything else.
		handler.setToDefaultView();
		progBar.render(w);
		pouch.render(w);
		ptv.render(w);
		w.draw(rightLabel);
		w.draw(leftLabel);
		w.draw(centreLabel);
		w.draw(pauseMenu);
    }
    
    private void setLayerArrangement() {
        handler.designateMain(mainLayer);
        handler.designateLower(lowerLayer);
        handler.designateUpper(upperLayer);
	}
    
    private void addEntitiesIntoManager() {
    	for(Entity e : layer1.getEntityObjects()) {
    		handler.addEntity(e);
    	}
    	for(Entity e : layer2.getEntityObjects()) {
    		handler.addEntity(e);
    	}
    	for(Entity e : layer3.getEntityObjects()) {
    		handler.addEntity(e);
    	}
    }
    
    /**
     * Method to switch to the upper layer in the current layer setup
     */
    public void moveToUpperLayer() {
    	if(lowerLayer == null) {
    		handler.playSound(SoundManager.MOVE_LAYER);
    		
	    	handler.shiftEntitiesUp();

			lowerLayer = layer1;
	        mainLayer = layer2;
	        upperLayer = layer3;

			lowerBackG = backG1;
			mainBackG = backG2;
			upperBackG = backG1;

	        leftLabel.setString("Lower Layer");
	        rightLabel.setString("Upper Layer");
	        centreLabel.setString("Middle Layer");
		}
    	
    	else {
    		if(upperLayer != null) {
    			handler.playSound(SoundManager.MOVE_LAYER);
    			
            	handler.shiftEntitiesUp();
        		
            	upperLayer = null;
        		mainLayer = layer3;
        		lowerLayer = layer2;

        		upperBackG = backG1;
        		mainBackG = backG2;
        		lowerBackG = backG1;

        		leftLabel.setString("Middle Layer");
        		centreLabel.setString("Upper Layer");
        	}
    	}
    	
    	for(Checkpoint c : checkpoints) {
    		handler.designateMain(c);
    	}
    }
    
    /**
     * Method to switch to the lower layer in the current layer setup
     */
    public void moveToLowerLayer() {
    	
    	//moving from in when upper is in main
		if(upperLayer == null) {
			handler.playSound(SoundManager.MOVE_LAYER);
			
			handler.shiftEntitiesDown();

			lowerLayer = layer1;
	        mainLayer = layer2;
	        upperLayer = layer3;

	        leftLabel.setString("Lower Layer");
	        rightLabel.setString("Upper Layer");
	        centreLabel.setString("Middle Layer");
		}
    	else {
    		//moving from starting layer setup
        	if(upperLayer != null) {
        		handler.playSound(SoundManager.MOVE_LAYER);
        		
        		handler.shiftEntitiesDown();

        		upperLayer = layer2;
        		mainLayer = layer1;
        		lowerLayer = null;

        		rightLabel.setString("Middle Layer");
        		centreLabel.setString("Lower Layer");
        	}
    	}
		
		for(Checkpoint c : checkpoints) {
    		handler.designateMain(c);
    	}
    }
    
    public void moveMarkerToPlayer() {
    	playerMarker.setPosition((float)player.getWorldX(), (float)(Launcher.WIN_HEIGHT-(Launcher.WIN_HEIGHT*0.1) + handler.getYOffset()));
    }

    // getters and setters
    public void setCompleted() {
        completed = true;
    }
    
    public void setTimerText(String s) {
    	ptv.setTimerText(s);
    }
    
    public void setTimerVisible(boolean b) {
    	ptv.setVisible(b);
    }
    
    public void updateSpeed(double sf) {
    	lvlSpeed = (lvlSpeed/(sf - 0.1)) * sf;
    	prevSpeed = sf * BASE_SPEED;
    }
    
    public void slowDown() {
    	lvlSpeed /= 2;
    	handler.getPlayer().setXSpeed(lvlSpeed);
    }
    
    public void resetSpeed() {
    	lvlSpeed = prevSpeed;
    	handler.getPlayer().setXSpeed(lvlSpeed);
    }
    
    public Checkpoint getRespawnPoint() {
    	return respawnPoint;
    }
    
    public void nullRespawn() {
    	respawnPoint = null;
    }
    
    public int getCheckpointNum() {
    	return checkpointNum;
    }
    
    @Override
    public boolean isCompleted() {
        return completed;
    }
    
    public Layer getMainLayer() {
    	return mainLayer;
    }

	public Layer getLowerLayer() {
		return lowerLayer;
	}

	public Layer getUpperLayer() {
		return upperLayer;
	}
	
	public ProgressBar getProgressBar() {
		return progBar;
	}
	
	public PowerUpPouch getPowerUpPouch() {
		return pouch;
	}
	
	public boolean inLowerLayer() {
		return lowerLayer == null;
	}
	
	public boolean inUpperLayer() {
		return upperLayer == null;
	}

	public double getViewModeOriginX() {
		return viewModeOriginX;
	}

	public double getViewModeOriginY() {
		return viewModeOriginY;
	}
	
	public double getLevelSpeed() {
		return lvlSpeed;
	}
	
	public double getSpeedFactor() {
		return speedFactor;
	}
	
	public double getInitialYOffset() {
		return initYOffset;
	}
	
	public void setNullLayerPosition(float x, float y) {
		nullLayer.setPosition(x, y);
	}
}

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * Class to handler loading and switching between levels - potentially unnecessary.
 * @author Alexander Bousbaine, Marta Adamska.
 *
 */
public class LevelManager {
	//handler instance for use by levels and this -M
	private Handler handler;
	
	// Array to represent all levels -A
	// I assume that the levels and cutscenes are in one array and for now there's one cutscene
	// for each level.
	// The contents of the array start with 0 - cutscene, 1 - level etc. -M
	private ArrayList<GameStage> stages;
	private GameStage currentStage;

	private Text deadText, layerText, respawnText, confirmTxt, ySkip, nSkip;
	private RectangleShape confirmationBox;
	
	//This should help progress between the levels I think -M
	private int lvlNum, totalStages;
	private double[] speedFactors;
	private double speedFactor;
	private boolean dispTxt, confirmSkip;

	public LevelManager(Handler h) {
		handler = h;
		// Levels

		totalStages = 13;
		stages = new ArrayList<>();
		dispTxt = confirmSkip = false;

		deadText = new Text("You Died!", handler.getFontBlackChancery(), 36);
		deadText.setPosition(Launcher.WIN_WIDTH / 2 - (deadText.getLocalBounds().width / 2), (float) (Launcher.WIN_HEIGHT * 0.3));
		deadText.setStyle(Text.BOLD);
		deadText.setColor(Color.RED);
		
		layerText = new Text("You can Change Layers if you Want.", handler.getFontBlackChancery(), 36);
		layerText.setPosition(Launcher.WIN_WIDTH / 2 - (layerText.getLocalBounds().width / 2), (float) (Launcher.WIN_HEIGHT * 0.4));
		layerText.setStyle(Text.BOLD);
		layerText.setColor(Color.RED);
		
		respawnText = new Text("Then Press 'SPACE' to Respawn!", handler.getFontBlackChancery(), 36);
		respawnText.setPosition(Launcher.WIN_WIDTH / 2 - (respawnText.getLocalBounds().width / 2), (float) (Launcher.WIN_HEIGHT * 0.5));
		respawnText.setStyle(Text.BOLD);
		respawnText.setColor(Color.RED);
		
		confirmTxt = new Text("Are you sure you want to skip?\nImportant information may be missed!", handler.getFontOrangeKid(), 24);
		ySkip = new Text("YES\n[ ENTER ]", handler.getFontOrangeKid(), 24);
		nSkip = new Text("NO\n[ SPACE ]", handler.getFontOrangeKid(), 24);
		
		confirmTxt.setPosition((Launcher.WIN_WIDTH / 4) + 36, (float) ((Launcher.WIN_HEIGHT * 0.25) + 48));
		ySkip.setPosition((Launcher.WIN_WIDTH / 4) + 48, (float) ((Launcher.WIN_HEIGHT * 0.25) + 120));
		nSkip.setPosition((Launcher.WIN_WIDTH / 2) - 48, (float) ((Launcher.WIN_HEIGHT * 0.25) + 120));
		
		confirmationBox = new RectangleShape(new Vector2f(Launcher.WIN_WIDTH / 2, Launcher.WIN_HEIGHT / 2));
		confirmationBox.setPosition(Launcher.WIN_WIDTH / 4, (float) (Launcher.WIN_HEIGHT * 0.25));
		confirmationBox.setFillColor(new Color(180, 180, 180));
		confirmationBox.setOutlineColor(Color.BLACK);
		confirmationBox.setOutlineThickness(10);
		
		speedFactors = new double[] {0, 1.0, 0, 1.2, 0, 1.4, 0, 1.6, 0, 1.8, 0};

		initStages();

		lvlNum = 0;
		speedFactor = 1;

		currentStage = stages.get(lvlNum);

		if(lvlNum % 2 == 1 && lvlNum < 11) {
			((Level)currentStage).setupLevel(speedFactor);
		}
	}

	/**
	 * Method to save the current parameters,
	 * so after resetting the game the player can continue
	 * @param s the file where we want to hold the save in
	 */
	public void setSave(SaveFile s) {
		if(s != null) {
			lvlNum = s.getLevelNumber();
			handler.getPlayer().setRespawnX(s.getRespawnX());
			handler.getPlayer().setRespawnY(s.getRespawnY());
			speedFactor = s.getSpeedFactor();
			handler.getPlayer().setNumDeaths(s.getPlayerDeaths());
			
			currentStage = stages.get(lvlNum);
			
			if(lvlNum % 2 == 1) {
				//if there is not a respawn set and the player is should be further in than the start
				if(((Level) currentStage).getRespawnPoint() == null && s.getCheckpointNumber() > 0) {
					speedFactor -= 0.1;
					//because the player will hit the checkpoint as they start playing
				}
				
				((Level) currentStage).setupLevel(speedFactor);
			}
			
			handler.stopAllSounds();
			handler.playMusicForLevel(currentStage.getID());
			
			handler.getPlayer().resetToCheckpoint();
			
			if(handler.getPlayer().isDead()) {
				handler.getPlayer().nowAlive();
			}
		}
	}

	/**
	 * To initialise the stages for the game
	 * the cutscenes are created on even indexes
	 * the levels are created on odd indexes
	 */
	private void initStages() {
		int id = 1;
		for(int i = 0; i < totalStages - 2; i++) {
			//if statements so that the method doesn't add in levels/cutscenes that don't currently exist.
			if(i % 2 == 0) {
				stages.add(new Cutscene(id, handler));
			}
			else {
				stages.add(new Level(id, handler));
				id++;
			}
		}	
		stages.add(new Cutscene(71, handler));
		stages.add(new Cutscene(72, handler));
	}

	/**
	 * Method to return the Level instance of the current level.
	 * @return - Level instance of the current level.
	 */
	public GameStage getCurrentStage() {
		return currentStage;
	}
	
	public void nullRespawns() {
		for(GameStage s : stages) {
			if(s instanceof Level) {
				((Level) s).nullRespawn();
			}
		}
	}

	/**
	 * Method to move the next stage.
	 * Odd index = level
	 * Even index = cutscene
	 */
	public void moveToNextLevel() {
		
		if (currentStage.isCompleted() && lvlNum < totalStages - 1) {
			
			handler.pauseMusicForLevel(currentStage.getID());
			
			int oldID = currentStage.getID();
			
			//get speed factor from level just completed
			if(stages.indexOf(currentStage) % 2 == 1 && lvlNum < totalStages - 2) {
				speedFactor = ((Level) currentStage).getSpeedFactor();
			}
			
			//load new level
			lvlNum++;
			handler.resetEntityManagerLists();
			currentStage = stages.get(lvlNum);
			
			//if it is the cutscene that requires text input 
			if (stages.indexOf(currentStage) == 10) {
				if(((Cutscene)currentStage).isPasswordSuccess()) {
					lvlNum ++;
				}
			}
			
			//if new stage is a level
			if(stages.indexOf(currentStage) % 2 == 1) {
				
				((Level) currentStage).setupLevel(speedFactors[lvlNum]);
			}
			
			if(currentStage.getID() != oldID) {
				handler.stopAllSounds();
				handler.playMusicForLevel(currentStage.getID());
			}
			else {
				handler.playMusicForLevel(oldID);
			}
			
			//save new game state (checkpoint 0)
			handler.setSaveState(lvlNum, 0, 0, 0, speedFactor, handler.getPlayer().getNumDeaths());
			
			if(handler.getPlayer().isDead()) {
				handler.getPlayer().nowAlive();
			}

		}
	}
	
	/**
	 * Method to increment the speed factor.
	 * Should be used when setting up a new level and after passing a checkpoint.
	 */
	public void incrementSpeedFactor() {
		speedFactor += 0.1;
	}
	
	/**
	 * Renders the current level's background.
	 * @param w - RenderWindow instance to draw to.
	 */
	public void render(RenderWindow w) {
		currentStage.render(w);
		
		if(dispTxt) {
			w.draw(respawnText);
			w.draw(layerText);
			w.draw(deadText);
		}
		
		if(confirmSkip) {
			w.draw(confirmationBox);
			w.draw(confirmTxt);
			w.draw(ySkip);
			w.draw(nSkip);
		}
	}
	
	/**
	 * Method to update call the update method from the current level and 
	 * check whether it is necessary to move to the next level.
	 */
	public void update() {
		
		if(handler.escapePressed()) {
			if(handler.isPaused()) {
				handler.unpauseGame();
			}
			else {
				handler.pauseGame();
			}
		}
		
		if(confirmSkip) {
			//if enter pressed
			if(handler.enterPressed()) {
				//skip
				currentStage.setCompleted(true);
				confirmSkip = false;
			}
			if(handler.spacePressed()) {
				confirmSkip = false;
				handler.unpauseGame();
			}
		}

		//skip cutscene
		if(currentStage.getID() != 6) {
			if(handler.isPaused() && stages.indexOf(currentStage) % 2 == 0) {
				//needs confirmation
				confirmSkip = true;
				handler.unpauseGame();
			}
		}
		//cannot skip cutscene 6 as it is crucial to the game - deal with it instead
		else {
			handler.initEndMenu();
			
			if(handler.isPaused()) {
				handler.unpauseGame();
			}
			if(((Cutscene) currentStage).isPasswordSet()) {
				if(((Cutscene) currentStage).isPasswordSuccess()){
					lvlNum = totalStages - 1;	
				}
				else {
					lvlNum = totalStages - 2;
				}
				currentStage = stages.get(lvlNum);
			}
		}

		if(!handler.isPaused()) {
			if(!handler.getPlayer().isDead()) {
				dispTxt = false;
				currentStage.update();
			}
			else {
				dispTxt = true;
				//allow player to change layer
				if(handler.rightPressed()) {
					((Level) currentStage).moveToUpperLayer();
				}
				if(handler.leftPressed()) {
					((Level) currentStage).moveToLowerLayer();
				}
				//respawn player
				if(handler.spacePressed()) {
					handler.getPlayer().nowAlive();	
				}
			}
		}

		moveToNextLevel();
	}
}

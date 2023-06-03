import java.util.ArrayList;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * A class to allow interaction between classes that would otherwise be difficult.
 * @author Alexander Bousbaine, Zubayer Ahmed
 *
 */
public class Handler {
	
	private Main main;
	private Display display;
	private EntityManager entityManager;
	private KeyManager keyManager;
	private SoundManager soundManager;
	private Level currentLevel;
	private EndMenu endMenu;
		
	public Handler(Main m) {
		main = m;
		display = main.getDisplay();
		entityManager = main.getEntityManager();
		keyManager = main.getKeyManager();
		soundManager = main.getSoundManager();
		endMenu = null;
		currentLevel = null;
	}
	
	public void setEndMenu(EndMenu m) {
		endMenu = m;
	}
	public void setCurrentLevel(Level l) {
		currentLevel = l;
	}
	
	public void progressGameState() {
		main.incrementGameStateIndicator();
	}
	
	public void toMainMenu() {
		main.toMainMenu();
	}
	
	public SaveFile loadSave() {
		return SaveHandler.readInSaveFile();
	}
	
	/**
	 * Method which resets the views and all entities to the x and y positions provided.
	 * @param xPos - x amount from the starting x value when the level was loaded.
	 * @param yPos - y amount from the starting y value when the level was loaded.
	 */
	public void resetToPosition(double xPos, double yPos) {
		//reset entities first
		getPlayer().resetToPosition(xPos, yPos);
		
		if(currentLevel != null) {
			currentLevel.getProgressBar().resetToPosition(getPlayer().getWorldX());
			
			currentLevel.recoverPowerUps();

			//then do views
			if(getYOffset() < currentLevel.getInitialYOffset()) {
				//System.out.println("respawn using offests");
				display.resetViewsToPosition(getPlayer().getWorldX() - Player.X_START_OFFSET, getPlayer().getWorldY() - (Launcher.WIN_HEIGHT*0.8)/3);
			}
			else {
				//System.out.println("reset using position");
				display.resetViewsToPosition(getPlayer().getWorldX() - Player.X_START_OFFSET, yPos);
			}

			currentLevel.setNullLayerPosition((float)xPos, (float)yPos);
		}
	}
	
	public void moveToMiddleLayer() {
		if(currentLevel.inLowerLayer()) {
			currentLevel.moveToUpperLayer();
		}
		if(currentLevel.inUpperLayer()) {
			currentLevel.moveToLowerLayer();
		}
	}
	
	public void resetCameraToPlayer() {
		display.resetViewsToPosition(currentLevel.getViewModeOriginX(), currentLevel.getViewModeOriginY());
	}
	
	//end menu
	public void initEndMenu() {
		endMenu.init();
	}	
	
	//Main methods
	
	public void pauseGame() {
		main.pause();
		if(getPlayer() != null) {
			if(getPlayer().getActivePU() != null) {
				getPlayer().getActivePU().pauseTimer();
			}
		}
	}
	
	public void unpauseGame() {
		main.unpause();
		if(getPlayer() != null) {
			if(getPlayer().getActivePU() != null) {
				getPlayer().getActivePU().unpauseTimer();
			}
		}
		
	}
	
	public boolean isPaused() {
		return main.isPaused();
	}
	
	public void setSaveState(int lvlNum, double respX, double respY, int checkNum, double speedFactor, int playerDeaths) {
		main.setSaveState(lvlNum, respX, respY, checkNum, speedFactor, playerDeaths);
	}
	
	//KeyManager methods
	public boolean anyKeyPressed() {
		return keyManager.anyKeyPressed();
	}
	
	public boolean upPressed() {
		return keyManager.isUpPressed();
	}
	
	public boolean leftPressed() {
		return keyManager.isLeftPressed();
	}
	
	public boolean downPressed() {
		return keyManager.isDownPressed();
	}
	
	public boolean rightPressed() {
		return keyManager.isRightPressed();
	}
	
	public boolean spacePressed() {
		return keyManager.isSpacePressed();
	}
	
	public boolean escapePressed() {
		return keyManager.isEscapePressed();
	}
	/**
	 * @see KeyManager
	 */
	public boolean shiftPressed() {
		return keyManager.isShiftPressed();
	}

	public boolean enterPressed() {
		return keyManager.isEnterPressed();
	}

	public boolean enterReleased() {
		return keyManager.isEnterReleased();
	}
	
	public boolean gPressed() {
		return keyManager.isGPressed();
	}

	public boolean ePressed() {
		return keyManager.isEPressed();
	}

	public boolean rPressed() {
		return keyManager.isRPressed();
	}
	
	public boolean mPressed() {
		return keyManager.isMPressed();
	}

	public String txtInput() {
		return keyManager.enteredText();
	}
	
	public void resetTextInput() {
		keyManager.resetInput();
	}

	//EntityManager methods
	
	public void addEntity(Entity e) {
		entityManager.addEntity(e);
	}
	
	public void removeEntityById(int i) {
		entityManager.removeById(i);
	}
	
	public Entity getEntityById(int i) {
		return entityManager.getEntityById(i);
	}
	
	public void removeVictims() {
		entityManager.removeVictims();
	}
	
	public int getNewEntityId() {
		return entityManager.getIdForEntity();
	}
	
	public void designateMain(Layer l) {
		entityManager.designateMain(l.getLayerEntityIds());
	}
	
	public boolean designateMain(Entity e) {
		return entityManager.designateMain(e.getId());
	}
	
	public void designateLower(Layer l) {
		entityManager.designateLower(l.getLayerEntityIds());
	}
	
	public void designateUpper(Layer l) {
		entityManager.designateUpper(l.getLayerEntityIds());
	}
	
	public void renderMainEntities(RenderWindow w) {
		entityManager.renderMain(w);	
	}

	public void renderLowerEntities(RenderWindow w) {
		entityManager.renderLower(w);	
	}
	
	public void renderUpperEntities(RenderWindow w) {
		entityManager.renderUpper(w);	
	}

	public void updateEntities() {
		entityManager.updateEntities();
	}
	
	public void initialUpdate() {
		entityManager.initUpdate();
	}
	
	public void shiftEntitiesUp() {
		entityManager.shuffleEntitiesDown();
	}
	
	public void shiftEntitiesDown() {
		entityManager.shuffleEntitiesUp();
	}
	
	public void resetEntityManagerLists() {
		entityManager.resetEntityLists();
	}
	
	public double getPlayerWorldX() {
		return entityManager.getPlayer().getWorldX();
	}
	
	public double getPlayerWorldY() {
		return entityManager.getPlayer().getWorldY();
	}
	
	public Player getPlayer() {
		return entityManager.getPlayer();
	}

	public ArrayList<Entity> getMainEntities() {
		return entityManager.getMainList();
	}
	
	public void setPlayer(Player p) {
		entityManager.setPlayer(p);
	}
	
	public void resetEnemies() {
		entityManager.resetMovingEnemiesInMain();
		entityManager.resetEnemiesInMain();
	}
	
	//Display methods
	public double getMainViewHeight() {
		return display.getMainViewHeight();
	}
	
	public double getMainViewWidth() {
		return display.getWindowWidth();
	}
	
	public double getSubViewHeight() {
		return display.getSubViewHeight();
	}
	
	public double getSubViewWidth() {
		return display.getSubViewWidth();
	}
	
	public double getWindowHeight() {
		return display.getWindowHeight();
	}
	
	public double getWindowWidth() {
		return display.getWindowWidth();
	}
	
	public void scrollViews(double xVal, double yVal) {
		display.scroll(xVal, yVal);
	}
	
	public void setToMainView() {
		display.setToMainView();
	}
	
	public void setToUpperView() {
		display.setToUpperView();
	}

	public void setToLowerView() {
		display.setToLowerView();
	}
	
	public void setToDefaultView() {
		display.setToDefaultView();
	}
	
	public Vector2f getSubViewSize() {
		return display.getSubViewSize();
	}
	
	public double getXOffset() {
		return display.getXOffset();
	}
	
	public double getYOffset() {
		return display.getYOffset();
	}
	
	public Font getFontBlackChancery() {
		return display.getFBlackChancery();
	}
	
	public Font getFontOrangeKid() {
		return display.getFOneSlot();
	}
	
	public Font getFontSourceSans() {
		return display.getFSourceSansReg();
	}
	
	//Level methods
	public int getCurrentLevelId() {
		return currentLevel.getID();
	}
	
	public void slowDownLevelSpeed() {
		currentLevel.slowDown();
		entityManager.toggleEnemySpeed();
	}
	
	public void resetLevelSpeed() {
		currentLevel.resetSpeed();
		entityManager.toggleEnemySpeed();
	}
	
	public PowerUpPouch getPouch() {
		return currentLevel.getPowerUpPouch();
	}
	
	public void emptyPouch() {
		currentLevel.getPowerUpPouch().emptyPouch();
	}
	
	public void sendToSpentList(PowerUpEntity p) {
		currentLevel.addPowerUpToSpentList(p);
	}
	
	public void doubleJumpHeight() {
		getPlayer().doubleJumpHeight();
	}
	
	public void resetJumpHeight() {
		getPlayer().resetJumpHeight();
	}
	
	public void setTimerText(String s) {
		if(!s.equals("")) {
			currentLevel.setTimerVisible(true);
		}
		else {
			currentLevel.setTimerVisible(false);
		}
		currentLevel.setTimerText(s);
	}
	
	public double getLevelSpeed() {
		return currentLevel.getLevelSpeed();
	}
	
	public void moveMarkerToPlayer() {
		currentLevel.moveMarkerToPlayer();
	}
	
	public void passCheckpoint(Checkpoint c) {
		currentLevel.passCheckpoint(c);
	}
	
	public void completeLevel() {
		currentLevel.setCompleted();
	}
	
	public Checkpoint getRespawnPoint() {
		return currentLevel.getRespawnPoint();
	}
	
	public boolean isRespawnPoint(Checkpoint c) {
		return currentLevel.isRespawnPoint(c);
	}

	//soundManager methods
	public void playSound(int i) {
		soundManager.playSound(i);
	}
	
	public void playMusicForLevel(int id) {
		soundManager.playMusicForLevel(id);
	}
	
	public void pauseMusicForLevel(int id) {
		soundManager.pauseMusicForLevel(id);
	}
	
	public void stopAllSounds() {
		soundManager.stopAllSounds();
	}

	public boolean isMuted() {
		return soundManager.isMuted();
	}
	
	public void toggleMute() {
		soundManager.toggleMuteSounds();
	}
	
	public void volumeUp(){
		soundManager.volumeUp();
	}
	
	public void volumeDown(){
		soundManager.volumeDown();
	}
}

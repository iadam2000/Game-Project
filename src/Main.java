
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;


/*
 * Ideas:
 * User Java.io.Serialisable interface to make saving function - 
 * 		create a class to hold 'state' data which can be serialisaed and deserialised.
 * 
 * Easter Eggs:
 * Create secret mode which turns all enemy models into that of the queen.
 * Some secret button combo which kills bad guys at the beginning of the game and causes player to automatically win?
 */

/**
 * Main class to do initialisation and game loop
 * @author Alexander Bousbaine, Zubayer Ahmed
 */
public class Main {

	public static final int UNIT = Launcher.WIN_WIDTH / 256;

	private Handler mainHandler;
	private Display display;
	private EntityManager entityManager;
	private LevelManager levelManager;
	private KeyManager keyManager;
	private SoundManager soundManager;
	private SaveFile runningSave;

	private Menu[] menus;

	private int gameStateIndicator, fps;
	private boolean paused, viewFPS;

	public Main() {
	
		//instantiate all infrastructure
		display = new Display("Running Out of Time");
		entityManager = new EntityManager();
		keyManager = new KeyManager();
		soundManager = new SoundManager();
		
		//instantiate handler
		mainHandler = new Handler(this);
	
		//instantiate everything else (if it takes in a Handler it goes past here and takes in above).
		entityManager.setPlayer(new Player(40, 40, 20, 20, mainHandler));
		levelManager = new LevelManager(mainHandler);
		
		menus = new Menu[3];
		setupMenus();
		
		mainHandler.setEndMenu((EndMenu) menus[2]);
		
		/*to be able to set where you start the game
		 	PARAMETERS ARE:
						levelNum, xPosition, yPsosition, currentCheckpointNumber, speedFactor, numberOfDeaths
						
			see below for values to input to get the correct experience.
		*/
		//SaveHandler.saveState(0, 0, 0, 0, 1.0, 0);
		
		runningSave = SaveHandler.readInSaveFile();
		if(runningSave == null) {
			/*
			lvlNum = ... -- speedFactor
			0 = pre-game cutscene
			1 = level 1 -- sf = 1
			2 = cutscene 2
			3 = level 2 -- sf = 1.2
			4 = cutscene 2
			5 = level 3 -- sf = 1.4
			6 = cutscene 3
			7 = level 4 -- sf = 1.6
			8 = cutscene 4
			9 = level 5 -- sf = 1.8
			10 = post-game cutscene
		*/
			//don't change this one - change the one above (may be commented out)
			SaveHandler.saveState(0, 0, 0, 0, 1.0, 0);
			runningSave = SaveHandler.readInSaveFile();
		}
	
		//gameStateIndicator shows which 'state' the game is in.
		// 0 is the Main Menu
		// 1 is the actual game
		// 2 is the end of the game menu
		// gameStateIndicator can be used to index the menus array to display the correct menu
		// once Main Menu is set up, should be start at 0.
		gameStateIndicator = 0;
		paused = false;
		viewFPS = false;
		
		levelManager.setSave(runningSave);
	
		double current = System.nanoTime();
		double end = current + 1000000000;
		
		//game loop
		while(display.getWindow().isOpen()) {
			current = System.nanoTime();
			
			display.clear();
	
			update();
			render(display.getWindow());
			fps++;
				
			display.display();

			//Handle events
			for(Event event : display.getWindow().pollEvents()) {
		    	switch (event.type){
		    		//The user pressed the close button
		    		case CLOSED:
		    			mainHandler.stopAllSounds();
		    			
		    			//save current save file
		    			SaveHandler.saveFile(runningSave);
		    			
		    			//close game window
		    			display.getWindow().close();
				        break;

		    		case TEXT_ENTERED:
						keyManager.handleTextEntered(event.asTextEvent());
						break;

				    case KEY_PRESSED:
				    	keyManager.handleKeyPress(event.asKeyEvent());
				    	break;
				    			
				    case KEY_RELEASED:
				    	keyManager.handleKeyRelease(event.asKeyEvent());
				    	break;
				    			
				    default:
				    	break;
		    	}
			}
			
			if(current >= end && viewFPS) {
				System.out.println("fps: "+fps);
				fps = 0;
				end = current + 1000000000;
			}
		}
	}

	/**
	 * Populates the menu array with a menu for a game stage
	 */
	private void setupMenus() {
		menus[0] = new MainMenu(mainHandler);	
		menus[1] = new PauseMenu(mainHandler);
		menus[2] = new EndMenu(mainHandler);
	}

	/**
	 * Updates the menu for each game stage
	 */
	private void update() {
		//soundManager.checkQueues();
		
		if(gameStateIndicator == 1) {
			levelManager.update();
		}
		
		if(gameStateIndicator != 1 && !paused) {
			//if not in 'game' state and not already paused, then pause.
			paused = true;
		}
		
		if(paused) {
			//so input can be found from menus
			menus[gameStateIndicator].update();
		}
	}

	/**
	 * Renders the menu when needed
	 * @param w window to render the menu on
	 */
	private void render(RenderWindow w){
		// 1 is the game 'state'
		if(gameStateIndicator == 1) {
			levelManager.render(w);
		}
		
		if(paused) {
			// render menu
			menus[gameStateIndicator].render(w);
		}
	}

	/**
	 * Return the current display
	 * @return
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 *
	 * @return
	 */
	public EntityManager getEntityManager(){
		return entityManager;
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void incrementGameStateIndicator() {
		switch (gameStateIndicator) {
			case 0:
				toGame();
				break;
				
			case 1:
				toEndMenu();
				break;
				
			case 2:
				toMainMenu();
				break;
				
			default:
				System.out.println("invalid game state");
		}
	}

	/**
	 * Method to go to the main menu when starting the game
	 */
	public void toMainMenu() {
		gameStateIndicator = 0;
		
		mainHandler.resetEntityManagerLists();
		
		SaveHandler.saveFile(runningSave);
	}

	/**
	 * Start game on the current save
	 */
	public void toGame() {
		gameStateIndicator = 1;
		
		levelManager.setSave(runningSave);
	}

	/**
	 * Method to move to the end menu when the game was finished
	 */
	public void toEndMenu() {
		mainHandler.moveToMiddleLayer();
		gameStateIndicator = 2;
		
		//reset game save to initial state
		SaveHandler.saveState(0, 0, 0, -1, 1f, 0);
		runningSave = SaveHandler.readInSaveFile();
	}

	/**
	 * Method to save where the player should respawn after reopening the game
	 *
	 * @param lvlNum level number
	 * @param respX X coordinate where the player will respawn
	 * @param respY Y coordinate where the player will respawn
	 * @param checkNum the last checkpoint the player passed (start - 0, midway - 1, finish - 2)
	 * @param speedFactor the speed on the level we want to save
	 * @param playerDeaths how many deaths has the player had
	 */
	public void setSaveState(int lvlNum, double respX, double respY, int checkNum, double speedFactor, int playerDeaths) {
		if(checkNum == -1) {
			levelManager.nullRespawns();
			checkNum = 0;
		}
		runningSave = new SaveFile(lvlNum, respX, respY, checkNum, speedFactor, playerDeaths);
	}

	/**
	 * Pause the game
	 */
	public void pause() {	
		paused = true;
	}

	/**
	 * Unpause the game
	 */
	public void unpause() {
		paused = false;
	}
}

import java.io.Serializable;

/**
 * Class to save necessary data to make the game's state consistent across different play sessions.
 * @author Alexander Bousbaine
 *
 */
public class SaveFile implements Serializable {
	
	private static final long serialVersionUID = -747792990981512827L;
	
	private int levelNumber, playerDeaths, checkpointNumber;
	private double speedFactor, respawnX, respawnY;
	
	/**
	 * Constructor - takes in the necessary data to let the player appear in the same position when they close and open the game again.
	 * @param lvlNum level number of the player's current level.
	 * @param respX player's respawn x position.
	 * @param respY player's respawn y position.
	 * @param checkNum checkpoint number of the last checkpoint the player passed.
	 * @param sf current speed factor of the game.
	 * @param pDeaths number of times the player has died.
	 */
	public SaveFile(int lvlNum, double respX, double respY, int checkNum, double sf, int pDeaths) {
		levelNumber = lvlNum;
		respawnX = respX;
		respawnY = respY;
		checkpointNumber = checkNum;
		speedFactor = sf;
		playerDeaths = pDeaths;
	}
	
	/**
	 * Method to get the level number stored in this save object.
	 * @return level number of current level.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}
	
	/**
	 * Method to get the player's respawn x position.
	 * @return player's respawn x position.
	 */
	public double getRespawnX() {
		return respawnX;
	}
	
	/**
	 * Method to get the player's respawn y position.
	 * @return player's respawn y position.
	 */
	public double getRespawnY() {
		return respawnY;
	}
	
	/**
	 * Method to get the checkpoint number of the last checkpoint the player passed.
	 * @return checkpoint number of specified checkpoint.
	 */
	public int getCheckpointNumber() {
		return checkpointNumber;
	}
	
	/**
	 * Method to get the speed factor at the time of saving.
	 * @return speed factor from this save file.
	 */
	public double getSpeedFactor() {
		return speedFactor;
	}
	
	/**
	 * Method to get the number of player deaths stored in this save file.
	 * @return number of player deaths at time of save.
	 */
	public int getPlayerDeaths() {
		return playerDeaths;
	}
}

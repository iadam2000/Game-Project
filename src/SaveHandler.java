
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A class to deal with saving and loading save files for the game.
 * @author Alexander Bousbaine
 *
 */
public class SaveHandler {
	
	/**
	 * Method to save the game's state. Takes in variable to add to the save file.
	 * @param lvlNum level number of the player's current level.
	 * @param respX player's respawn x position.
	 * @param respY player's respawn y position.
	 * @param checkNum checkpoint number of the last checkpoint the player passed.
	 * @param speedFactor current speed factor of the game.
	 * @param playerDeaths number of times the player has died.
	 */
	public static void saveState(int lvlNum, double respX, double respY, int checkNum, double speedFactor, int playerDeaths) {
		
		SaveFile s = new SaveFile(lvlNum, respX, respY, checkNum, speedFactor, playerDeaths);
		
		try {
			FileOutputStream saver;	

			//Should use the .ser file extension, but...
			//we don't need to follow no conventions! XD
			saver = new FileOutputStream("src/Assets/Save_Files/ROOT_Save.totesLegitSaveFile");
			
			ObjectOutputStream converter = new ObjectOutputStream(saver); 
			
			converter.writeObject(s);
			
			converter.close();
		}
		catch (Exception e) {
			System.out.println("Error saving game.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to save the game's state. Takes in a ready-made save file and saves that.
	 * @param s
	 */
	public static void saveFile(SaveFile s) {
		if(s != null) {
			try {
				FileOutputStream saver;	

				//Should use the .ser file extension, but...
				//we don't need to follow no conventions! XD
				saver = new FileOutputStream("src/Assets/Save_Files/ROOT_Save.totesLegitSaveFile");
				
				ObjectOutputStream converter = new ObjectOutputStream(saver); 
				
				converter.writeObject(s);
				
				converter.close();
			}
			catch (Exception e) {
				System.out.println("Error saving game.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to read in a save file and load the game's state from that.
	 * @return returns a SaveFile object if one could be found, otherwise returns null.
	 */
	public static SaveFile readInSaveFile() {
		SaveFile save = null;
		
		try {
			FileInputStream inputStream = new FileInputStream("src/Assets/Save_Files/ROOT_Save.totesLegitSaveFile"); 
			
			ObjectInputStream objectDat = new ObjectInputStream(inputStream);
			
			save = (SaveFile)objectDat.readObject();
			
			objectDat.close();
			
		} catch (Exception e) {
			//System.out.println("Error loading save.");
			//e.printStackTrace();
		} 
		
		return save;	
	}
}

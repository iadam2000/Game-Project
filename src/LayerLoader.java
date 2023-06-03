
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class to load in layer data and associated data.
 * @author Alexander Bousbaine
 *
 */
public class LayerLoader {
	
	private String path;
	private int lvlLen, lvlHeight;
	
	//contains level data in string format from file.
	private ArrayList<String> rawLvlData;
		
	//contains level entity data as integer arrays of [x, y, width, height].
	private ArrayList<int[]> entityPositionData;
		
	//contains what type the entity data in above describes. Type should be at same index as data. 
	private ArrayList<String> entityTypes;
	
	//contains the data for each entity in Object arrays in the form [String, int[]].
	private ArrayList<Object[]> entityData;
	
	/**
	 * Constructor - initialises path and instantiates ArrayLists
	 */
	public LayerLoader() {
		path = "";
		rawLvlData = new ArrayList<>();
		entityPositionData = new ArrayList<>();
		entityTypes = new ArrayList<>();
		entityData = new ArrayList<>();
	}
	
	/**
	 * Method to get the length of the layer loaded in by this class.
	 * @return - the length of the layer in pixels (should be multiplied by UNIT after retrieval).
	 */
	public int getLayerLength() {
		int endX = 0;
		for(int[] dat : entityPositionData) {
			if(dat[0] + dat[2] > endX) {
				endX = dat[0] + dat[2];
			}
		}
		return endX;
	}
	
	/**
	 * Method to load levels from the path passed into the method.
	 * @param p - path to the level file to load.
	 */
	public void loadLevel(String p) {
		path = p;
		if(!path.equals("")) {
			readLevelFile();
			processLvlData();
			initDimensions();
		}
		else {
			System.out.println("No file path given.");
		}
	}
	
	/**
	 * Method to get the level data. 
	 * The arrays contain data about entities in the form [xPos, yPos, width, height].
	 * Note that for the 'enemy' entity type and its variants, the width and height portions may be unnecessary.
	 * @return An ArrayList of integer arrays.
	 */
	public ArrayList<int[]> getEntityPositionData() {
		return entityPositionData;
	}
	
	/**
	 * Method to get the types of entities which the level data describes.
	 * @return ArrayList of Strings. The index of String is the index of the data it applies to.
	 */
	public ArrayList<String> getEntityTypes(){
		return entityTypes;
	}
	
	/**
	 * Method to get the entities and data as an array of Object arrays.
	 * Object arrays are in form [String entityType, int[] entityData]. 
	 * As they are in an Object array (and so are treated as objects), they must be cast to (String) and (int[]) respectively when used.
	 * 
	 * entityData portion is in form [xPos, yPos, width, height].
	 * Note: width and height may be unnecessary (and so be 0) for the entity type 'enemy' and its variants.
	 * @return ArrayList of Object arrays containing entity data and entity type. 
	 */
	public ArrayList<Object[]> getEntityData(){
		for(int i = 0; i < entityPositionData.size(); i++) {
			entityData.add(new Object[] {entityTypes.get(i), entityPositionData.get(i)});
		}
		return entityData;
	}
	
	/**
	 * Method which gets the dimensions of the level based on the positions of the entities within it. 
	 */
	private void initDimensions() {
		for(int[] arr : entityPositionData) {
			lvlHeight = (lvlHeight < arr[1]) ? arr[1] : lvlHeight;
			lvlLen = (lvlLen < arr[0] + arr[2]) ? arr[0] + arr[2] : lvlLen;
		}
	}
	
	/**
	 * Method to read in the level car 'lvl.txt'
	 */
	private void readLevelFile(){
		File file = new File(path);

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line;
			while((line = br.readLine()) != null) {
				rawLvlData.add(line);
			}
			
			br.close();
		} catch (Exception e) {
			System.out.println("ERROR: Exception when reading a level file -");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that turns strings into integers etc.
	 */
	private void processLvlData() {
		
		for(String s : rawLvlData) {
			boolean breakout = false;
			int startPoint = 0, endPoint = 0, segment = 0;
			int[] parts = new int[4];
			String part;
			
			for(int i = 0; i < s.length() && !breakout; i++) {
				
				char token = s.charAt(i);
				
				switch(token) {
					case '/':
						if(s.length() > 1) {
							if(s.charAt(i+1) == '/') {
								breakout = true;
							}
						}
						break;
				
					case ',':
						endPoint = i; 
						part = (s.substring(startPoint, endPoint)).replace(" ","");
						parts[segment] = Integer.parseInt(part);
						segment++;
						startPoint = endPoint + 1;
						break;
						
					case ':':
						endPoint = i;
						part = (s.substring(startPoint, endPoint)).replace(" ","");
						parts[segment] = Integer.parseInt(part);
						
						segment = 0;
						startPoint = i + 1;
						endPoint = i + 1;
						
						entityPositionData.add(parts);
						
						parts = new int[4];
						break;
					
					case ';':
						endPoint = i;
						String type = (s.substring(startPoint, endPoint)).replace(" ","");
						
						entityTypes.add(type);
						
						startPoint = i + 1;
						endPoint = i + 1;
						break;
						
					default:
						break;
				}
			}
		}
	}
}

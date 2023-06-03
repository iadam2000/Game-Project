
import java.util.ArrayList;

/**
 * A class to represent a single layer of the 3 layer level format
 * @author Marta Adamska, Alexander Bousabine, Charlotte Brocksom
 *
 */
public class Layer {
    private Handler handler;
    private LayerLoader layerLoader; // "LayerLoader"
 
    // int level to represent the current level
    // Layer to show which layer we are currently loading
    private int level, layer, numCheckpoints, len, r, g, b;
    
    private ArrayList<Integer> checkpointData, layerEntityIds;

    // This array holds the data for all the entities on this layer
    private ArrayList<Object[]> entityData;
    
    //ArrayList to hold all entity objects for addition into EntityHandler stuff
    private ArrayList<Entity> entityObjects;

    // The array holds the colours of the platforms
    private int[] platformColours = new int[]{ 237, 229, 77, 138, 54, 16, 154, 166, 252, 42, 116, 83, 76, 54, 40};

    // The array holds the colours of the obstacles
    private int[] obstacleColours = new int[]{4, 8, 96, 255, 151, 151, 146, 208, 80, 254, 159, 106, 86, 114, 148};
    
    public Layer(int level, int layer, Handler h) {
        handler = h;
        this.level = level;
        this.layer = layer;
        layerLoader = new LayerLoader();
        entityObjects = new ArrayList<>();
        layerEntityIds = new ArrayList<>();
        checkpointData = new ArrayList<>();

        layerLoader.loadLevel("src/Assets/Levels/Level" + level + "/Layer" + layer + ".txt");
        entityData = layerLoader.getEntityData();
        
        numCheckpoints = 0;
        
        len = 10000;
        r = 3 * (level - 1);
        g = (3 * (level - 1)) + 1;
        b = (3 * (level - 1)) + 2;

        createEntities();
    }
    
    // Creates new instances of entities in the array
    private void createEntities() {
    	int id;
        for (Object[] entity : entityData) {
            switch ((String) entity[0]) {
                case "platform":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                    entityObjects.add(new Platform((int[]) entity[1], id, handler, platformColours[r],  platformColours[g], platformColours[b]));
                    break;

                case "obstacle":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                	entityObjects.add(new Obstacle((int[]) entity[1], id, handler, obstacleColours[r], obstacleColours[g], obstacleColours[b]));
                    break;

                case "enemy":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                	entityObjects.add(new Enemy((int[]) entity[1], id, handler));
                    break;

                case "movingEnemy":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                	entityObjects.add(new MovingEnemy((int[]) entity[1], id, handler)); // The 10 is an example speeed
                    break;

                case "slowDown":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                	entityObjects.add(new SlowDownEntity((int[]) entity[1], id, handler));
                	break;

                case "doubleJump":
                	id = handler.getNewEntityId();
                	layerEntityIds.add(id);
                	entityObjects.add(new DoubleJumpEntity((int[]) entity[1], id, handler));
                	break;

                case "checkpoint":
                	numCheckpoints++;
                	checkpointData.add(((int[]) entity[1])[0]);
                	checkpointData.add(((int[]) entity[1])[1]);
                	break;

                case "levelEnd":
                	//need to define an object here for some reason cuz otherwise it makes one real big obstacle on the ground and im not sure why

                	len = ((int[]) entity[1])[0] * Main.UNIT;

                	numCheckpoints++;
                	checkpointData.add(((int[]) entity[1])[0]);
                	checkpointData.add(((int[]) entity[1])[1]);
                	break;

                default:
                    System.out.println("Oops we don't have an entity like that (or someone made a typo) - \""+(String) entity[0]+"\"");
                    break;
            }
        }
    }

    public int getLayer() {
        return layer;
    }
    
    public int getNumCheckpoints() {
    	return numCheckpoints;
    }
    
    public ArrayList<Integer> getCheckpointData(){
    	return checkpointData;
    }
    
    public ArrayList<Integer> getLayerEntityIds() {
    	return layerEntityIds;
    }
    
    public int getLength() {
    	return len;
    }
    
    public ArrayList<Entity> getEntityObjects(){
    	return entityObjects;
    }
}
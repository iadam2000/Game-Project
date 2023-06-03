
import java.util.ArrayList;

import org.jsfml.graphics.RenderWindow;

/**
 * Class to deal with entities e.g. delete them once off screen etc..
 * @author Alexander Bousbaine
 */
public class EntityManager {

	private int nextFreeId = -1;
	private ArrayList<Entity> entityList, mainList, lowerList, upperList, lowerExtension, upperExtension, victimList;
	private	Player player;
	private boolean slowed;
	
	/**
	 * Constructor - sets up entity lists.
	 */
	public EntityManager() {
		entityList = new ArrayList<>();
		mainList = new ArrayList<>();
		upperList = new ArrayList<>();
		lowerList = new ArrayList<>();
		victimList = new ArrayList<>();
		lowerExtension = upperExtension = null;
		
		slowed = false;
	}
	
//	public void checkForCheckpoints() {
//		for(Entity e : mainList) {
//			if(e instanceof Checkpoint) {
//				System.out.println("checkpoint exists");
//			}
//		}
//	}
	
	/**
	 * Method to reset all MovingEnemy instances to their original position.
	 * Should be called on player death.
	 */
	public void resetMovingEnemiesInMain() {
		for(Entity e : mainList) {
			if(e instanceof MovingEnemy) {
				((MovingEnemy) e).resetToOrigin();
			}
		}
	}
	
	/**
	 * Method to reset all Enemy instances to their original position.
	 * Should be called on player death.
	 */
	public void resetEnemiesInMain() {
		for(Entity e : mainList) {
			if(e instanceof Enemy) {
				((Enemy) e).resetToOrigin();
			}
		}
	}
	
	/**
	 * Method to slow down or speed up the speed of Enemy instances.
	 * Used to create the 'slow down' effect of the slow down power-up.
	 */
	public void toggleEnemySpeed() {
		if(slowed) {
			for(Entity e : mainList) {
				if(e instanceof Enemy) {
					((Enemy) e).speedUp();
				}
			}
		}
		else {
			for(Entity e : mainList) {
				if(e instanceof Enemy) {
					((Enemy) e).slow();
				}
			}
		}
		slowed = !slowed;
	}
	
	/**
	 * Method to add an entity into the EntityManager lists.
	 * @param e Entity to add to the lists
	 */
	public void addEntity(Entity e) {
		if(e != null) {
			entityList.add(e);
		}
	}
	
	/**
	 * Method to find an entity instance by its id.
	 * @param i the id of the entity to instance to return.
	 * @return the Entity instance with the id provided, or null if not found.
	 */
	public Entity getEntityById(int i) {
		for(Entity e : entityList) {
			if(e.getId() == i) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Method to get a new unique id for an entity.
	 * @return new id for an entity.
	 */
	public int getIdForEntity() {
		nextFreeId++;
		return nextFreeId;
	}
	
	/**
	 * Method to remove an entity from the game.
	 * Does this by adding the entity to a special list which will be scanned for removal at a later point.
	 * @param i id of the entity to remove.
	 */
	public void removeById(int i) {
		victimList.add(getEntityById(i));
	}
	
	/**
	 * Method to designate that multiple entities should be in the main layer.
	 * @param ids the ids of the entities that should be in the main layer.
	 */
	public void designateMain(ArrayList<Integer> ids) {
		Entity e;
		for(int i : ids) {			
			if((e = getEntityById(i)) != null) {
				if(!mainList.contains(e)) {
					mainList.add(e);
				}
			}
		}
	}
	
	/**
	 * Method to designate a single entity as part of the main layer.
	 * Will return false if the entity is already in the main list.
	 * @param id id of the entity to add.
	 * @return true if the entity was added, false if not.
	 */
	public boolean designateMain(int id) {
		Entity e;
		if((e = getEntityById(id)) != null) {
			if(!mainList.contains(e)) {
				mainList.add(e);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to designate multiple entities into the lower layer's list.
	 * @param ids ids of the entities to add to the lower layer's list.
	 */
	public void designateLower(ArrayList<Integer> ids) {
		Entity e;
		for(int i : ids) {
			if((e = getEntityById(i)) != null) {
				if(!lowerList.contains(e)) {
					lowerList.add(e);
				}
			}
		}
	}
	
	/**
	 * Method to designate multiple entities as being part of the upper layer.
	 * @param ids the ids of the entities that should be in the upper layer.
	 */
	public void designateUpper(ArrayList<Integer> ids) {
		Entity e;
		for(int i : ids) {
			if((e = getEntityById(i)) != null) {
				if(!upperList.contains(e)) {
					upperList.add(e);
				}
			}
		}
	}
	
	/**
	 * Move entities up from one layer to the next (i.e. lower layer to main layer).
	 */
	public void shuffleEntitiesUp() {
		if(upperExtension == null) {
			upperExtension = upperList;
			upperList = mainList;
			mainList = lowerList;
			lowerList = lowerExtension;
			lowerExtension = null;
		}
	}
	
	/**
	 * Method to move entities down one layer (e.g. main layer to lower layer).
	 */
	public void shuffleEntitiesDown() {
		if(lowerExtension == null) {
			lowerExtension = lowerList;
			lowerList = mainList;
			mainList = upperList;
			upperList = upperExtension;
			upperExtension = null;
		}
	}
	
	/**
	 * Method to clear all entity lists.
	 * Should be called at the end of each level to prepare for the next.
	 */
	public void resetEntityLists() {
		entityList = new ArrayList<>();
		mainList = new ArrayList<>();
		upperList = new ArrayList<>();
		lowerList = new ArrayList<>();
		lowerExtension = upperExtension = null;
	}
	
	/**
	 * Method to empty the victim list, removing entities in it from the game.
	 * Should be called after {@link Main#update()} and {@link Main#Render(RenderWindow)} in the game loop.
	 */
	public void removeVictims() {
		for(Entity e : victimList) {
			mainList.remove(e);
			entityList.remove(e);
		}
		victimList.clear();
	}
	
	/**
	 * Method to update all entities once.
	 * Should be done at the start of each level because otherwise entities on the upper/lower layer 
	 * will not be visible until the player goes to that layer for the first time.
	 */
	public void initUpdate() {
		for(Entity e : lowerList) {
			e.update();
		}
		for(Entity e : upperList) {
			e.update();
		}
	}
	
	/**
	 * Method to call the update method of all entities in the main layer, and the player.
	 */
	public void updateEntities() {
		player.update();
		for(Entity e : mainList) {
			e.update();
		}
	}
	
	/**
	 * Method to call the render method of all entities in the main layer.
	 * @param w RenderWindow to draw to.
	 */
	public void renderMain(RenderWindow w) {
		for(Entity e : mainList) {
			e.render(w);
		}
		player.render(w);
	}
	
	/**
	 * Method to call the render method of all entities in the upper layer.
	 * @param w RenderWindow to draw to.
	 */
	public void renderUpper(RenderWindow w) {
		for(Entity e : upperList) {
			e.render(w);
		}
	}
	
	/**
	 * Method to call the render method of all entities in the lower layer.
	 * @param w RenderWindow to draw to.
	 */
	public void renderLower(RenderWindow w) {
		for(Entity e : lowerList) {
			e.render(w);
		}
	}
	
	/**
	 * Method to get the list containing all entities in all layers.
	 * @return list containing all entities in the current level.
	 */
	public ArrayList<Entity> getList(){
		return entityList;
	}
	
	/**
	 * Method to set the player instance.
	 * @param p player instance.
	 */
	public void setPlayer(Player p) {
		player = p;
	}
	
	/**
	 * Method to get the player instance.
	 * @return player instance.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Method to return the list of entities in the main layer.
	 * @return list of entities in the main layer.
	 */
	public ArrayList<Entity> getMainList(){
		return mainList;
	}
}

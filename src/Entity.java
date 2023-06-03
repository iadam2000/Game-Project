
import java.io.IOException;
import java.nio.file.Paths;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * Class to represent entities - those being player, enemies, obstacles, etc... who should all extend this
 * @author Alexander Bousbaine, Marta Adamska
 *
 */
public abstract class Entity {
	
	/**
	 * Returned if a collision at the top of this entity is detected.
	 */
	public static final int TOP = 1;
	/**
	 * Returned if a collision at the bottom of this entity is detected.
	 */
	public static final int BOTTOM = 2;
	/**
	 * Returned if a collision at the left face of this entity is detected.
	 */
	public static final int LEFT = 3;
	/**
	 * Returned if a collision at the right face of this entity is detected.
	 */
	public static final int RIGHT = 4;
	
	private Handler handler;
	
	//sceneX and Y are the x and y position values for the entity on the screen.
	//worldX and Y are the x and y position values for the entity in relation to the entire 'world'.
	//width and height are the width and height of the entity.
	//id is a unique identifier which is be used to keep track of individual entities.
	//mod values modify their prefixed values (x, y, width, height)
	private double sceneX, sceneY, worldX, worldY, width, height, bXMod, bYMod, bWidMod, bHeitMod;
	private int id;
	
	//Texture entity to represent the entity's texture in graphical memory.
	private Texture tex;
	private Texture textures[];
	
	//The Sprite instance to make the Texture drawable.
	private Sprite skin = null;
	//If an entity has animations, then it will have multiple sprites.
	private Sprite[] sprites;
	
	//FloatRect objects to be collision boxes
	//protected for debugging's sake (above also) - should be made private for final product
	protected FloatRect AABB, bottomAABB, rightAABB;
	
	//debug code ^ - look for 'debug code' to find what code to remove
	
	private FloatRect intersection;
	
	//should be removed for final product
	public RectangleShape interBox;
	
	/**
	 * Constructor
	 * @param xPos - xPosition of the entity (scene-wise).
	 * @param yPos - y position of entity (scene-wise).
	 * @param wid - width of entity.
	 * @param ht - height of entity.
	 * @param h - handler instance to gain access to handler methods.
	 */
	public Entity(int xPos, int yPos, int wid, int ht, Handler h) {
		handler = h;
		
		worldX = xPos * Main.UNIT;
		worldY = yPos * Main.UNIT;
		width = wid * Main.UNIT;
		height = ht * Main.UNIT;
		
		worldY = (int)handler.getMainViewHeight() - worldY + width;
		
		tex = new Texture();
		
		bXMod = 0;
		bYMod = 0;
		bWidMod = 0;
		bHeitMod = 0;
		
		AABB = new FloatRect((float)worldX, (float)worldY, (float)width, (float)height);
		bottomAABB = new FloatRect((float)(worldX+(width/8)), (float)(worldY + height/2), (float)width/2, (float)height/2);
		rightAABB = new FloatRect((float)(worldX+(3*width/2)), (float)(worldY), (float)width/4, (float)height);
		
		interBox = new RectangleShape();
		interBox.setFillColor(Color.BLACK);
		
	}
	
	/**
	 * Constructor - takes in position data of the entity as an array in the format [x, y, width, height].
	 * @param positionData - position data of the entity as an array in the format [x, y, width, height].
	 * @param h - Handler instance to gain access to handler methods.
	 */
	public Entity(int[] positionData, Handler h) {
		handler = h;
		
		worldX = positionData[0] * Main.UNIT;
		worldY = positionData[1] * Main.UNIT;
		width = positionData[3] * Main.UNIT;
		height = positionData[2] * Main.UNIT;

		worldY = (int)handler.getMainViewHeight() - worldY + width;
		
		tex = new Texture();
		
		AABB = new FloatRect((float)worldX, (float)worldY, (float)height, (float)width);
		bottomAABB = null;
	}
	
	/**
	 * Method to shrink the AABB of an entity to 80% width
	 * Made for the Player's use.
	 */
	public void shrinkAABB() {
		bWidMod = -width/3;
		bXMod = width/6;
	}
	
	/**
	 * Method to return entity types.
	 * @return String representation of entity's type e.g. 'platform'.
	 */
	public abstract String getType();

	/**
	 * Method which is called every tick.
	 * Should update any variables which need regularly updating such as any position data.
	 */
	public abstract void update();
	
	/**
	 * Method which is called every tick.
	 * Should draw all drawable objects to the screen using the draw() method of the RenderWindow instance.
	 * @param w - RenderWindow instance to draw to.
	 */
	public abstract void render(RenderWindow w);
	
	/**
	 * Method that checks whether an entity collides with another entity.
	 * @param e - entity to check collision with.
	 * @return true if collision occurs, otherwise false.
	 */
	public boolean intersects(Entity e) {
		if(e != null) {
			intersection = AABB.intersection(e.getAABB());
			//remove below if - debug code
			if(intersection!=null && interBox != null) {
				interBox.setPosition(intersection.left, intersection.top);
				interBox.setSize(new Vector2f(intersection.width, intersection.height));
			}
			return (AABB.intersection(e.getAABB()) != null);
		}
		else {
			return false;
		}
	}
	
	/**
	 * Method to determine where an entity collides with another entity.
	 * @param e - entity to check for collisions with.
	 * @return Value related to top, bottom, right, or left depending on where the collision was detected.
	 */
	public int collidesWhereWith(Entity e) {
		double eX = e.getWorldX(), eY = e.getWorldY(), eWid = e.getWidth(), eHt = e.getHeight();
		
		if(bottomAABB != null) {
			if(bottomAABB.intersection(e.getAABB()) != null) {
				return BOTTOM;
			}
		}
		else {
			if(worldY+height >= eY && worldY+height < eY + (eHt/2)) {
				return BOTTOM;
			}
		}
		
		if(rightAABB != null) {
			if(rightAABB.intersection(e.getAABB()) != null) {
				return RIGHT;
			}
		}
		else {
			if(worldX+width >= eX && worldX+width < eX + (eWid/2)) {
				return RIGHT;
			}
		}
		
		if(eY + eWid >= worldY && eY + eWid < worldY + height/2) {
			return TOP;
		}
		
		if(worldX + (width/2) <= eX && worldX + width >= eX) {
			return LEFT;
		}
		
		return 0;
	}
	
	/**
	 * Method to update the size of the entity's skin sprite.
	 */
	public void updateSkinSize() {
		Vector2f texSize = new Vector2f(skin.getTexture().getSize());
		Vector2f target = new Vector2f((float)width, (float)height);
		Vector2f scale = Vector2f.componentwiseDiv(target, texSize);
		
		skin.setPosition((float)sceneX, (float)sceneY);
	    skin.setScale(scale);
	}
	
	/**
	 * Method to update where the entity's skin sprite is rendered on the screen.
	 */
	public void updateSkinPos() {
		skin.setPosition((float)worldX, (float)worldY);
		
		if(bottomAABB != null && rightAABB != null) {
			AABB = new FloatRect((float)(worldX + bXMod), (float)(worldY + bYMod), (float)(width + bWidMod), (float)(height + bHeitMod));
			bottomAABB = new FloatRect((float)(worldX+(width/4)), (float)(worldY + height/2), (float)(width/2 - 3*Main.UNIT), (float)height/2);
			rightAABB = new FloatRect((float)(worldX+(7*width/8)+bWidMod), (float)(worldY), (float)width/8, (float)(height*0.9));
		}
		else {
			AABB = new FloatRect((float)(worldX + bXMod), (float)(worldY + bYMod), (float)(width + bWidMod), (float)(height + bHeitMod));
		}
	}
	
	/**
	 * Method to load an image into the entity.
	 * Loads it in to a Texture and then initialises the Sprite instance from it.
	 * @param texturePath - the path of the texture to load in.
	 */
	public void loadTextureIntoSprite(String texturePath) {
		try {
		    tex.loadFromFile(Paths.get(texturePath));
		    tex.setSmooth(true);
		    
		}
		catch(IOException ex) {
			System.out.println("Trouble loading entity texture.");
		    ex.printStackTrace();
		}
		
		Vector2f texSize = new Vector2f(tex.getSize());
		Vector2f target = new Vector2f((float)width, (float)height);
		Vector2f scale = Vector2f.componentwiseDiv(target, texSize);
		
		skin = new Sprite(tex);
	    skin.setPosition((float)sceneX, (float)sceneY);
	    skin.setScale(scale);
	}
	
	/**
	 * Method that loads all textures provided in the 'paths' parameter into the textures array.
	 * @param - Array of paths which textures should be loaded from.
	 */
	public void loadTextures(String[] paths){
		textures = new Texture[paths.length];
		sprites = new Sprite[paths.length];
		
		for(int i = 0; i < paths.length; i++) {
			textures[i] = new Texture();
			
			try {
				textures[i].loadFromFile(Paths.get(paths[i]));
				textures[i].setSmooth(true);    
			}
			catch(IOException ex) {
				System.out.println("Trouble loading texture from path: "+paths[i]);
			    ex.printStackTrace();
			}
			
			Vector2f texSize = new Vector2f(textures[i].getSize());
			Vector2f target = new Vector2f((float)width, (float)height);
			Vector2f scale = Vector2f.componentwiseDiv(target, texSize);
			
			sprites[i] = new Sprite(textures[i]);
		    sprites[i].setScale(scale);
		}
	}
	
	/**
	 * Method to set the skin sprite to the index provided of the sprites array.
	 * @param i - index of the sprite array. Should be no smaller than 0. Should not be larger than the size of the sprite array.
	 */
	public void setTextureTo(int i) {
		skin = sprites[i];
	}
	
	//getters and setters
	/**
	 * Method to get skin Sprite.
	 * @return skin Sprite.
	 */
	public Sprite getSkin() {
		return skin;
	}
	
	/**
	 * Method to the entity's x position in regards to the world.
	 * @return x value in relation to entire world.
	 */
	public double getWorldX() {
		return worldX;
	}

	/**
	 * Method to the entity's y position in regards to the world.
	 * @return y value in relation to entire world.
	 */
	public double getWorldY() {
		return worldY;
	}

	/**
	 * Method to get the entity's x position in relation to the current window.
	 * @return x position in relation to the current window.
	 */
	public double getSceneX() {
		return worldX - handler.getXOffset();
	}

	/**
	 * Method to get the entity's y position in relation to the current window.
	 * @return y position in relation to the current window.
	 */
	public double getSceneY() {
		return worldY - handler.getYOffset();
	}

	/**
	 * Method to get the width of the entity.
	 * @return Entity's width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Method to get the entity's height.
	 * @return Entity's height.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Method to get the AABB (Axis Aligned Bounding Box) for this entity.
	 * @return - FloatRect used as AABB.
	 */
	public FloatRect getAABB() {
		return AABB;
	}
	
	/**
	 * Method to get the entity's id.
	 * @return Entity's id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Method to set the x position of this entity in regards to the world.
	 * @param worldX x position to set to.
	 */
	public void setWorldX(double worldX) {
		this.worldX = worldX;
	}

	/**
	 * Method to set the y position of this entity in regards to the world.
	 * @param worldX y position to set to.
	 */
	public void setWorldY(double worldY) {
		this.worldY = worldY;
	}
	
	/**
	 * Method to set the x position of this entity in regards to the window.
	 * @param worldX x position to set to.
	 */
	public void setSceneX(double f) {
		sceneX = f;
	}

	/**
	 * Method to set the y position of this entity in regards to the window.
	 * @param worldX y position to set to.
	 */
	public void setSceneY(double y) {
		sceneY = y;
	}
	
	/**
	 * Method to set the entity's id.
	 * @param i integer to set id to.
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * Method to set entity's width.
	 * @param width value to set width to.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Method to set entity's height.
	 * @param height value to set height to.
	 */
	public void setHeight(double height) {
		this.height = height;
		
	}
}

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * A class to represent the pouch in which the player will keep their power-ups.
 * @author Alexander Bousbaine
 *
 */
public class PowerUpPouch {

	private RectangleShape pouch;
	private Sprite powerUp;
	
	//vectors to represent the position of the pouch
	private Vector2f bottomLeftInMain;
	
	//other possible positions
	//private Vector2f bottomLeftOfMid, topRight, bottomRightInMain;
	
	/**
	 * Constructor - sets up visual elements of the pouch.
	 */
	public PowerUpPouch() {
		//topRight = new Vector2f(Launcher.WIN_WIDTH - 23*Main.UNIT, 4*Main.UNIT);
		//bottomLeftOfMid = new Vector2f(Launcher.WIN_WIDTH/2 - 19*Main.UNIT, (float) ((Launcher.WIN_HEIGHT * 0.8) + 4*Main.UNIT));
		//bottomRightInMain = new Vector2f(Launcher.WIN_WIDTH - 19*Main.UNIT, (float) ((Launcher.WIN_HEIGHT * 0.8) - 18*Main.UNIT));
		bottomLeftInMain = new Vector2f(2*Main.UNIT, (float) ((Launcher.WIN_HEIGHT * 0.8) - 18*Main.UNIT));
		
		//pouch to hold powerups.
		pouch = new RectangleShape(new Vector2f(17*Main.UNIT, 17*Main.UNIT));
		
		//switch vector here to change position
		pouch.setPosition(bottomLeftInMain);
		pouch.setFillColor(new Color(107, 73, 0));
		pouch.setOutlineColor(new Color(61, 46, 0));
		pouch.setOutlineThickness(2*Main.UNIT);		
		
		powerUp = null;
	}
	
	/**
	 * Method to render the pouch and its contents.
	 * @param w - RenderWindow instance to draw to.
	 */
	public void render(RenderWindow w) {
		w.draw(pouch);
		if(powerUp != null) {
			w.draw(powerUp);
		}
	}
	
	/**
	 * Method to remove visual of power-up being in the pouch.
	 */
	public void emptyPouch() {
		powerUp = null;
	}

	/**
	 * Method to set the contents of the power-up pouch.
	 * @param p - Power-up entity the player collided with.
	 * @return - true if image taken and set, else false.
	 */
	public boolean setPouchContents(Entity p) {
		if(powerUp == null) {
			Texture img = (Texture) p.getSkin().getTexture();
			
			Vector2f texSize = new Vector2f(img.getSize());
			Vector2f target = new Vector2f(15*Main.UNIT, 15*Main.UNIT);
			Vector2f scale = Vector2f.componentwiseDiv(target, texSize);
			
			powerUp = new Sprite(img);
			powerUp.setScale(scale);
			powerUp.setPosition(new Vector2f(3*Main.UNIT, (float) ((Launcher.WIN_HEIGHT * 0.8) - 17*Main.UNIT)));
			
			return true;
		}
		else {
			return false;
		}
	}
}

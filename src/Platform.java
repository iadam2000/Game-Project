
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * A class to represent platforms in the game.
 * @author Marta Adamska, Charlotte Brocksom
 *
 */
public class Platform extends Entity {

    private Color col;
    private Vector2f v;
    private RectangleShape rect;

    /**
     * Method to create a platform object - these are things the player can stand on.
     * @param data position data as an array to feed into {@link Entity#Entity(int[], Handler)}
     * @param i id of this entity.
     * @param h handler instance for superclass.
     * @param r red value of the rgb color for this platform.
     * @param g green value of the rgb color for this platform.
     * @param b blue value of the rgb color for this platform.
     */
    public Platform (int[] data, int i, Handler h, int r, int g, int b) {
        super(data, h);
        setId(i);

        col = new Color(r, g, b);
        v = new Vector2f((float)this.getHeight(), (float)this.getWidth());
        rect = new RectangleShape(v);
        rect.setFillColor(col);
        rect.setPosition((float)getWorldX(), (float)getWorldY());
        rect.setOutlineColor(Color.BLACK);
        rect.setOutlineThickness(3);
    }

    @Override
    public void update() {
    	
    }

    @Override
    public void render(RenderWindow w) { 
        w.draw(rect);
    }
    
    @Override
    public String getType() {
        return "platform";
    }
}
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

/***
 * A class to represent the background of each layer.
 * @author Charlotte Brocksom
 */
public class Background{

    private Texture backgroundTx;
    private Sprite background;

    /**
     * Constructor - class to create a background
     * @param level number of the level
     * @param view the position on the display where we want the image to be
     */
    public Background(int level, String view) {

        backgroundTx = new Texture();

        try {
            backgroundTx.loadFromFile(Paths.get("src/Assets/Backgrounds/back" + level + view));
            background = new Sprite(backgroundTx);
            background.setScale((float)0.806, (float)0.645);

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        background.setPosition(0, 0);
    }
    
    /**
     * Constructor - Creates a background for a cutscene.
     */
    public Background() {

        backgroundTx = new Texture();

        try {
            backgroundTx.loadFromFile(Paths.get("src/Assets/Backgrounds/cutBack.png"));
            background = new Sprite(backgroundTx);
            background.setScale((float)0.630, (float)0.645);

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        background.setPosition(0, 0);
    }

    /**
     * Create background by passing path as the only argument.
     * @param path - path to the file that should be used as the background image.
     */
    public Background(String path) {
    	
    	backgroundTx = new Texture();

        try {
            backgroundTx.loadFromFile(Paths.get(path));
            background = new Sprite(backgroundTx);
            
            Vector2f texSize = new Vector2f(backgroundTx.getSize());
    		Vector2f target = new Vector2f((float)Launcher.WIN_WIDTH, (float)Launcher.WIN_HEIGHT);
    		Vector2f scale = Vector2f.componentwiseDiv(target, texSize);

    	    background.setScale(scale);

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        background.setPosition(0, 0);
    }

    /**
     * Render the image onto the screen
     * @param w window to render the background to.
     */
    public void render(RenderWindow w) {
        w.draw(background);
    }

    /**
     * Method to get the Sprite object that holds the image used as the background.
     * @return Sprite object that holds the background image.
     */
    public Sprite getBackground(){
        return background;
    }
}

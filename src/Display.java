
import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;

/**
 * Class to setup the window and make views in the correct places.
 * @author Alexander Bousbaine
 */
public class Display {

	private double windowWidth, windowHeight, subViewHeight, subViewWidth, mainViewHeight;
	private RenderWindow window;
	private View mainView, lowerView, upperView, defaultView;
	private Vector2f viewDimensions;
	
	private Font blackChancery, oneSlot, sourceSansReg;

	private double viewXOffset, viewYOffset;
	
	/**
	 * Constructor - sets up the display window.
	 * @param name - name of the window.
	 */
	public Display(String name) {
		//antialiasing - for enhanced smoothness - not in atm
		//ContextSettings settings = new ContextSettings(16);
		
		viewXOffset = 0;
		viewYOffset = 0;
		
		windowWidth = Launcher.WIN_WIDTH;
		windowHeight = Launcher.WIN_HEIGHT;

		mainViewHeight = Launcher.WIN_HEIGHT * 0.8;
		
		subViewWidth = Launcher.WIN_WIDTH * 0.5;
		subViewHeight = Launcher.WIN_HEIGHT * 0.2;
		
		viewDimensions = new Vector2f((int)windowWidth, (int)windowHeight);
		
		//create window
		window = new RenderWindow();
		//make window have a close button and a title bar, but 
		window.create(new VideoMode(Launcher.WIN_WIDTH, Launcher.WIN_HEIGHT), name, RenderWindow.CLOSE | RenderWindow.TITLEBAR);
		
		//create the views, 'cameras', to view the three screens.
		//get a reference to the underlying view which could be used for overlays i.e. progress bar, inventory, pause menu, etc...
		defaultView = (View) window.getDefaultView();
		mainView = new View(new Vector2f(Launcher.WIN_WIDTH/2, Launcher.WIN_HEIGHT/2), viewDimensions);
		lowerView = new View(new Vector2f(Launcher.WIN_WIDTH/2, Launcher.WIN_HEIGHT/2), viewDimensions);
		upperView = new View(new Vector2f(Launcher.WIN_WIDTH/2, Launcher.WIN_HEIGHT/2), viewDimensions);		
		
		mainView.setViewport(new FloatRect(0f, 0f, 1f, 0.8f));
		lowerView.setViewport(new FloatRect(0f, 0.8f, 0.5f, 0.2f));
		upperView.setViewport(new FloatRect(0.5f, 0.8f, 0.5f, 0.2f));
		
		//And as below, because the height has been shrunk by 20%, we need to increase the width by 25% to keep ratios equal. Or something.
		mainView.reset(new FloatRect(0, 0, 1.25f * Launcher.WIN_WIDTH, Launcher.WIN_HEIGHT));
		
		//since the height is shrunk by a factor of five, the width needs to be multiplied make things equal.
		//But since the width was shrunk by 2 we divide that factor by 2.
		// ...or something, I'm not sure. It all came together in my head but I'm not sure how.
		lowerView.reset(new FloatRect(0, 0, 2.5f * Launcher.WIN_WIDTH, Launcher.WIN_HEIGHT));
		upperView.reset(new FloatRect(0, 0, 2.5f * Launcher.WIN_WIDTH, Launcher.WIN_HEIGHT));
		
		//set frame-rate limit
		window.setFramerateLimit(60);
		
		setupFonts();
		
	}
	
	private void setupFonts() {
		blackChancery = new Font();
		try {
			blackChancery.loadFromFile(Paths.get("src/Assets/Fonts/BLKCHCRY.ttf"));
		} catch (IOException e) {
			System.out.println("Error with font BLACKCHANCERY");
			e.printStackTrace();
		}
		
		oneSlot = new Font();
        try {
            oneSlot.loadFromFile(Paths.get("src/Assets/Fonts/OneSlot.ttf"));
        } catch (IOException e) {
            System.out.println("Error with font ONESLOT");
            e.printStackTrace();
        }
        
        sourceSansReg = new Font();
        try {
            sourceSansReg.loadFromFile(Paths.get("src/Assets/Fonts/SourceSansPro-Regular.ttf"));
        } catch (IOException e) {
            System.out.println("Error with font SOURCESANSPRO");
            e.printStackTrace();
        }
	}


	/**
	 * Returns Black Chancery font instance.
	 * @return Black Chancery Font Instance.
	 */
	public Font getFBlackChancery() {
		return blackChancery;
	}
	
	/**
	 * Returns One Slot font instance.
	 * @return One Slot Font Instance.
	 */
	public Font getFOneSlot() {
		return oneSlot;
	}
	
	/**
	 * Returns Source Sans Regular font instance.
	 * @return Source Sans Regular Font Instance.
	 */
	public Font getFSourceSansReg() {
		return sourceSansReg;
	}
	
	/**
	 * Allows objects to be drawn to the main view (upper 80% height, full width).
	 */
	public void setToMainView() {
		window.setView(mainView);
	}
	
	/**
	 * Allows objects to be drawn to the lower view (bottom 20% height, left half).
	 */
	public void setToLowerView() {
		window.setView(lowerView);
	}
	
	/**
	 * Allows objects to be drawn to the upper view (bottom 20% height, right half).
	 */
	public void setToUpperView() {
		window.setView(upperView);
	}
	
	/**
	 * Allows objects to be drawn to the default view (whole screen).
	 */
	public void setToDefaultView() {
		window.setView(defaultView);
	}
	
	/**
	 * Method to move all views by the give x and y values.
	 * @param xVal - amount to move on x axis.
	 * @param yVal - amount to move on y axis.
	 */
	public void scroll(double xVal, double yVal) {
		mainView.move((float)xVal, (float)yVal);
		lowerView.move((float)xVal, (float)yVal);
		upperView.move((float)xVal, (float)yVal);
		
		viewXOffset += xVal;
		viewYOffset += yVal;
	}
	
	/**
	 * Allows access to the clear() method of the RenderWindow
	 */
	public void clear() {
		window.clear(Color.WHITE);
	}
	
	/**
	 * Allows access to the display() method of the RenderWindow
	 */
	public void display() {
		window.display();
	}
	
	/**
	 * Method to move all views to their original position (0, 0)
	 */
	public void resetViews() {
		mainView.move((float)-viewXOffset, (float) - viewYOffset);
		lowerView.move((float)-viewXOffset, (float) - viewYOffset);
		upperView.move((float)-viewXOffset, (float) - viewYOffset);
		
		viewXOffset = 0;
		viewYOffset = 0;
	}
	
	/**
	 * Sets the views to the x, y (pixels) coordinates provided
	 * @param x - x position to set to.
	 * @param y - y position to set to.
	 */
	public void resetViewsToPosition(double x, double y) {
		mainView.move((float)(x - viewXOffset), (float)(y - viewYOffset));
		lowerView.move((float)(x - viewXOffset), (float)(y - viewYOffset));
		upperView.move((float)(x - viewXOffset), (float)(y - viewYOffset));
		
		viewXOffset = x;
		viewYOffset = y;
	}
	
	//getters and setters
	
	/**
	 * Method to get the size of the lower and upper views.
	 * @return Vector2f instance that represents the (width, height) of the lower/upper view.
	 */
	public Vector2f getSubViewSize() {
		return lowerView.getSize();
	}

	/**
	 * Method to get the RenderWindow instance of this window.
	 * @return RednerWindow instance to draw everything to.
	 */
	public RenderWindow getWindow() {
		return window;
	}

	/**
	 * Method to get the width of the window.
	 * @return width of window.
	 */
	public double getWindowWidth() {
		return windowWidth;
	}

	/**
	 * Method to get the height of the window.
	 * @return window height.
	 */
	public double getWindowHeight() {
		return windowHeight;
	}

	/**
	 * Method to get the height of the upper/lower view.
	 * @return upper/lower view height
	 */
	public double getSubViewHeight() {
		return subViewHeight;
	}

	/**
	 * Method to get the width of the upper/lower view.
	 * @return upper/lower view height.
	 */
	public double getSubViewWidth() {
		return subViewWidth;
	}

	/**
	 * Method to get the height of the main view.
	 * @return main view height.
	 */
	public double getMainViewHeight() {
		return mainViewHeight;
	}
	
	/**
	 * Method to get the current x offset of the views.
	 * @return x offset from starting position.
	 */
	public double getXOffset() {
		return viewXOffset;
	}
	
	/**
	 * Method to get the current y offset of the views.
	 * @return y offset from the starting position.
	 */
	public double getYOffset() {
		return viewYOffset;
	}
}

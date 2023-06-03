import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * A class to represent the progress bar that will be displayed during the game.
 * @author Alexander Bousbaine
 *
 */
public class ProgressBar {

	private double totalLength, currentLength, shellWid, barBuff, barWid, levelLength;
	private RectangleShape shell, bar, barBackground;
	
	private Handler handler;
	
	/**
	 * Constructor - creates the various visula objects that make up the progress bar.
	 * @param lvlLen length of the level the progress bar is being created in.
	 * @param h handler instance to gain access to handler instances.
	 */
	public ProgressBar(int lvlLen, Handler h) {
		handler = h;
		
		levelLength = lvlLen;
		shellWid = Launcher.WIN_WIDTH * 0.75;
		barBuff = 0.02 * shellWid;		
		barWid = 2 * Main.UNIT;
		
		totalLength = shellWid - 2 * barBuff;
		currentLength = 0;
		
		shell = new RectangleShape(new Vector2f((float) shellWid, (float) 4* Main.UNIT));
		shell.setFillColor(Color.BLACK);
		shell.setPosition(new Vector2f(Launcher.WIN_WIDTH / 8, (float)(Launcher.WIN_HEIGHT * 0.8) - (4*Main.UNIT)));
		
		bar = new RectangleShape(new Vector2f(0, (float) barWid));
		bar.setFillColor(Color.GREEN);
		bar.setPosition(new Vector2f((float)(Launcher.WIN_WIDTH / 8 + barBuff), (float)(Launcher.WIN_HEIGHT * 0.8) - (3*Main.UNIT)));
		
		barBackground = new RectangleShape(new Vector2f((float) totalLength, (float) barWid));
		barBackground.setFillColor(Color.WHITE);
		barBackground.setPosition(new Vector2f((float)(Launcher.WIN_WIDTH / 8 + barBuff), (float)(Launcher.WIN_HEIGHT * 0.8) - (3*Main.UNIT)));
		
	}
	
	/**
	 * Resets the progress bar to the given x position out of the level's length.
	 * This should take in the player's x position to reset the progress bar at time of death.
	 * @param xPos player's position along the level.
	 */
	public void resetToPosition(double xPos) {
		if(xPos <= levelLength && xPos >= 0) {
			currentLength = xPos / levelLength;
		}
	}
	
	/**
	 * Update method similar to that of {@link Entity#update()}.
	 * Should be called every tick to reflect the player's progress through the level.
	 */
	public void update() {
		currentLength = handler.getPlayerWorldX() / levelLength * totalLength;
		bar.setSize(new Vector2f((float)(currentLength), (float) barWid));
	}
	
	/**
	 * Render method similar to that of {@link Entity#render(RenderWindow)}
	 * @param w RenderWindow to draw objects to.
	 */
	public void render(RenderWindow w) {
		w.draw(shell);
		w.draw(barBackground);
		w.draw(bar);
	}
	
}

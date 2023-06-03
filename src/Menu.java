import org.jsfml.graphics.RenderWindow;

/**
 * A class to be a base class for all menus in the game - those being the main menu, the pause menu and the end game menu.
 * @author Alexander Bousbaine
 *
 */
public interface Menu {

	/**
	 * Method similar to {@link Entity#update()}
	 * Should be called every tick to make sure any inputs/changes to the menu is registered.
	 * Any changes to 'state' that being position vars, objects to display, etc... should happen here.
	 */
	public abstract void update();
	
	/**
	 * Method similar to {@link Entity#render(RenderWindow)}
	 * Should be called every tick to render any visual elements of the game to the screen.
	 * @param w RenderWindow to draw to.
	 */
	public abstract void render(RenderWindow w);
	
}

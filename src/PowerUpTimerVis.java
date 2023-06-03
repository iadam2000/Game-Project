import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;

/**
 * Class to visualise the amount of time left before the power-up in use runs out.
 * @author Alexander Bousbaine
 *
 */
public class PowerUpTimerVis {

	private CircleShape shell;
	private Text timerTxt;
	
	private boolean visible;
	
	/**
	 * Constructor - Creates text and shape objects that are needed to display the time.
	 * @param h handler instance to access the handler methods.
	 */
	public PowerUpTimerVis(Handler h) {
		
		visible = false;
		
		timerTxt = new Text("", h.getFontBlackChancery(), 24);
		timerTxt.setColor(Color.BLACK);
		timerTxt.setStyle(Text.BOLD);
		timerTxt.setPosition(27*Main.UNIT, (float)((Launcher.WIN_HEIGHT * 0.8) - 14*Main.UNIT));
			
		shell = new CircleShape(3*Main.UNIT);
		shell.setFillColor(new Color(187, 187, 187));
		shell.setOutlineColor(Color.BLACK);
		shell.setOutlineThickness(Main.UNIT);
		shell.setPosition(25*Main.UNIT, (float)((Launcher.WIN_HEIGHT * 0.8) - 13*Main.UNIT));
	}
	
	/**
	 * Method to set the string that will be displayed.
	 * Should always be an integer.
	 * @param s integer to display on the timer.
	 */
	public void setTimerText(String s) {
		timerTxt.setString(s);
	}

	/**
	 * Render method similar to that of {@link Entity#render(RenderWindow)}
	 * @param w RenderWindow to draw objects and text to.
	 */
	public void render(RenderWindow w) {
		if(visible) {
			w.draw(shell);
			w.draw(timerTxt);
		}
	}
	
	/**
	 * Method to set whether or not the timer should be visible.
	 * Should only be visible when a power-up is in use.
	 * @param b true if timer should be visible, otherwise false.
	 */
	public void setVisible(boolean b) {
		visible = b;
	}
}

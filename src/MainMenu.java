import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class MainMenu implements Menu{

	private Handler handler;
	private Background bg;
	private RectangleShape backing1;
	private Text play, title, reset;
	
	public MainMenu(Handler h) {
		handler = h;
		
		bg = new Background("src/Assets/Images/MMBackgroundv2.png");
		
		title = new Text("Running Out of Time", handler.getFontBlackChancery(), 48);
		title.setPosition(Launcher.WIN_WIDTH / 2 - (title.getLocalBounds().width / 2), (float) (Launcher.WIN_HEIGHT * 0.3));
		title.setColor(Color.BLACK);
		
		play = new Text("Press 'SPACE' to play!", handler.getFontBlackChancery(), 48);
		play.setPosition(Launcher.WIN_WIDTH / 2 - (play.getLocalBounds().width / 2), (float) (Launcher.WIN_HEIGHT * 0.5));
		play.setColor(Color.RED);
		
		reset = new Text("Press 'R' to Reset your Progress - This Cannot Be Undone!", handler.getFontSourceSans(), 24);
		reset.setPosition(0, (float) (Launcher.WIN_HEIGHT - 32));
		reset.setColor(Color.BLACK);
		
		backing1 = new RectangleShape(new Vector2f(play.getLocalBounds().width + 10, (float)(Launcher.WIN_HEIGHT * 0.3) + 10));
		backing1.setPosition(Launcher.WIN_WIDTH / 2 - (play.getLocalBounds().width / 2) - 5, (float) (Launcher.WIN_HEIGHT * 0.3) - 5);
		backing1.setFillColor(Color.WHITE);
		backing1.setOutlineColor(Color.BLACK);
		backing1.setOutlineThickness(5);
		
	}

	@Override
	public void update() {
		
		if(handler.spacePressed()) {
			handler.progressGameState();
			handler.unpauseGame();
		}
		
		if(handler.rPressed()) {
			handler.setSaveState(0, 0, 0, -1, 1f, 0);
		}
	}

	@Override
	public void render(RenderWindow w) {
		bg.render(w);
		w.draw(backing1);
		w.draw(play);
		w.draw(title);
		w.draw(reset);
	}

}

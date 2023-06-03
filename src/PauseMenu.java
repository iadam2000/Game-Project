import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * Class to create the pause menu in our game.
 * @author Alexander Bousbaine
 *
 */
public class PauseMenu implements Menu{

	private Handler handler;
	
	private float baseX, baseY;
	private Vector2f baseMenuDimensions, controlMenuDimensions;
	private int txtSize;
	private boolean instructions, init;
	private RectangleShape menuBox;
	private Text[] menuText;
	private Font txtFont;
	
	/**
	 * Constructor - sets up the text and other visual objects for the pause menu. 
	 * @param h handler instance to gain access to handler methods.
	 */
	public PauseMenu(Handler h) {
		handler = h;
		instructions = false;
		init = true;
		
		txtFont = h.getFontSourceSans();
		
		baseX = (float) (Launcher.WIN_WIDTH * 0.333);
		baseY = (float) (Launcher.WIN_HEIGHT * 0.125);
		txtSize = (int)Launcher.WIN_WIDTH/50;
		
		baseMenuDimensions = new Vector2f((float)Launcher.WIN_WIDTH / 3, (float)(Launcher.WIN_HEIGHT * 0.75));
		controlMenuDimensions = new Vector2f((float)Launcher.WIN_WIDTH / 2, (float)(Launcher.WIN_HEIGHT * 0.75));
		
		menuBox = new RectangleShape(baseMenuDimensions);
		menuBox.setPosition(baseX, baseY);
		menuBox.setFillColor(new Color(180, 180, 180));
		menuBox.setOutlineColor(new Color(120, 120, 120));
		menuBox.setOutlineThickness(10);
		
		menuText = new Text[20];
		menuText[0] = new Text("", txtFont, 2*txtSize);		
		menuText[0].setPosition(baseX + Launcher.WIN_WIDTH/8 - 4*Main.UNIT, baseY);
		
		for(int i = 1; i < menuText.length; i++) {
			menuText[i] = new Text("", txtFont, txtSize);		
			menuText[i].setPosition(baseX+Main.UNIT, baseY + txtSize*i);
		}
		
	}
	
	private void setupBaseMenu() {
		if(init) {
			baseX = (float) (Launcher.WIN_WIDTH * 0.333);
			baseY = (float) (Launcher.WIN_HEIGHT * 0.125);
			
			menuBox.setSize(baseMenuDimensions);
			menuBox.setPosition(baseX, baseY);
			
			for(int i = 1; i < menuText.length; i++) {
				menuText[i].setString("");
				menuText[i].setPosition(baseX+Main.UNIT, (baseY + txtSize) + txtSize*i);
				menuText[i].setColor(Color.BLACK);
	
			}
			
			menuText[0].setString("Paused");
			menuText[1].setString("ESC -> Unpause the Game");
			menuText[3].setString("ENTER -> Return to Main Menu");
			menuText[4].setString("Your Last Checkpoint will be Saved");
			menuText[6].setString("SPACE -> View Controls");
			menuText[8].setString("R -> Volume Up");
			menuText[9].setString("E -> Volume Down");
			
			if(handler.isMuted()) {
				menuText[11].setString("M -> Unmute all Sounds");
			}
			else {
				menuText[11].setString("M -> Mute all Sounds");
			}
			
			init = false;
		}
	}
	
	private void setupControlMenu() {
		if(init) {
			baseX = (float) (Launcher.WIN_WIDTH * 0.25);
			baseY = (float) (Launcher.WIN_HEIGHT * 0.125);
			
			menuBox.setSize(controlMenuDimensions);
			menuBox.setPosition(baseX, baseY);
			
			for(int i = 1; i < menuText.length; i++) {
				menuText[i].setString("");
				menuText[i].setPosition(baseX+Main.UNIT, (baseY + txtSize) + txtSize*i);
				menuText[i].setColor(Color.BLACK);
			}
			
			menuText[0].setString("Controls");
			menuText[1].setString("ESC -> Unpause the Game");
			menuText[3].setString("W/UP -> Jump");
			menuText[4].setString("S/DOWN -> Duck");
			menuText[6].setString("D/RIGHT -> Move to Upper Layer");
			menuText[7].setString("A/LEFT -> Move to Lower Layer");
			menuText[9].setString("SPACE -> Use Power Up");
			menuText[10].setString("SHIFT -> Discard Power Up");
			
			menuText[19].setString("SPACE -> Go Back to Previous Menu");
			
			init = false;
		}
	}
	
	@Override
	public void update() {
		if(instructions) {
			setupControlMenu();
		}
		else {
			setupBaseMenu();
		}
		if(handler.spacePressed()) {
			instructions = !instructions;
			init = true;
		}
		
		if(!instructions) {
			if(handler.enterPressed()) {
				handler.moveToMiddleLayer();
				handler.toMainMenu();
				instructions = false;
			}
			
			if(handler.ePressed()) {
				handler.volumeDown();
			}
			if(handler.rPressed()) {
				handler.volumeUp();
			}
			if(handler.mPressed()) {
				handler.toggleMute();
				
				if(handler.isMuted()) {
					menuText[11].setString("M -> Unmute all Sounds");
					handler.pauseMusicForLevel(handler.getCurrentLevelId());
				}
				else {
					menuText[11].setString("M -> Mute all Sounds");
					handler.playMusicForLevel(handler.getCurrentLevelId());
				}
			}
		}
	}
	
	@Override
	public void render(RenderWindow w) {
		w.draw(menuBox);
		for(Text t : menuText) {
			w.draw(t);
		}
	}
}

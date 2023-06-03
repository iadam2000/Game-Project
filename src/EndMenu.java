import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * A class to display the End Menu of the game.
 * @author Alexander Bousbaine
 *
 */
public class EndMenu implements Menu {

	private Handler handler;
	private Text[] credits;
	private Text deaths, thanks, congrats, instructions;
	private Font txtFont;
	private Background bg;
	private RectangleShape backing1, backing2, backing3;
	
	/**
	 * Constructor - loads in the background image, font and sets up other visible objects.
	 * @param h - handler instance to gain access to handler methods.
	 */
	public EndMenu(Handler h) {
		handler = h;
		
		bg = new Background("src/Assets/Images/MMBackgroundv2.png");
		
		txtFont = handler.getFontSourceSans();
		
		credits = new Text[21];
		for(int i = 0; i < credits.length; i++) {
			credits[i] = new Text("", txtFont, 24);
		}
		
		deaths = new Text("", txtFont, 24);
		congrats = new Text("", txtFont, 30);
		thanks = new Text("", txtFont, 24);
		instructions = new Text("", txtFont, 24);
		
		backing1 = new RectangleShape();
		backing2 = new RectangleShape();
		backing3 = new RectangleShape();
		
		setupCredits();
		
		//text set in init method
		deaths.setString("You died 1000 times");
		deaths.setPosition(Launcher.WIN_WIDTH / 2 + (deaths.getLocalBounds().width), (float)(Launcher.WIN_HEIGHT*0.1));
		deaths.setColor(Color.BLACK);
		
		congrats.setString("Congratulations - You Have Completed the Game!");
		congrats.setPosition(Launcher.WIN_WIDTH / 2 - (congrats.getLocalBounds().width/10), (float) (Launcher.WIN_HEIGHT * 0.4));
		congrats.setColor(Color.BLACK);
		congrats.setStyle(Text.BOLD);
		
		thanks.setString("Thank You Very Much for Playing");
		thanks.setPosition(Launcher.WIN_WIDTH / 2 + (thanks.getLocalBounds().width/3), (float) (Launcher.WIN_HEIGHT * 0.5));
		thanks.setColor(Color.BLACK);
		
		instructions.setString("Press 'SPACE' to return to the Main Menu");
		instructions.setPosition(Launcher.WIN_WIDTH / 2 + (instructions.getLocalBounds().width/5), (float) (Launcher.WIN_HEIGHT * 0.7));
		instructions.setColor(Color.RED);
		
		
		backing2.setSize(new Vector2f(deaths.getLocalBounds().width + 10, deaths.getLocalBounds().height + 10));
		backing2.setPosition(Launcher.WIN_WIDTH / 2 + (deaths.getLocalBounds().width) - 5, (float)(Launcher.WIN_HEIGHT*0.1));
		backing2.setOutlineColor(Color.GREEN);
		backing2.setOutlineThickness(3);
		
		backing3.setSize(new Vector2f(congrats.getLocalBounds().width + 20, (float) (Launcher.WIN_HEIGHT * 0.3) + 30 + 10));
		backing3.setPosition(Launcher.WIN_WIDTH / 2 - (congrats.getLocalBounds().width/10), (float) (Launcher.WIN_HEIGHT * 0.4));
		backing3.setOutlineColor(Color.BLACK);
		backing3.setOutlineThickness(5);
	}
	
	/**
	 * Method to update the text showing how many times the player died as they played.
	 * Should be called after all levels have been finished.
	 */
	public void init() {
		deaths.setString("You died "+handler.getPlayer().getNumDeaths()+" times");
		deaths.setPosition(Launcher.WIN_WIDTH / 2 + (deaths.getLocalBounds().width), (float)(Launcher.WIN_HEIGHT*0.1));
		
		if(handler.getPlayer().getNumDeaths() > 0) {
			backing2.setOutlineColor(Color.RED);
		}
		
		backing2.setSize(new Vector2f(deaths.getLocalBounds().width + 10, deaths.getLocalBounds().height + 10));
		backing2.setPosition(Launcher.WIN_WIDTH / 2 + (deaths.getLocalBounds().width) - 5, (float)(Launcher.WIN_HEIGHT*0.1));
	}
	
	private void setupCredits() {
		double base = Launcher.WIN_HEIGHT * 0.1, end = 0;
		
		credits[0].setString("MADE BY:");
		credits[1].setString("> Ibraheem Adam");
		credits[2].setString("> Marta Adamska");
		credits[3].setString("> Zubayer Ahmed");
		credits[4].setString("> Alexander Bousbaine");
		credits[5].setString("> Charlotte Brocksom");
		credits[6].setString("ACKNOWLEDGEMENTS:");
		credits[7].setString("> Level One Music");
		credits[8].setString("     - Stardust (Ziggy is coming) by Kraftamt");
		credits[9].setString("> Level Two Music ");
		credits[10].setString("     - Empty House by Mana Junkie");
		credits[11].setString("> Level Three Music ");
		credits[12].setString("     - Latinium by Javolenus");
		credits[13].setString("> Level Four Music");
		credits[14].setString("     - The Speck-tacular Flight of the Firefly by J.Lang");
		credits[15].setString("> Level Five Music");
		credits[16].setString("     - Le Graoully passe Ã la toussaint by Bluemillenium");
		credits[17].setString("> Ducking Sound");
		credits[18].setString("     - Crouch00.wav by punpcklbw");
		credits[19].setString("> Contains public sector information licensed");
		credits[20].setString("  under the Open Government Licence v3.0");

		for(int i = 0; i < credits.length; i++) {
			credits[i].setPosition((float)3*Main.UNIT, (float)(base + i*30));
			credits[i].setColor(Color.BLACK);
			end = base + (i*30);
		}
		
		backing1.setSize(new Vector2f(545, (float) (end - base + 40)));
		backing1.setPosition(3*Main.UNIT - 5, (float) (base - 5));
		backing1.setOutlineColor(Color.BLACK);
		backing1.setOutlineThickness(5);
	}

	@Override
	public void update() {
		if(handler.spacePressed()) {
			handler.progressGameState();
		}
	}

	@Override
	public void render(RenderWindow w) {
		bg.render(w);
		
		w.draw(backing1);
		w.draw(backing2);
		w.draw(backing3);
		
		for(Text t : credits) {
			w.draw(t);
		}
		w.draw(deaths);
		w.draw(thanks);
		w.draw(congrats);
		w.draw(instructions);
	}
}

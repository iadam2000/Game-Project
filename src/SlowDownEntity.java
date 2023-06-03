import org.jsfml.graphics.RenderWindow;

/**
 * A class to represent the physical pickup object for the Slow Down power-up
 * @author Alexander Bousbaine
 *
 */
public class SlowDownEntity extends Entity implements PowerUpEntity{
	
	private SlowDownPU slowDown;
	private boolean taken;
	
	/**
	 * Constructor - takes in position data to feed to Entity super class as well as an Id, and a handler instance.
	 * @param posDat position data for Entity superclass.
	 * @param i id of this entity from the getNewEntityId handler method.
	 * @param h handler instance for super class.
	 */
	public SlowDownEntity(int[] posDat, int i, Handler h) {
		super(posDat, h);
		setId(i);
		
		taken = false;
		setWidth(10*Main.UNIT);
		setHeight(10*Main.UNIT);
		
		//loadTextureIntoSprite("src/Assets/Images/SlowDownTest.png");
		loadTextureIntoSprite("src/Assets/Images/slowSpeed.png");
		updateSkinPos();
		
		slowDown = new SlowDownPU(h);
	}

	@Override
	public void update() {}

	@Override
	public void render(RenderWindow w) {
		if(!taken) {
			w.draw(getSkin());
		}
	}
	
	@Override
    public String getType() {
        return "power up";
    }
	
	@Override
	public SlowDownPU getPowerUp() {
		return slowDown;
	}
	
	@Override
	public void take() {
		taken = true;
	}
	
	@Override
	public void place() {
		taken = false;
	}
	
	@Override
	public boolean isTaken() {
		return taken;
	}
}

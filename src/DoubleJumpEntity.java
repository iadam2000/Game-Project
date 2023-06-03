import org.jsfml.graphics.RenderWindow;

/**
 * A class to represent the physical pick-up object for the Double Jump power-up.
 * @author Alexander Bousbaine
 *
 */
public class DoubleJumpEntity extends Entity implements PowerUpEntity{
	
	private DoubleJumpPU doubleJump;
	private boolean taken;
	
	/**
	 * Constructor - sets the position and loads in the image for this entity.
	 * @param posDat - position data for the entity.
	 * @param id - id for this entity, gotten from getNewEntityId() method.
	 * @param h - handler instance for superclass.
	 */
	public DoubleJumpEntity(int[] posDat, int id, Handler h) {
		super(posDat, h);
		setId(id);
		
		taken = false;
		
		setWidth(10*Main.UNIT);
		setHeight(10*Main.UNIT);
		
		loadTextureIntoSprite("src/Assets/Images/jumpPower.png");
		updateSkinPos();
		
		doubleJump = new DoubleJumpPU(h);
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
	public DoubleJumpPU getPowerUp() {
		return doubleJump;
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

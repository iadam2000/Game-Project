/**
 * A class to represent the functionality of the Slow Down power-up
 * @author Alexander Bousbaine
 *
 */
public class SlowDownPU extends PowerUp{

	private Handler handler;
	
	/**
	 * Constructor - sets up handler instance and calls super class constructor
	 * @param h
	 */
	public SlowDownPU(Handler h) {
		super();
		handler = h;
	}
	
	@Override
	public void forceEnd() {
		handler.resetLevelSpeed();
		handler.resetJumpHeight();
		handler.setTimerText("");
	}
	
	@Override
	public PowerUp deleteIfFinished() {
		if(isActive()) {
			if(isFinished()) {
				handler.resetLevelSpeed();
				handler.resetJumpHeight();
				handler.setTimerText("");
				return null;
			}
		}
		return this;
	}

	@Override
	public void activate() {
		init();
		handler.slowDownLevelSpeed();
		handler.getPlayer().slowDownJump();
		
	}
}

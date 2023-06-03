/**
 * A class to represent the functionality of the Double Jump power-up.
 * @author Alexander Bousbaine
 *
 */
public class DoubleJumpPU extends PowerUp{

	private Handler handler;
	
	/**
	 * Constructor - lets methods access the handler.
	 * @param h - handler instance for access to handler methods.
	 */
	public DoubleJumpPU(Handler h) {
		super();
		handler = h;
	}
	
	@Override
	public void forceEnd() {
		handler.resetJumpHeight();
		handler.setTimerText("");
	}
	
	@Override
	public PowerUp deleteIfFinished() {
		if(isActive()) {
			if(isFinished()) {
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
		handler.doubleJumpHeight();
	}
}

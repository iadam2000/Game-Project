import org.jsfml.system.Clock;

/**
 * A class to act as a blueprint to power ups
 * @author Alexander Bousbaine
 *
 */
public abstract class PowerUp{
	
	private final double initialDuration = 5;
	private double duration;
	private boolean active;
	private Clock timer;
	
	/**
	 * Constructor - sets active to false, meaning that the power-up should not be in use when a child of this object is created.
	 */
	public PowerUp() {
		active = false;
	}
	
	/**
	 * Method to activate the power-up and let it take effect.
	 */
	public abstract void activate();
	
	/**
	 * Method to delete the current power-up if it's timer finishes.
	 * Such functionality described here should be implemented in a child class along side any intended effects that occur once the power-up finishes.
	 * @return This Power-Up instance if timer hasn't finished, otherwise null.
	 */
	public abstract PowerUp deleteIfFinished();
	
	/**
	 * Method to forcefully end the power-up.
	 * When implemented, should cease whatever effect the power-up was providing to the player.
	 */
	public abstract void forceEnd();
	
	/**
	 * Method to set the 'active' boolean to true and starts timer.
	 * Should be used in the implementation of the activate function.
	 */
	public void init() {
		duration = initialDuration;
		active = true;
		timer = new Clock();
	}
	
	/**
	 * Method to check whether the power-up has run for as long as specified.
	 * Will change 'active' boolean to false if returns true.
	 * @return - true if has run for as long as duration specified, else false.
	 */
	public boolean isFinished() {
		if(timer.getElapsedTime().asSeconds() >= duration) {
			active = false;
			return true;
		}
		return false;
	}

	/**
	 * Method to return how long the power-up should run for.
	 * @return - the amount of time (sec) left on the timer.
	 */
	public double getTimeLeft() {
		return duration - timer.getElapsedTime().asSeconds();
	}
	
	/**
	 * Method to get the duration of the power-up.
	 * @return duration of the power-up.
	 */
	public double getDuration() {
		return duration;
	}
	
	/**
	 * Method to get whether the power-up is in use or not.
	 * @return true if in use, else false.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Method to pause a power-up's timer.
	 */
	public void pauseTimer() {
		duration = getTimeLeft();
		timer = null;
	}
	
	/**
	 * Method to unpause a power-up's timer.
	 */
	public void unpauseTimer() {
		timer = new Clock();
	}
}

/**
 * An interface to allow PowerUps to be grouped and to have common methods.
 * @author Alexander Bousbaine
 *
 */
public interface PowerUpEntity {
	
	public abstract PowerUp getPowerUp();
	public abstract void take();
	public abstract void place();
	public abstract boolean isTaken();
}

import org.jsfml.graphics.RenderWindow;

/**
 * An abstract class to group levels and cutscenes.
 */

public abstract class GameStage {

    // ID - integer to identify the stage
    private int ID;

    // finished - is the stage finished?
    private boolean completed;

    /**
     * Constructor method
     * @param ID - number to identify a stage (for more info look in LevelManager -M)
     */
    public GameStage(int ID) {
        this.ID = ID;
        completed = false;
    }

    public abstract void render(RenderWindow w);
    public abstract void update();

    // Getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean finished) {
        this.completed = finished;
    }
}

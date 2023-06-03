import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * This class represents a cutscene.
 * One should finish before a level starts.
 * @author Marta Adamska, Charlotte Brocksom, Alexander Bousbaine
 */
public class Cutscene extends GameStage {

    private Handler handler;

    private final String PASSWORD = "NEVER GONNA GIVE YOU UP";

    // A list of all dialogues in the cutscene
    private ArrayList<String> dialogue;
    private int len;

    // Text to display on the screen for the current dialogue
    private Text currentDialogue, next, skip, playerInput, inputFeedback;
    private int count;


    private RectangleShape txtBox;
    //private RectangleShape background;
    private Background background;
    private Sprite player, sideCharacter, tutorial;
    private Texture playerTex, sideTexture, tutorialTex;
    private boolean isSideCharacter;

    private boolean passwordSuccess, attemptSet;
    /**
     * Constructor method for Cutscene
     * @param cutsceneID - the number we use to identify the current cutscene.
     * @param h - handler instance to gain access to handler methods.
     */
    public Cutscene(int cutsceneID, Handler h) {
        super(cutsceneID);
        //System.out.println("Cutscene: "+cutsceneID);
        inputFeedback = new Text();
        inputFeedback.setPosition(300, 300);
        inputFeedback.setColor(Color.RED);
        this.handler = h;

        dialogue = new ArrayList<>();
        dialogue = fileToList("src/Assets/Cutscenes/Cutscene" + this.getID() + ".txt");
        len = dialogue.size();
        
        passwordSuccess = attemptSet = false;

        this.addComponents();

        count = 0;
        sideTexture = new Texture();

        // We have player input in the cutscene #6
        if (this.getID() == 6) {
            playerInput = new Text("", handler.getFontOrangeKid(), 24);
            playerInput.setColor(Color.RED);
            playerInput.setPosition((float) Main.UNIT * 70, (float) (Launcher.WIN_HEIGHT * 0.15));
        }
    }

    @Override
    public void render(RenderWindow w) {
        //w.draw(bg);
        background.render(w);
        w.draw(txtBox);
        w.draw(player);
        if(currentDialogue != null) {
        	w.draw(currentDialogue);
        }

        w.draw(next);
        if (isSideCharacter) {
            w.draw(sideCharacter);
        }
        if (this.getID() == 6 && count == 7) {
            w.draw(playerInput);
            w.draw(inputFeedback);
        }
        if (this.getID() != 7) {
            w.draw(skip); // you can't skip the cutscene with password input
        }

        if(this.getID() == 1 && count < 10){
            this.drawTutorial(count, w);
        }
    }

    // WATCH OUT! I found out how to make Enter key work! (it's called RETURN on jsflm)
    // Also it works on enter released so you can't "skip" everything by holding enter down
    @Override
    public void update() {
        // We need to check if there are any side characters to put on the screen
        isSideCharacter = false;

        if (this.getID() == 1) {
            this.loadSideCharacter("queen", (float) (Launcher.WIN_WIDTH) - Main.UNIT * 110, (float) (Launcher.WIN_HEIGHT * 0.40));
            isSideCharacter = true;
        }
        if (this.getID() == 3 && count < 14) {
            this.loadSideCharacter("queenVic", (float) (Launcher.WIN_WIDTH) - Main.UNIT * 110, (float) (Launcher.WIN_HEIGHT * 0.40));
            isSideCharacter = true;
        }
        if (this.getID() == 4 && count < 11) {
            this.loadSideCharacter("JuliusCaesar", (float) (Launcher.WIN_WIDTH) - Main.UNIT * 110, (float) (Launcher.WIN_HEIGHT * 0.40));
            isSideCharacter = true;
        }
        if (this.getID() == 5 && count > 8 && count < 13) {
            this.loadSideCharacter("dino", (float) (Launcher.WIN_WIDTH) - Main.UNIT * 110, (float) (Launcher.WIN_HEIGHT * 0.40));
            isSideCharacter = true;
        }
        if ((this.getID() == 4 && count >= 11 && count < 21) ||
                (this.getID() == 5 && count >= 1 && count < 9) ||
                (this.getID() == 71 && count >= 1 && count < 9) ||
                (this.getID() == 72 && count >=1 && count < 9)) {
            this.loadSideCharacter("evilMan", (float) (Launcher.WIN_WIDTH) - Main.UNIT * 110, (float) (Launcher.WIN_HEIGHT * 0.40));
            isSideCharacter = true;
        }
        //if(this.getID() == 72 && count >= 10 && count < 17)
       
        //what's this here for? ^
        
        if(getID() >= 6) {
    		skip.setString("");
        }      
        
        if(count == len - 1) {
        	if(getID() < 6) {
        		skip.setString("");
	        	next.setString("START LEVEL\n[ ENTER ]");
	        	next.setColor(Color.RED);
        	}
        	if(getID() == 6) {
        		skip.setString("");
        		
        		next.setString("CONFIRM\n[ ENTER ]");
	        	next.setColor(Color.RED);    
        	}
        }
        
        if(getID() == 71 && count >= len - 1) {
        	if(handler.enterPressed() && handler.enterReleased()) {
        		handler.progressGameState();
        	}
        }
        if(getID() == 72 && count >= len - 1) {
        	if(handler.enterPressed() && handler.enterReleased()) {
        		handler.progressGameState();
        	}
        }

        // At the end of the game in cutscene #6 player has to type in a password and press enter:
        // If the password is correct it goes to cutscene 7.1 (id == 71)
        // If the password is not correct the cutscene changes to 7.2 (id == 72)
        if (this.getID() == 6 && count == len - 1) {
            playerInput.setString(handler.txtInput().toUpperCase());
            if (handler.enterPressed() && handler.enterReleased() && handler.txtInput().replaceAll("\\s","").length() > 0){
            	attemptSet = true;
                passwordSuccess = checkMatch(handler.txtInput());
            }
        }
        //stops from skipping through and also stops from buffering a release to skip first part of cutscene
        else if (handler.enterPressed() && (handler.enterReleased() || count < 1)) {
        	if(getID() == 6 && count == len - 2) {
        		handler.resetTextInput();
        	}
            if (count < len - 1) {
                count++;
                currentDialogue.setString(dialogue.get(count));
            } else {
                this.setCompleted(true);
            }
        }
    }
    
    /**
     * Checks whether the player's inputed password matches the set password.
     * @param input - the password the player entered.
     * @return - true if they match, otherwise false.
     */
    private boolean checkMatch(String input) {
    	String in = input.toUpperCase().replaceAll("\\s","");
    	String target = PASSWORD.toUpperCase().replaceAll("\\s","");
    	
    	return in.equals(target);
    }

    /**
     * This class reads a .txt file containing dialogues. Each line contains one statement(?)
     * Because using "\n" in text doesn't work I put "---" every time we want to display a new line
     * and then using regex I replace it with new line :) -M
     * @param pathToFile path to file that contains the dialogue
     */
    private ArrayList<String> fileToList(String pathToFile) {
        ArrayList<String> dialList = new ArrayList<>();

        try{
            File f = new File(pathToFile);
            Scanner read = new Scanner(f);

            while(read.hasNextLine()) {
                dialList.add(read.nextLine().replaceAll("---", "\n"));
            }

            read.close();
        } catch (FileNotFoundException e){
            System.out.println("Oh my, something wrong with dialogue files!");
            e.printStackTrace();
        }
        return dialList;
    }

    /**
     * Method used to load an image file to display as a side character.
     * @param characterName the name of the cutscene buddy
     */
    private void loadSideCharacter(String characterName, float xPos, float yPos) {
        sideTexture = new Texture();
        try {
            sideTexture.loadFromFile(Paths.get("src/Assets/Characters/" + characterName + ".png"));
            sideTexture.setSmooth(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector2f texSize = new Vector2f(sideTexture.getSize());
        Vector2f target = new Vector2f(40 * Main.UNIT, 80 * Main.UNIT);
        Vector2f scale = Vector2f.componentwiseDiv(target, texSize);

        sideCharacter = new Sprite(sideTexture);
        sideCharacter.setScale(scale);
        sideCharacter.setPosition(xPos, yPos);
    }

    private void drawTutorial(int slideNum, RenderWindow w) {
        tutorialTex = new Texture();
        try {
            tutorialTex.loadFromFile(Paths.get("src/Assets/Tutorial/Slide" + slideNum + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tutorial = new Sprite(tutorialTex);
        tutorial.setPosition(0, 0);
        w.draw(tutorial);
    }
    
    /**
     * Method to declutter the constructor and that adds all the fixed components of the cutscene:
     * - player
     * - dialogue window and first line of the dialogue
     * - NEXT and SKIP
     */
    private void addComponents() {
        if (dialogue.size() > 0) {
            currentDialogue = new Text(dialogue.get(count), handler.getFontOrangeKid(), 22);
            currentDialogue.setColor(Color.BLACK);
            currentDialogue.setPosition(Main.UNIT * 15, (float) (Launcher.WIN_HEIGHT * 0.15));
        }

        txtBox = new RectangleShape(new Vector2f((float)(Launcher.WIN_WIDTH) - Main.UNIT * 20, (float)(Launcher.WIN_HEIGHT * 0.25)));
        txtBox.setPosition(Main.UNIT * 10, (float)(Launcher.WIN_HEIGHT * 0.1));
        txtBox.setFillColor(Color.WHITE);

        background = new Background();

        skip = new Text("SKIP\n[ ESCAPE ]", handler.getFontOrangeKid(), 22);
        skip.setColor(Color.BLACK);
        skip.setPosition((float)(Launcher.WIN_WIDTH) - Main.UNIT * 32, Main.UNIT * 15);

        next = new Text("NEXT\n[ ENTER ]", handler.getFontOrangeKid(), 22);
        next.setColor(Color.BLACK);
        next.setPosition((float)(Launcher.WIN_WIDTH) - Main.UNIT * 32, Main.UNIT * 40);

        playerTex = new Texture();
        try {
            playerTex.loadFromFile(Paths.get("src/Assets/Characters/standing man.png"));
            playerTex.setSmooth(true);
        }
        catch(IOException ex) {
            System.out.println("Trouble loading cutscene texture.");
            ex.printStackTrace();
        }
        Vector2f texSize = new Vector2f(playerTex.getSize());
        Vector2f target = new Vector2f(40 * Main.UNIT, 80 * Main.UNIT);
        Vector2f scale = Vector2f.componentwiseDiv(target, texSize);

        player = new Sprite(playerTex);
        player.setScale(scale);
        player.setPosition((float)(Launcher.WIN_WIDTH) - Main.UNIT * 220, (float)(Launcher.WIN_HEIGHT * 0.40));
    }

    /**
     * Method to get whether the played entered the password correctly.
     * @return true if correct password entered, otherwise false.
     */
    public boolean isPasswordSuccess() {
        return passwordSuccess;
    }
    
    /**
     * Method to get whether the player has entered any password, regardless of whether it is correct or not.
     * @return true if attempt made, else false.
     */
    public boolean isPasswordSet() {
        return attemptSet;
    }
}

import org.jsfml.window.Keyboard;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.TextEvent;

/**
 * Class to enable keyboard inputs
 * @author Alexander Bousbaine, Zubayer Ahmed
 */
public class KeyManager{
	
	private boolean any, up, down, left, right, rightToggle,
			leftToggle, space, spaceToggle, shift, escape, escapeToggle,
			enter, enterToggle, enterReleased, enterToggleRel, g, gToggle,e,eToggle,r,rToggle, m, mToggle;
	
	private String input = "";

	public void handleKeyPress(KeyEvent event) {
		any = true;
		
		if(event.key == Keyboard.Key.W || event.key == Keyboard.Key.UP) {
			up = true;
		}
		if(event.key == Keyboard.Key.A || event.key == Keyboard.Key.LEFT) {
			left = true;
			leftToggle = true;
		}
		if(event.key == Keyboard.Key.S || event.key == Keyboard.Key.DOWN) {
			down = true;
		}
		if(event.key == Keyboard.Key.D || event.key == Keyboard.Key.RIGHT) {
			right = true;
			rightToggle = true;
		}	
		if(event.key == Keyboard.Key.SPACE) {
			space = true;
			spaceToggle = true;
		}	
		if(event.key == Keyboard.Key.ESCAPE) {
			escape = true;
			escapeToggle = true;
		}
		if(event.key == Keyboard.Key.LSHIFT) {
			shift = true;
		}
		if(event.key == Keyboard.Key.RSHIFT) {
			shift = true;
		}
		if(event.key == Keyboard.Key.RETURN) {
			enter = true;
			enterToggle = true;
		}
		if(event.key == Keyboard.Key.G) {
			g = true;
			gToggle = true;
		}
		if(event.key == Keyboard.Key.E) {
			e = true;
			eToggle=true;
		}
		if(event.key == Keyboard.Key.R) {
			r = true;
			rToggle=true;
		}
		if(event.key == Keyboard.Key.M) {
			m = true;
			mToggle=true;
		}
	}

	public void handleTextEntered(TextEvent event) {
		if(event.asTextEvent().unicode < 128 && event.asTextEvent().unicode > 31) {
			input += ((char)event.asTextEvent().unicode);
		}
		else if(input.length() > 0 && event.asTextEvent().unicode == '\b') {
			input = input.substring(0, input.length() - 1);
		}
	}

	public String enteredText() {
		return input;
	}
	
	public void resetInput() {
		input = "";
	}

	public void handleKeyRelease(KeyEvent event) {
		any = false;
		
		if(event.key == Keyboard.Key.W || event.key == Keyboard.Key.UP) {
			up = false;
		}
		if(event.key == Keyboard.Key.A || event.key == Keyboard.Key.LEFT) {
			left = false;
		}
		if(event.key == Keyboard.Key.S || event.key == Keyboard.Key.DOWN) {
			down = false;
		}
		if(event.key == Keyboard.Key.D || event.key == Keyboard.Key.RIGHT) {
			right = false;
		}
		if(event.key == Keyboard.Key.SPACE) {
			space = false;
		}
		if(event.key == Keyboard.Key.ESCAPE) {
			escape = false;
		}
		if(event.key == Keyboard.Key.LSHIFT) {
			shift = false;
		}
		if(event.key == Keyboard.Key.RSHIFT) {
			shift = false;
		}
		if(event.key == Keyboard.Key.RETURN) {
			enter = false;
			enterReleased = true;
			enterToggleRel = true;
		}
		if(event.key == Keyboard.Key.G) {
			g = false;
		}
		if(event.key == Keyboard.Key.E) {
			e = false;
		}
		if(event.key == Keyboard.Key.R) {
			r = false;
		}
		if(event.key == Keyboard.Key.M) {
			m = false;
		}
	}
	
	public boolean anyKeyPressed() {
		up = down = left = right = false;
		
		return any;
	}
	
	public boolean isUpPressed() {
		return up;
	}

	public boolean isDownPressed() {
		return down;
	}

	public boolean isLeftPressed() {
		boolean ret = (left && leftToggle);
		
		if(leftToggle) {
			leftToggle = !leftToggle;
		}
		
		return ret;
	}

	public boolean isRightPressed() {
		boolean ret = (right && rightToggle);
		
		if(rightToggle) {
			rightToggle = !rightToggle;
		}
		
		return ret;	
	}

	public boolean isSpacePressed(){
		boolean ret = (space && spaceToggle);
		
		if(spaceToggle) {
			spaceToggle = !spaceToggle;
		}
		
		return ret;	
	}
	
	public boolean isEscapePressed() {
		boolean ret = (escape && escapeToggle);

		if(escapeToggle) {
			escapeToggle = false;
		}

		return ret;
	}
	
	/**
	 * Method to get whether the shift key is pressed
	 * @return true if pressed, else false
	 */
	public boolean isShiftPressed() {
		return shift;
	}

	public boolean isEnterPressed() {
		boolean ret = enter && enterToggle;
		if(enterToggle) {
			enterToggle = false;
		}
		return ret;
	}

	public boolean isEnterReleased() {
		boolean ret = enterReleased && enterToggleRel;
		if (enterToggleRel) {
			enterToggleRel = false;
		}
		return ret;
	}
	
	public boolean isGPressed() {
		boolean ret = (g && gToggle);
		
		if(gToggle) {
			gToggle = !gToggle;
		}
		
		return ret;
	}
	
	public boolean isEPressed() {
		boolean ret = (e && eToggle);

		if(eToggle) {
			eToggle = !eToggle;
		}

		return ret;
	}
	
	public boolean isRPressed() {
		boolean ret = (r && rToggle);

		if(rToggle) {
			rToggle = !rToggle;
		}

		return ret;
	}
	
	public boolean isMPressed() {
		boolean ret = (m && mToggle);

		if(mToggle) {
			mToggle = !mToggle;
		}

		return ret;
	}
}

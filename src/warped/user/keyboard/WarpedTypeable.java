package warped.user.keyboard;

import java.awt.event.KeyEvent;

public interface WarpedTypeable {

	/*Any class that implements this interface can receive key input from the keyboard.
	 * Call startReceiving() and the class will get a keyTyped() call every time a key is released on the keyboard.
	 * Call stopReceiving to stop keyboard input to the class.
	 * */
	
	/**Keyboard press event, only occurs if receiving.
	 * @param e - the KeyEvent from the keyboard.
	 * @author 5som3 */
	public void keyPressed(KeyEvent e);
	
	/**Keyboard released event, only occurs if receiving.
	 * @param e - the KeyEvent from the keyboard.
	 * @author 5som3 */
	public void keyReleased(KeyEvent e);
		
	/**set if the typeable will receive KeyEvents from the WarpedKeyboard.
	 * @param isRecieive - if true the typeable will receive KeyEvents through the keyPressed() and keyReleased() methods.
	 * @author 5som3*/
	public default void setReceiving(boolean isReceiving) {
		if(isReceiving) WarpedKeyboard.addTypeable(this);
		else WarpedKeyboard.removeTypeable(this);
	}
	
	/**Is the typeable currently receiving KeyEvents from the WarpedKeyboard.
	 * @return boolean - true if the typeable is receiving events else false.
	 * @author 5som3*/
	public default boolean isReceiving() {return WarpedKeyboard.contains(this);}
	
	
}

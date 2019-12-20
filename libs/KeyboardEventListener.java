package libs;

import java.awt.event.KeyEvent;

/**
 * Keyboard event listener
 * 
 * @author williamhooper 
 */

public interface KeyboardEventListener
{
    /**
     * Receive a keyboard event
     * 
     * @param ke
     */
    public void keyboardEvent( KeyEvent ke );
}

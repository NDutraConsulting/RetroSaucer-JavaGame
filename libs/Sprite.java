package libs;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Sprite class
 * 
 * @author williamhooper 
 */

public interface Sprite
{
    /**
     * Determine if the passed Sprite object collided with this object.
     * 
     * @param obj
     */
    public abstract void checkCollision( Sprite obj );

    /**
     * Draw method
     * 
     * @param g
     */
    public abstract void draw( Graphics2D g );

    /**
     * Check to see if the passed bounding box intersects with our bounding box. Returns a new Rectangle that represents the
     * intersection of the two rectangles. If the two rectangles do not intersect, the result will be an empty rectangle.
     * 
     * 
     * @param boundingBox
     */
    public abstract Rectangle intersects( Rectangle boundingBox );

    /**
     * Receive a keyboard event.
     * 
     * @param ke
     */
    public abstract void keyboardEvent( KeyEvent ke );

    /**
     * Receive a mouse event.
     * 
     * @param me
     */
    public abstract void mouseEvent( MouseEvent me );

    /**
     * Update the sprite's state.
     * 
     */
    public abstract void update();

    public abstract double getX();

    public abstract double getY();

    
    
    public abstract int getWidth();
    public abstract int getHeight();

	
    
    
  


}

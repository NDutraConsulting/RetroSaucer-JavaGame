package saucerGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameDisplay;
import libs.Sprite;

/**
 * The score sprite. This sprite displays the score in the upper left corner of the display
 * 
 * @author williamhooper
 * 
 */
public class ScoreSprite implements Sprite
{
    private int score;
    private Rectangle displayBounds;

    /**
     * Constructor
     * 
     */
    public ScoreSprite( )
    {
        displayBounds = GameDisplay.getBounds();
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics2D g )
    {
        Font f = new Font( "Times New Roman", Font.BOLD, 24 );
        g.setFont( f );
        g.setColor( Color.RED );
        g.drawString( "Score " + score, displayBounds.x +30, displayBounds.y + 28 );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void keyboardEvent( KeyEvent ke )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEvent( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void update()
    {
        // TODO Auto-generated method stub

    }

    /**
     * Add points to the score
     * 
     * @param value
     */
    public void add( int value )
    {
        score += value;
    }

    @Override
    public double getX()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getY()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getWidth()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

	public void set(int life) {
		// TODO Auto-generated method stub
		score = life;
	}

	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}

}

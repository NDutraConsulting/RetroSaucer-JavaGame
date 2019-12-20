package saucerGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.GameDisplay;
import libs.GameEvent;
import libs.ImageUtil;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

/**
 * The player sprite. This is the sprite the player controlls
 * 
 * @author williamhooper
 * 
 */
public class DestroyedPlayerSprite implements Sprite
{
    private double xPos;
    private double yPos;
    private double width;
    private double heigth;
    private Rectangle playerShape;
    private Rectangle displayBounds;
   
    private BufferedImage player1Bmp;
   
    private long lastTime;
  

    /**
     * Constructor
     * 
     */
    public DestroyedPlayerSprite( )
    {
        displayBounds = GameDisplay.getBounds();

        xPos = displayBounds.getWidth() / 2;
        yPos = displayBounds.getHeight() - 48;

        width = 32;
        heigth = 32;
        playerShape = new Rectangle( ( int ) xPos, ( int ) yPos, ( int ) width, ( int ) heigth );

       
    }

    public DestroyedPlayerSprite( double x, double y )
    {
        displayBounds = GameDisplay.getBounds();

        lastTime = System.currentTimeMillis();

        try
        {

            player1Bmp = ImageUtil.loadBufferedImage( this, "images/toolbox.png" );
            //player1Bmp1 = ImageUtil.loadBufferedImage( this, "images/tank1.png" );

        }
        catch ( IOException e )
        {
            e.printStackTrace();
            // this should quit - release mode -
        }

        width = ( player1Bmp.getWidth() - 20 );
        heigth = ( player1Bmp.getHeight() );
        xPos = x;
        yPos = y;
        playerShape = new Rectangle( ( int ) xPos, ( int ) yPos, ( int ) width, ( int ) heigth );



    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics2D g )
    {

        if ( player1Bmp == null )
        {
            playerShape.x = ( int ) xPos;
            playerShape.y = ( int ) yPos;
            g.setColor( Color.BLUE );
            g.fill( playerShape );
        }
        else
        {
            playerShape.x = ( int ) xPos;
            playerShape.y = ( int ) yPos;

            
                g.drawImage( player1Bmp, null, ( int ) xPos, ( int ) yPos );
           
        }
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return ( playerShape.intersects( boundingBox ) ? playerShape : null );
    }

    @Override
    public void keyboardEvent( KeyEvent ke )
    {
        // TODO Auto-generated method stub
        /*
         * if ( ke.getID() == KeyEvent.KEY_PRESSED ) {
         * 
         * if ( ke.getID() == KeyEvent.VK_LEFT ) { xPos = xPos - 10;
         * 
         * if ( changeFrame ) { changeFrame = false; } else { changeFrame =
         * true; } }
         * 
         * else if ( ke.getID() == KeyEvent.VK_RIGHT ) { xPos = xPos + 20;
         * 
         * if ( changeFrame ) { changeFrame = false; } else { changeFrame =
         * true; } }
         * 
         * }
         */
    }

    @Override
    public void mouseEvent( MouseEvent me )
    {
        // lastXpos = xPos;
        // lastYpos = yPos;
/*
        xPos = me.getX();
        yPos = me.getY();

        if ( me.getButton() == MouseEvent.BUTTON1 && missileTime < System.currentTimeMillis() )
        {
            PlayerMissile missile = new PlayerMissile();
            if ( missile != null )
            {
                missile.setxPos( xPos + ( playerShape.width / 2 ) );
                missile.setyPos( yPos );
                GameEventDispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddLast, missile ) );
                missileTime = System.currentTimeMillis() + 250;

            }

        }

        if ( me.getID() == MouseEvent.MOUSE_MOVED )
        {

            if ( lastTime < System.currentTimeMillis() )
            {
                changeFrame = true;
                lastTime = System.currentTimeMillis() + 100;
            }
            else
            {
                changeFrame = false;
            }

        }
*/
    }

    @Override
    public void update()
    {
        if (lastTime+800<System.currentTimeMillis()){
            //changeFrame = false;
            
            GameEventDispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
        }

        /**
         * Check for left/right wall collisions
         */
        if ( ( xPos + width ) >= displayBounds.width )
        {
            xPos = displayBounds.width - width;
        }
        if ( xPos <= 0 )
        {
            xPos = 0;
        }

        /**
         * The player can only go up so far
         */
        if ( ( yPos + heigth ) >= displayBounds.height )
        {
            yPos = displayBounds.height - heigth;
        }
        if ( yPos <= ( 3 * ( displayBounds.height / 4 ) ) )
        {
            yPos = ( 3 * ( displayBounds.height / 4 ) );
        }

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


    public void revVel()
    {
        // TODO Auto-generated method stub
        
    }

}

package saucerGame;

import libs.Sprite;
import libs.SpritePoolObject;



public abstract class MobileSprite extends SpritePoolObject implements Sprite
{
    
    private static int playerXpos, playerYpos;
    protected static boolean findPlayer = false;
   
    protected Direction myDirection;
    
    
  
    
    public Direction getDirection(){
        return myDirection;
    }
    
    protected int getPlayerXPos(){
        return playerXpos;
    }

    protected int getPlayerYPos(){
        return playerYpos;
    }

    
    protected void setPlayerXPos(int x){
        playerXpos = x;
    }

    protected void setPlayerYPos(int y){
        playerYpos = y;
    }
    public abstract void setX(double x);

    public abstract void setY(double y);

    public abstract void setXVel(double xVel);

    public abstract void setYVel(double yVel);
    public abstract void setPosition(double x, double y);

    public abstract void setVelocity(double vel);

	public abstract void takeDamage(int damage);
}

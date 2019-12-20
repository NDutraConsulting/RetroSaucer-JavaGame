package saucerGame;

import libs.SpritePool;
import libs.SpritePoolObject;


public class PlayerMissilePool extends SpritePool
{

    public PlayerMissilePool(){
        this.setPoolSize( 15 );
        this.fillSpritePool();
      
    }
   
    @Override
    protected SpritePoolObject create()
    {
        // TODO Auto-generated method stub
        return new PlayerMissile(this);
    }

    
    
}

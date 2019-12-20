package saucerGame;

import libs.SpritePool;
import libs.SpritePoolObject;

public class EnemyMissilePool extends SpritePool
{





    public EnemyMissilePool(){
    
        this.setPoolSize( 70 );
        this.fillSpritePool();
        
    }
   
    @Override
    protected SpritePoolObject create()
    {
        // TODO Auto-generated method stub
        return new EnemyMissile();
    }

}

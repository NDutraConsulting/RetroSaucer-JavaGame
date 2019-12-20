package saucerGame;

import libs.SpritePool;
import libs.SpritePoolObject;


public class EnemySpritePool extends SpritePool
{

    
	private EnemyMissilePool MissilePool;

	public EnemySpritePool(EnemyMissilePool missile){
		this.MissilePool = missile;
        this.setPoolSize( 15 );
        this.fillSpritePool();
      
    }
   
    public EnemySpritePool() {
    	 this.setPoolSize( 60 );
         this.fillSpritePool();
      
	}

	@Override
    protected SpritePoolObject create()
    {
        // TODO Auto-generated method stub
        return new EnemySprite(MissilePool);
    }

}

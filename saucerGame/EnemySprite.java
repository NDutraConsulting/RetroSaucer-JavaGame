package saucerGame;


import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Random;

import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;
import libs.GameEvent.GameEventType;

/**
 * Enemy sprite
 * 
 * This sprite bounces around the screen randomly dispatching enemy missile sprites with 
 * the projection angle of the players location this is adjusted +/- 30 degrees to increase playability
 * 
 * @author williamhooper $Id: EnemySprite.java,v 1.4 2011/08/29 00:22:43
 *         williamhooper Exp $
 */
public class EnemySprite extends MobileSprite {
	private static boolean isAttacking;
	private double xPos;
	private double yPos;

	private Rectangle enemyShape;

	private Random random;

	private long nextTime;

	private static BufferedImage enemyBmp, enemyBmp1;

	private int width;
	private int height;

	private boolean changeFrame;// used to switch between frames

	private static final long NextMissileTime = 500;
//private AudioSample missleSound;
	private int life = 20;
	private int moveCountY = 0;
	private int moveCountX = 0;

	private double yVel = 4;
	private double xVel = 4;
	private Rectangle bounds;
	private int damage = 10;

	private int myLevel =1;
	
	private EnemyMissilePool missilePool;

	public EnemySprite(int x, int y, int life) {

		bounds = GameDisplay.getBounds();

		enemyShape = new Rectangle(x, y, 20, 20);
		//this.color = Color.red;

		this.life = 20;

		xPos = x;
		yPos = y;

		random = new Random();
		nextTime = System.currentTimeMillis() + NextMissileTime;
	}

	public EnemySprite(EnemyMissilePool mPool) {
		try {
			if (enemyBmp == null) {
				enemyBmp = ImageUtil.loadBufferedImage(this,
						"images/saucerred.png");
			}
			if (enemyBmp1 == null) {
				enemyBmp1 = ImageUtil.loadBufferedImage(this,
						"images/saucerred.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}
		missilePool = mPool;
		bounds = GameDisplay.getBounds();

		enemyShape = new Rectangle(1, 1, 50, 50);

		this.life = 20;

		xPos = 1;
		yPos = 1;

		random = new Random();
		nextTime = System.currentTimeMillis() + NextMissileTime;
	}

	@Override
	public void draw(Graphics2D g) {
		enemyShape.x = (int) xPos;
		enemyShape.y = (int) yPos;
	
		if (changeFrame) {
			g.drawImage(enemyBmp, enemyShape.x, enemyShape.y, null);
		} else {
			g.drawImage(enemyBmp1, enemyShape.x, enemyShape.y, null);
		}
	}

	@Override
	public void update() {

		if(findPlayer){ // if the player has pressed the m_key this static variable in mobile sprite is set to false
			findEnemy();
			if(moveCountX >0){
				xPos +=xVel;
				moveCountX--;
			}
			else if(moveCountX < 0){
				moveCountX++;
				xPos -= xVel;
			}
			if(moveCountY>0){
				moveCountY--;
				yPos += yVel;
			}else if(moveCountY < 0){
				moveCountY++;
				yPos -= yVel;
			}
			
		}
		
		if (yPos <= 0) {
			yVel *= -1;
			yPos = yPos + 4;

		} 
		else if(!findPlayer){ // bounce around the screen unless the player changes findplayer static boolean variable 
			yPos += yVel;
		}

		if ((yPos + enemyShape.height) >= bounds.height) {
			yVel *= -1;
			yPos = (bounds.height - enemyShape.height - 4);
		}

		if (xPos <= 0) {
			xVel *= -1;
			xPos = 4;

		} else if(!findPlayer) {
			xPos += xVel;
		}

		if ((xPos + enemyShape.width) >= bounds.width) {
			xVel *= -1;
			xPos = (bounds.width - enemyShape.width - 4);
		}
		
		if (life <= 0) {
			//checkIn();
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, this));
			int score = (137*this.myLevel);
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Score, new Integer(score)));
		}
		
if(!findPlayer){
		if ((random.nextInt(100) < 7) && nextTime <= System.currentTimeMillis()) {
			EnemyMissile missile = (EnemyMissile) missilePool.checkOut();
			if (missile != null) {
				missile.setPosition((xPos + (enemyShape.width / 2)), yPos
						+ (enemyShape.height / 2));
				missile.setLVL(this.myLevel);
				double xd = xPos - getPlayerXPos();
				double yd = getPlayerYPos() - yPos;
			

				double angle  = Math.toDegrees(Math.atan(yd/xd)); 
				
				
				if (xd > 0 && yd > 0) { // quadrant 1
					angle = 90 - angle;
				
				} else if (xd < 0 && yd > 0) { // quadrant 2
					angle = 270 + (angle * -1);
				
				} else if (xd < 0 && yd < 0) { // quadrant 3
					angle = 270 - angle;
				
				} else if (xd > 0 && yd < 0) { // quadrant 4
					angle = 90 + (angle * -1);
				
				} 
				((EnemyMissile) missile).setDirection(angle);
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.AddLast, missile));
			}
			nextTime = System.currentTimeMillis() + NextMissileTime;

//			 use this to ensure the sound is played completely
//			 if ( missleSound.getState() != AudioSampleState.PLAYING )
//			 {
			// missleSound.play();
//			 }
		}
		}
	}


	public void findEnemy() {

		int distanceX = (int) (getPlayerXPos() - xPos); /*
														 * this calls the static
														 * player position field
														 * and subtracts this
														 * enemies x location to
														 * determine distance
														 */
		int distanceY = (int) (getPlayerYPos() - yPos);

		if (distanceX > 0) {
			distanceX = (distanceX - enemyShape.width + 1);
		} else if (distanceX <= 0) {
			distanceX = (int) (distanceX + enemyShape.getWidth() - 1);
		}

		//
		 moveCountX = (int) Math.round((distanceX) / xVel);// currentVelocityX);
		moveCountY = (int) Math.round((distanceY) / yVel); //currentVelocityY);

	}

	public void checkSurroundingTilesForPlayer() { /* or opening */

	}

	public void waitForAlly() {

	}

	public void checkCollision(Sprite obj) {
	}




	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return (enemyShape.intersects(boundingBox) ? enemyShape.getBounds()
				: null);
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
	}

	@Override
	public void mouseEvent(MouseEvent me) {
	}

	@Override
	public double getX() {
			return xPos;
	}

	@Override
	public double getY() {
		return yPos;
	}

	@Override
	public int getWidth() {
			return width;
	}

	@Override
	public int getHeight() {
			return height;
	}

	public static boolean isAttacking() {
		return isAttacking;
	}

	public int getDamage() {
		return damage;
	}

	public void setX(int x) {
		enemyShape.x += x;
	}

	public void setY(int y) {
		enemyShape.y += y;
	}

	public void updateLevel() {
		
		this.life = (20 * this.myLevel);

		if(life> 100){
			life = 100;
		}
		
		this.damage = (10 * this.myLevel);
	}

	public void takeDamage(int d) {
		life -= d;
	}

	@Override
	public void setX(double x) {
		this.xPos = x;
	}

	@Override
	public void setY(double y) {
		this.yPos = y;
	}

	@Override
	public void setXVel(double xVel) {
		this.xVel = xVel;
	}

	@Override
	public void setYVel(double yVel) {
		this.yVel = yVel;
	}

	@Override
	public void setPosition(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public void setVelocity(double vel) {
		this.xVel = vel;
		this.yVel = vel;
	}


	public void setLevel(int lvl) {
		this.myLevel = lvl;
	}

	public void setLife(int i) {

		this.life = i;
	}

	public int mylevel() {
		return this.myLevel;
	}
}

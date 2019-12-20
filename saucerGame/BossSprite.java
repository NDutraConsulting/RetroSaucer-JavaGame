package saucerGame;


import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Random;

//import libs.AudioSample;

import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;
import libs.GameEvent.GameEventType;

/**
 * Enemy sprite
 * 
 * 
 * 
 */
public class BossSprite extends MobileSprite {
	private static boolean isAttacking;
	private double xPos;
	private double yPos;

	private Rectangle enemyShape;

	private Random random;

	private long nextTime;

	private static BufferedImage enemyBmp, enemyBmp1;

	private int width;
	private int height;

	private static final long NextMissileTime = 500;

	// private AudioSample missleSound;
	private int life = 20;

	private double yVel = 4;
	private double xVel = 4;

	private Rectangle bounds;

	private int damage = 10;

	private int myLevel = 5;

	private EnemyMissilePool missilePool;
	private boolean lifeLow;
	private double scaler =1;

	
	public BossSprite(int x, int y, int life) {

		bounds = GameDisplay.getBounds();

		enemyShape = new Rectangle(x, y, 20, 20);
		// this.color = Color.red;

		this.life = 20;

		xPos = x;
		yPos = y;

		random = new Random();
		nextTime = System.currentTimeMillis() + NextMissileTime;
	}

	public BossSprite(EnemyMissilePool mPool) {
		try {
			if (enemyBmp == null) {
				enemyBmp = ImageUtil.loadBufferedImage(this,
						"images/finalboss.gif");
			}
			if (enemyBmp1 == null) {
				enemyBmp1 = ImageUtil.loadBufferedImage(this,
						"images/finalboss2.gif");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}
		missilePool = mPool;
		bounds = GameDisplay.getBounds();

		scaler = .7;
		enemyShape = new Rectangle(1, 1, ((int)((enemyBmp.getWidth())*scaler)), ((int)((enemyBmp.getHeight())*scaler)));

		this.life = 150;

		xPos = 1;
		yPos = 1;

		random = new Random();
		nextTime = System.currentTimeMillis() + NextMissileTime;
	}

	public BossSprite(EnemyMissilePool mPool, double s) {
		try {
			if (enemyBmp == null) {
				enemyBmp = ImageUtil.loadBufferedImage(this,
						"images/finalboss.gif");
			}
			if (enemyBmp1 == null) {
				enemyBmp1 = ImageUtil.loadBufferedImage(this,
						"images/finalboss2.gif");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}
		missilePool = mPool;
		bounds = GameDisplay.getBounds();

		this.scaler = s ;
		enemyShape = new Rectangle(1, 1, ((int)((enemyBmp.getWidth())*scaler)), ((int)((enemyBmp.getHeight())*scaler)));

		this.life = 150;

		xPos = 1;
		yPos = 1;

		random = new Random();
		nextTime = System.currentTimeMillis() + NextMissileTime;
	}

	@Override
	public void draw(Graphics2D g) {
		enemyShape.x = (int)( xPos);
		enemyShape.y = (int) (yPos);
				AffineTransform tx = new AffineTransform();
				tx.translate(enemyShape.x, enemyShape.y);
				tx.scale(scaler, scaler);
			
		if (!lifeLow) {
			g.drawImage(enemyBmp, tx, null);
		} else {
			g.drawImage(enemyBmp1, tx, null);
		}

	}

	@Override
	public void update() {

		if (yPos <= 0) {
			yVel *= -1;
			yPos = yPos + 4;

		} else {
			yPos += yVel;
		}

		if ((yPos + enemyShape.height) >= bounds.height) {
			yVel *= -1;
			yPos = (bounds.height - enemyShape.height - 4);
		}

		if (xPos <= 0) {
			xVel *= -1;
			xPos = 4;

		} else {
			xPos += xVel;
		}

		if ((xPos + enemyShape.width) >= bounds.width) {
			xVel *= -1;
			xPos = (bounds.width - enemyShape.width - 4);
		}

		if (life <= 0) {
			// checkIn();
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.RemoveBoss, this));
			int score = (839 * this.myLevel);
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Score, new Integer(score)));
		} else if (life<200){
			lifeLow = true;
		}
		
			if ((random.nextInt(100) < 7)
					&& nextTime <= System.currentTimeMillis()) {
				EnemyMissile missile = (EnemyMissile) missilePool.checkOut();
				if (missile != null) {
					missile.setPosition((xPos + (enemyShape.width / 2)), yPos
							+ (enemyShape.height / 2));
					missile.setLVL(this.myLevel);
					double xd = xPos - getPlayerXPos();
					double yd = getPlayerYPos() - yPos;

					double angle = Math.toDegrees(Math.atan(yd / xd));

					if (xd > 0 && yd > 0) { // quadrant 1
						angle = 90 - angle;

					} else if (xd < 0 && yd > 0) { // quadrant 2
						angle = 270 + (angle * -1);

					} else if (xd < 0 && yd < 0) { // quadrant 3
						angle = 270 - angle;

					} else if (xd > 0 && yd < 0) { // quadrant 4
						angle = 90 + (angle * -1);

					} else {

					}
					((EnemyMissile) missile).setDirection(angle);
					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.AddLast, missile));
				}
				nextTime = System.currentTimeMillis() + NextMissileTime;

				// use this to ensure the sound is played completely
				// if ( missleSound.getState() != AudioSampleState.PLAYING )
				// {
				// missleSound.play();
				// }
			}
			
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

		if (life > 100) {
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

	public int getLife() {
		// TODO Auto-generated method stub
		return life;
	}
}

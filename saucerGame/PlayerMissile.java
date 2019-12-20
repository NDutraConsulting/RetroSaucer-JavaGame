package saucerGame;


import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;
import libs.GameEvent.GameEventType;
import libs.SpritePoolObject;

/**
 * Player missile sprite. This sprite is created by the player sprite
 * 
 * @author nbouck && williamhooper
 * 
 */
public class PlayerMissile extends SpritePoolObject implements Sprite {

	private double xPos;
	private double yPos;
	private double width;
	private double height;
	private double yVel;
	private Rectangle missileShape;

	private int damage = 10;
	private double firingAngle;
	private double xVel;

	private static BufferedImage Bmp;
	private static BufferedImage Bmp1;

	/**
	 * Constructor
	 * 
	 */
	public PlayerMissile(PlayerMissilePool myPool) {
		try {
			if (Bmp == null) {
				Bmp = ImageUtil.loadBufferedImage(this,
						"images/yellowbullet.png");
			}
			if (Bmp1 == null) {
				Bmp1 = ImageUtil.loadBufferedImage(this,
						"images/yellowbullet.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}
		setParentPool(myPool);
		xPos = 0;
		yPos = 0;

		yVel = -8;
		xVel = -8;

		width = 15;
		height = 15;

		missileShape = new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) height);
	}

	@Override
	public void checkCollision(Sprite obj) {
		/**
		 * Check to see if we hit an enemy
		 */
		if (obj instanceof EnemySprite || obj instanceof BossSprite) {
			if (obj.intersects(missileShape) != null) {
				/**
				 * Dispatch an event to remove the enemy
				 */

				((MobileSprite) obj).takeDamage(damage);

				this.checkIn();
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.Remove, this));

			
				/**
				 * Done with the player missle
				 */
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		missileShape.x = (int) xPos;
		missileShape.y = (int) yPos;
		g.drawImage(Bmp, null, (int) xPos, (int) yPos);
	
	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return (missileShape.intersects(boundingBox) ? missileShape : null);
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEvent(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		if ((yPos + height) <= 0 || yPos > 900 || xPos > 1000 || xPos < 0) {
			/**
			 * The missle went off the screen so remove it
			 */
			this.checkIn();
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, this));
		}

		double nextY = (Math.sin((Math.toRadians(firingAngle))) * yVel);
		double nextX = (Math.cos((Math.toRadians(firingAngle))) * xVel);
		yPos = (yPos + nextY);
		xPos = (xPos + nextX);
	}

	/**
	 * @param xPos
	 *            the xPos to set
	 */
	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	/**
	 * @param yPos
	 *            the yPos to set
	 */
	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDamage() {
		// TODO Auto-generated method stub
		return damage;
	}

	public void revVel() {
		// TODO Auto-generated method stub

	}

	public void setDirection(double angle) {
		// TODO Auto-generated method stub
		this.firingAngle = (angle + 90);

	}

	public void setDirection(double mXPos, double mYPos) {
		// TODO Auto-generated method stub

	}
}

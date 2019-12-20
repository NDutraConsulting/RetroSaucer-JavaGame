package saucerGame;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;
import libs.GameEvent.GameEventType;

/**
 * Enemy missile sprite. This sprite is created by the enemy sprite
 * 
 * @author williamhooper
 * 
 */
public class EnemyMissile extends MobileSprite implements Sprite {
	private double xPos;
	private double yPos;
	private double width;
	private double heigth;
	private double xVel;
	private double yVel;
	private Rectangle missileShape;
	private Rectangle displayBounds;
	private static BufferedImage missleImage;
	private static BufferedImage missleImage1;

	private int damage = 5;
	private int lvl = 0;
	private double firingAngle;

	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 * @param xv
	 * @param yv
	 */

	public EnemyMissile(double x, double y, double xv, double yv) {
		xPos = x;
		yPos = y;

		xVel = xv;
		yVel = yv;

		try {
			if (missleImage == null) {
				missleImage = ImageUtil.loadBufferedImage(this,
						"images/pinkbullet.png");
			}
			if (missleImage1 == null) {
				missleImage1 = ImageUtil.loadBufferedImage(this,
						"images/limebullet.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}

		width = 15;
		heigth = 15;
		missileShape = new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) heigth);

		displayBounds = GameDisplay.getBounds();
	}

	public EnemyMissile() {
		xPos = 0;
		yPos = 0;

		xVel = 8;
		yVel = 8;

		try {
			if (missleImage == null) {
				missleImage = ImageUtil.loadBufferedImage(this,
						"images/pinkbullet.png");
			}
			if (missleImage1 == null) {
				missleImage1 = ImageUtil.loadBufferedImage(this,
						"images/limebullet.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - error found -
		}

		width = 15;
		heigth = 15;
		missileShape = new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) heigth);

		displayBounds = GameDisplay.getBounds();
	}

	@Override
	public void checkCollision(Sprite obj) {
		/**
		 * Check to see if we hit the player
		 */
		if (obj instanceof PlayerSprite) {
			if (obj.intersects(missileShape.getBounds()) != null) {
				/**
				 * Dispatch an event to remove the player
				 */

				this.checkIn();
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.Remove, this));

			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		missileShape.x = (int) xPos;
		missileShape.y = (int) yPos;
		if (lvl > 1) {
			g.drawImage(missleImage1, null, (int) xPos, (int) yPos);
		} else {
			g.drawImage(missleImage, null, (int) xPos, (int) yPos);
		}
	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return (missileShape.getBounds().intersects(boundingBox) ? missileShape
				.getBounds() : null);
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		

	}

	@Override
	public void mouseEvent(MouseEvent me) {
		
	}

	@Override
	public void update() {
		if ((yPos + this.missileShape.height) >= displayBounds.getHeight()
				|| yPos <= 0 || xPos <= 0 || xPos >= displayBounds.getWidth()) {
			this.checkIn();
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, this));
		}

		double nextY = (Math.sin((Math.toRadians(firingAngle))) * yVel);
		double nextX = (Math.cos((Math.toRadians(firingAngle))) * xVel);
		yPos = (yPos + nextY);
		xPos = (xPos + nextX);
		// xPos += xVel;
		// yPos += yVel;

	}

	@Override
	public double getX() {
		
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

	@Override
	public void setX(double x) {
		this.xPos = x;

	}

	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub
		this.yPos = y;
	}

	@Override
	public void setXVel(double xVel) {
		// TODO Auto-generated method stub
		this.xVel = xVel;
	}

	@Override
	public void setYVel(double yVel) {
		// TODO Auto-generated method stub
		this.yVel = yVel;
	}

	@Override
	public void setPosition(double x, double y) {
		// TODO Auto-generated method stub
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public void setVelocity(double vel) {
		// TODO Auto-generated method stub

	}

	public void setLVL(int level) {
		// TODO Auto-generated method stub
		this.lvl = level;
		if (lvl > 0) {
			damage = (5 * lvl);
		}
	}

	public void setDirection(double angle) {
		// TODO Auto-generated method stub
		int rAcc = (int) (Math.random() * 20);
		int r = (int) (Math.random() * 3);
		switch (r) {
		case 1: {
			rAcc *= -1;
		}
			break;
		case 2: {
			rAcc -= 20;
		}
			break;
		case 3: {
			rAcc = 0;
		}
		default: {
			rAcc -= 30;
		}
		}

		this.firingAngle = (angle + (90 + rAcc));

	}

	@Override
	public void takeDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
}

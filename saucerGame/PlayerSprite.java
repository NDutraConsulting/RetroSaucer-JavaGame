package saucerGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.AudioSample;
import libs.GameDisplay;
import libs.GameEvent;
import libs.ImageUtil;

import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;


/**
 * The player sprite. This is the sprite the player controls
 * 
 * 
 * 
 */
public class PlayerSprite extends MobileSprite {
	private double xPos;
	private double yPos;
	private double width;
	private double heigth;
	private Rectangle playerShape;
	private Rectangle displayBounds;
	private long missileTime;
	private BufferedImage player1Bmp, player1Bmp1;

	private boolean isDead = false;
	private PlayerMissilePool myMissilePool;
	private int damage = 4;
	private int life = 50000;

	private int velocity = 15;
	private int moveCountX = 0;

	private int moveCountY = 0;

	private double shootingAngle;

	private double sAngle;
	private boolean angleSet = false;
	private boolean shooting;

	private long absorbTimer;
	private int absorbCount;
	private long setAbsorbTimer;
	private int absorbDurration;
	private int level;
	private double xVel = 5.5;
	private int absorbMax;
	private BufferedImage player1Damaged;
	private BufferedImage player1DamagedFiring;
	
	private int hitCount = 50;
	private Image player1heavyDamagedFiring;
	private Image player1HeavyDamaged;

	private static AudioSample missleSound;
	private static boolean isAttacking = true;

	/**
	 * Constructor
	 * 
	 */
	public PlayerSprite() {
		displayBounds = GameDisplay.getBounds();

		xPos = displayBounds.getWidth() / 2;
		yPos = displayBounds.getHeight() - 48;

		setPlayerXPos((int) xPos);
		setPlayerYPos((int) yPos);

		width = 32;
		heigth = 32;
		playerShape = new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) heigth);

		missileTime = System.currentTimeMillis() + 250;
	}

	public PlayerSprite(PlayerMissilePool pool) {
		// findPlayer = false;
		myMissilePool = pool;
		displayBounds = GameDisplay.getBounds();

		try {

			player1Bmp = ImageUtil.loadBufferedImage(this,
					"images/saucerblue.png");
			player1Bmp1 = ImageUtil.loadBufferedImage(this,
					"images/saucerbasebluefiring.png");
			player1Damaged = ImageUtil.loadBufferedImage(this,
					"images/saucerdamage.png");
			player1DamagedFiring = ImageUtil.loadBufferedImage(this,
					"images/saucerdamagefiring.png");
			player1HeavyDamaged = ImageUtil.loadBufferedImage(this,
					"images/saucerheavydamage.png");
			player1heavyDamagedFiring = ImageUtil.loadBufferedImage(this,
					"images/saucerheavydamagefiring.png");

		} catch (IOException e) {
			e.printStackTrace();
			// this should quit - release mode -
		}
		try {
			missleSound = new AudioSample(this, "sounds/bomb2.wav");

		} catch (Exception e) {

		}

		width = (player1Bmp.getWidth());
		heigth = (player1Bmp.getHeight());
		xPos = (displayBounds.getWidth() - (width * 2));
		yPos = displayBounds.getHeight() - (heigth * 2);
		playerShape = new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) heigth);

		missileTime = System.currentTimeMillis() + 250;

		setPlayerXPos((int) xPos);

		setPlayerYPos((int) yPos);

	}

	public void checkCollision(Sprite obj) {

		if (obj.intersects(playerShape) != null) {
			if (obj instanceof EnemySprite) {
				
				
				if (findPlayer) {

					switch (this.level) {
					case 1: {
						life = life + 7;
						absorbCount += 1;
						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.lifeScore, new Integer(life)));

						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.Remove, obj));
					}

						break;
					case 2: {
						life = life + 25;
						absorbCount += 1;
						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.lifeScore, new Integer(life)));

						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.Remove, obj));
					}
					case 3: {
						life = life + 55;
						absorbCount += 1;
						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.lifeScore, new Integer(life)));

						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.Remove, obj));
					}
						break;
					case 4: {
						life = life + 10;
						absorbCount += 1;
						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.lifeScore, new Integer(life)));

						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.Remove, obj));
					}

						break;
					default: {
						life = life + 10;
						absorbCount += 1;
						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.lifeScore, new Integer(life)));

						GameEventDispatcher.dispatchEvent(new GameEvent(this,
								GameEventType.Remove, obj));

					}
					}

					if (absorbCount >= absorbMax) {
						findPlayer = false;
					}
				}

				else if (!findPlayer) {
					life = life - (((EnemySprite) obj).getDamage() / 4);

					hitCount = 0;
					/**
					 * Dispatch an event to update the Lifescore
					 */
					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.lifeScore, new Integer(life)));
				}
			} else if (obj instanceof EnemyMissile) {
				hitCount = 0;
				life = life - ((EnemyMissile) obj).getDamage();
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.lifeScore, new Integer(life)));
			}
		}

	}

	@Override
	public void draw(Graphics2D g) {

		if (player1Bmp == null) {
			playerShape.x = (int) xPos;
			playerShape.y = (int) yPos;
			g.setColor(Color.BLUE);
			g.fill(playerShape);
		} else {
			playerShape.x = (int) xPos;
			playerShape.y = (int) yPos;

			AffineTransform Tx = new AffineTransform();

			Tx.translate((xPos + (width / 2)), (yPos + (heigth / 2)));
			Tx.rotate(((shootingAngle * Math.PI) / 180));

			Tx.translate(-(xPos + (width / 2)), -(yPos + (heigth / 2)));
			Tx.translate(xPos, yPos);
			
			if (life > 450) {
				if (shooting && hitCount<15) {
					g.drawImage(player1DamagedFiring, Tx, null);
					shooting = false;
					hitCount++;
				}else if(!shooting && hitCount<15){
					g.drawImage(player1Damaged, Tx, null);
					
					hitCount++;
					
				}else if(shooting){
					g.drawImage(player1Bmp1, Tx, null);
					shooting = false;
				}else {
				
					g.drawImage(player1Bmp, Tx, null);
				}
//			} else if( life < 900 && life >400) {
//				if (shooting) {
//					g.drawImage(player1DamagedFiring, Tx, null);
//					shooting = false;
//				} else {
//					g.drawImage(player1Damaged, Tx, null);
//				}
				
			}else{
				
				if (shooting) {
					g.drawImage(player1heavyDamagedFiring, Tx, null);
					shooting = false;
					hitCount++;
				}else if(!shooting){
					g.drawImage(player1HeavyDamaged, Tx, null);
					
					hitCount++;
					
				}
			}

		}
	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return (playerShape.intersects(boundingBox) ? playerShape : null);
	}

	@Override
	public void update() {
		if (absorbTimer < System.currentTimeMillis()) {
			findPlayer = false;
		}

		/**
		 * Check for left/right/top/bottom wall collisions
		 * 
		 * update x & y Pos accordingly
		 */

		if (((xPos + width) <= displayBounds.width) && moveCountX > 0) {
			xPos = xPos + velocity;

			moveCountX--;
		} else if (xPos >= 0 && moveCountX < 0) {

			xPos = xPos - velocity;

			moveCountX++;
		}

		if (((yPos + heigth) <= displayBounds.height) && moveCountY > 0) {
			yPos = yPos + velocity;

			moveCountY--;
		} else if (yPos >= 0 && moveCountY < 0) {
			yPos = yPos - velocity;

			moveCountY++;

		}
		if (yPos <= 0) {

			yPos = 4;

		}

		else if ((yPos + playerShape.height) >= displayBounds.height) {

			yPos = (displayBounds.height - playerShape.height - 4);
		}

		if (xPos <= 0) {
			xVel *= -1;
			xPos = 4;

		} else if ((xPos + playerShape.width) >= displayBounds.width) {
			xVel *= -1;
			xPos = (displayBounds.width - playerShape.width - 4);
		} else if (findPlayer) {
			xPos += xVel;
		}

		if (life <= 0) {

			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, this));

			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.End, this));

		}

		setPlayerXPos((int) xPos);

		setPlayerYPos((int) yPos);

	}

	public double getX() {
		return xPos;
	}

	public double getY() {
		return yPos;
	}

	public boolean isDead() {

		// TODO Auto-generated method stub
		return isDead;

	}

	public boolean isDead(boolean d) {
		isDead = d;
		// TODO Auto-generated method stub
		return isDead;

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return (int) width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return (int) heigth;
	}

	@Override
	public void mouseEvent(MouseEvent me) {

		// if (lastTime < System.currentTimeMillis()) {
		if (me.getID() == MouseEvent.MOUSE_MOVED) {
			angleSet = false;
			double mXPos = (me.getX() - (xPos + width / 2));
			double mYPos = ((yPos + (heigth / 2)) - me.getY());

			sAngle = Math.toDegrees(Math.atan(((mYPos) / mXPos)));

			if (mXPos > 0 && mYPos > 0) { // quadrant 1
				shootingAngle = 90 - sAngle;
				// System.out.println("q1");
			} else if (mXPos < 0 && mYPos > 0) { // quadrant 2
				shootingAngle = 270 + (sAngle * -1);
				// System.out.println("q2");
			} else if (mXPos < 0 && mYPos < 0) { // quadrant 3
				shootingAngle = 270 - sAngle;
				// System.out.println("q3");
			} else if (mXPos > 0 && mYPos < 0) { // quadrant 4
				shootingAngle = 90 + (sAngle * -1);
				// System.out.println("q4");
			} else {
				// System.out.println("missed instance of a quadrant");
			}

			// System.out.println(shootingAngle);

			// changeFrame = true;

			// lastTime = System.currentTimeMillis() + 300;
			angleSet = true;
		}
		// }

		if (me.getButton() == MouseEvent.BUTTON1
				&& missileTime < System.currentTimeMillis() && angleSet) {

			if (!findPlayer) {
				PlayerMissile missile = (PlayerMissile) myMissilePool
						.checkOut();
				missile.setDirection(shootingAngle);
				if (missile != null) {
					shooting = true;
					missile.setxPos(xPos + (playerShape.width / 2));
					missile.setyPos(yPos + (playerShape.height / 2));
					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.AddFirst, missile));
					missileTime = System.currentTimeMillis() + 150;
					// if ( missleSound.getState() !=
					// AudioSampleState.PLAYING )
					// {
					missleSound.play();
					// }

				}

			}
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub

		if (ke.getKeyCode() == KeyEvent.VK_LEFT) {

			// moveCountX = -1;
			xPos = (xPos - velocity);
		} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveCountX = 1;

			xPos = (xPos + velocity);
		} else if (ke.getKeyCode() == KeyEvent.VK_UP) {

			// yPos = yPos - velocity;
			moveCountY = -1;

		} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			// yPos = yPos + velocity;
			moveCountY = 1;

		} else if (ke.getKeyCode() == KeyEvent.VK_M) {

			// dispatch event > set enemys to find player --> absorb mode to
			// gain life
			setAbsorbTimer();

		} else if (ke.getKeyCode() == KeyEvent.VK_SPACE
				&& missileTime < System.currentTimeMillis()) {
			missileTime = (System.currentTimeMillis() + 150);
			PlayerMissile missile = null;

			missile = (PlayerMissile) myMissilePool.checkOut();
			missile.setDirection(shootingAngle);
			if (missile != null) {

				missile.setxPos(xPos + (playerShape.width / 2));
				missile.setyPos(yPos + (playerShape.height / 2));
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.AddFirst, missile));

				// if ( missleSound.getState() != AudioSampleState.PLAYING )
				// {
				missleSound.play();

			}

		}

	}

	private synchronized void setAbsorbTimer() { // this is synchronized becuase
													// the keyboard events where
													// locking up

		if (setAbsorbTimer < System.currentTimeMillis()) {

			switch (this.level) { // adjust amount of time you can absorb here
			case 0: {
				setAbsorbTimer = System.currentTimeMillis() + 5000;

				absorbTimer = System.currentTimeMillis() + 2000;
				findPlayer = true;
				absorbMax = 7;
				absorbCount = 0;
			}
				break;
			case 1: {
				setAbsorbTimer = System.currentTimeMillis() + 5000;

				absorbTimer = System.currentTimeMillis() + 2000;
				findPlayer = true;
				absorbMax = 8;
				absorbCount = 0;
			}
				break;
			case 2: {
				setAbsorbTimer = System.currentTimeMillis() + 7000;

				absorbTimer = System.currentTimeMillis() + 4000;
				findPlayer = true;
				absorbMax = 11;
				absorbCount = 0;
			}
				break;
			case 3: {
				setAbsorbTimer = System.currentTimeMillis() + 1500
						+ absorbDurration;

				absorbTimer = System.currentTimeMillis() + absorbDurration;
				findPlayer = true;
				absorbMax = 30;
				absorbCount = 0;
			}
				break;
			case 4: {
				setAbsorbTimer = System.currentTimeMillis() + 500
						+ absorbDurration;

				absorbTimer = System.currentTimeMillis() + absorbDurration;
				findPlayer = true;
				absorbMax = 24;
				absorbCount = 0;
			}
				break;
			default: {
				setAbsorbTimer = System.currentTimeMillis() + 500
						+ absorbDurration;

				absorbTimer = System.currentTimeMillis() + absorbDurration;
				findPlayer = true;
				absorbCount = 0;
			}
				break;
			}

		}
	}

	public static boolean isAttacking() {
		// TODO Auto-generated method stub
		return isAttacking;
	}

	public int getDamage() {
		return damage;
	}

	public void revVel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setX(double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXVel(double xVel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setYVel(double yVel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVelocity(double vel) {
		// TODO Auto-generated method stub

	}

	public void takeDamage(int damage2) {
		// TODO Auto-generated method stub
		life -= damage2;
	}

	public void setAbsorbTime(int i) {
		// TODO Auto-generated method stub
		absorbDurration = i;
	}

	public void setLevel(int i) {
		// TODO Auto-generated method stub
		this.level = i;

	}

	public void setLife(int i) {
		// TODO Auto-generated method stub
		this.life = i;
	}

	public int getVel() {
		// TODO Auto-generated method stub
		return velocity;
	}

}

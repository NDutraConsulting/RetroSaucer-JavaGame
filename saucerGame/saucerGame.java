package saucerGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import libs.AudioMidi;
import libs.GameDisplay;
import libs.GameEngine;
import libs.Game;
import libs.GameEvent;
import libs.GameEventDispatcher;

import libs.KeyboardEventListener;
import libs.MouseEventListener;
import libs.Sprite;
import libs.GameEvent.GameEventType;
import libs.SpritePoolObject;

/**
 * Example game
 * 
 * @author Nbouck && William Hooper
 * 
 *         Cave Story/Enemies -- Cave_story_ballos_final.gif
 *         http://strategywiki.org/wiki/Cave_Story/Enemies
 * 
 * 
 */
public class saucerGame implements Game, MouseEventListener,
		KeyboardEventListener {
	/**
	 * Our list of sprites
	 */
	private LinkedList<Sprite> spriteList;

	/**
	 * Paths for the enemys
	 */
	// private BufferedImage background;

	private long nextTime;
	private long enemyCount;

	private ScoreSprite scoreSprite;

	private LifeSprite lifeSprite;

	private LvlSprite lvlSprite;

	private AudioMidi backGroundMusic;

	private GameState gameState;

	private PlayerSprite playerSprite;

	private long lastEnemyCount;

	private long StartCountDown;
	private PlayerMissilePool playerMissilePool;

	private EnemyMissilePool enemyMissilePool;

	private EnemySpritePool enemyPool;

	private int level = 1;

	private boolean lvl1;

	private boolean lvl2;

	private boolean lvl3;

	private boolean lvl4;

	private boolean defeatBoss1 = false;

	private boolean addEnemies = true;

	private BossSprite endBoss;

	private static final long NextEnemyTime = 500;
	private static final long NumberOfEnemys = 204;

	private enum GameState {
		SPLASH, RUNNING, END, PAUSE, STARTING, WIN
	};

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		saucerGame game = new saucerGame();
		GameEngine.start(game);

		/**
		 * If we are here we have stopped the game engine
		 */
		GameDisplay.dispose();
	}

	/**
	 * Constructor
	 */
	public saucerGame() {
		/**
		 * Create our game display
		 */
		GameDisplay.create(800, 600);

		playerMissilePool = new PlayerMissilePool();
		enemyMissilePool = new EnemyMissilePool();
		enemyPool = new EnemySpritePool(enemyMissilePool);

		/**
		 * Create our list of sprites
		 */

		spriteList = new LinkedList<Sprite>();

		/**
		 * Add a mouse listener so we can get mouse events
		 */
		GameDisplay.addMouseListener(this);

		/**
		 * Add a keyboard listener so we can get key presses
		 */
		GameDisplay.addKeyboardListener(this);

		/**
		 * Dont let the mouse leave our display
		 */
		GameDisplay.captureCursor(true);

		/**
		 * Create the two paths the enemy sprite will follow
		 */

		/**
		 * Setup some variables for managing the enemies
		 */
		nextTime = System.currentTimeMillis() + NextEnemyTime;
		enemyCount = 0;

		/*
		 * New Background
		 */

		gameState = GameState.SPLASH;

		spriteList.add(new SplashSprite());

		try {
			backGroundMusic = new AudioMidi(this, "sounds/backinblack.mid");
			backGroundMusic.setVolume((double) 3.5);
			backGroundMusic.loop(AudioMidi.LOOP_CONTINUOUSLY);
		} catch (InvalidMidiDataException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (MidiUnavailableException e) {

			e.printStackTrace();
		}
		
		 endBoss = new BossSprite(this.enemyMissilePool, .87);
		 endBoss.setLife(500);

	}

	public void buildRunning() {

		// try
		// {
		//
		// background = ImageUtil.loadBufferedImage( this,
		// "images/valcaanobg1.png" );
		// // player1Bmp = ImageUtil.loadBufferedImage( this,
		// // "images/toolbox.png" );
		//
		// }
		// catch ( IOException e )
		// {
		// e.printStackTrace();
		// // this should quit - release mode -
		// }

		/**
		 * Toss in the player, supporting sprites, and go
		 */
		spriteList.clear();
		playerSprite = new PlayerSprite(playerMissilePool);
		playerSprite.setLife(2000);
		lvlSprite = new LvlSprite();
		spriteList.add(lvlSprite);
		spriteList.add((Sprite) playerSprite);
		scoreSprite = new ScoreSprite();
		lifeSprite = new LifeSprite();
		spriteList.add(scoreSprite);
		spriteList.add(0, lifeSprite);

		lvl1 = false;
		lvl2 = false;
		lvl3 = false;
		lvl4 = false;
		this.enemyCount = 0;
		gameState = GameState.RUNNING;
	}

	@Override
	public void collisions() {
		/**
		 * Check collisions on the Sprite objects
		 */
		if (gameState == GameState.RUNNING) {
			synchronized (spriteList) {
				for (Sprite spriteObj : spriteList) {
					for (Sprite otherSprite : spriteList) {
						if (!otherSprite.equals(spriteObj)) {
							spriteObj.checkCollision(otherSprite);
						}
					}
				}
			}
		}
	}

	@Override
	public void draw() {
		/**
		 * Update the graphics we drew on
		 */
		GameDisplay.update();
	}

	@Override
	public void render() {
		/**
		 * Get the current graphics
		 */
		Graphics2D offscreenGraphics = (Graphics2D) GameDisplay.getContext();

		/**
		 * Draw a white background.
		 */
		offscreenGraphics.setColor(Color.BLACK);

		Rectangle bounds = GameDisplay.getBounds();
		offscreenGraphics.fillRect(0, 0, (int) bounds.getWidth(),
				(int) bounds.getHeight());

		/**
		 * Draw the Sprite objects
		 */
		synchronized (spriteList) {
			for (Sprite spriteObj : spriteList) {
				spriteObj.draw(offscreenGraphics);
			}
		}
	}

	@Override
	public void update() {
		/**
		 * See if it is time to release an enemy
		 */

		if (gameState == GameState.RUNNING) {

			if (scoreSprite.getScore() > 3500 && !defeatBoss1) {
				BossSprite b = new BossSprite(this.enemyMissilePool);
				b.setLife(500);
				b.setPosition(300, -10);
				b.setLevel(4);
				spriteList.add(b);
				defeatBoss1 = true;
				addEnemies = false;
			} else if (scoreSprite.getScore() > 4000
					&& scoreSprite.getScore() < 8000 && !lvl1) {
				lvlSprite.set(2);
				lvl1 = true;
				this.level = 2;
				playerSprite.setLevel(2);
				playerSprite.setAbsorbTime(8000);

			} else if (scoreSprite.getScore() > 12000
					&& scoreSprite.getScore() < 14000 && !lvl2) {
				lvlSprite.set(3);
				lvl2 = true;
				this.level = 3;
				playerSprite.setLevel(3);
				playerSprite.setAbsorbTime(9000);
			} else if (scoreSprite.getScore() > 15000
					&& scoreSprite.getScore() < 17000 && !lvl3) {
				lvlSprite.set(4);
				lvl3 = true;
				this.level = 4;
				playerSprite.setLevel(4);
				playerSprite.setAbsorbTime(14000);
			} else if (scoreSprite.getScore() > 19000 && !lvl4) {
				lvl4 = true;
				
				 endBoss = new BossSprite(this.enemyMissilePool, .87);
				 endBoss.setLife(500);
				 endBoss.setPosition(300, -10);
				 endBoss.setLevel(4);
				spriteList.add(endBoss);
				defeatBoss1 = true;
				addEnemies = false;
			}else if( endBoss.getLife() <=0 && scoreSprite.getScore() >19000){
				
				gameState = GameState.WIN;
			}

			if (lastEnemyCount >= NumberOfEnemys) {
				StartCountDown++;

				if (StartCountDown >= 14000) {

					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.End, this));
				}
			}

			if (enemyCount < NumberOfEnemys
					&& nextTime <= System.currentTimeMillis() && addEnemies) {
				nextTime = System.currentTimeMillis() + NextEnemyTime + 800;
				enemyCount++;
				if ((enemyCount % 2) == 0) {
					EnemySprite s = (EnemySprite) enemyPool.checkOut();
					if (s != null) {
						s.setLevel(this.level);
						s.updateLevel();

						s.setPosition(1, 1);

						spriteList.add(s);

					}
				} else {
					EnemySprite s = (EnemySprite) enemyPool.checkOut();
					if (s != null) {
						s.setLevel(level);
						s.setLife(10);
						s.setPosition(700, 5);
						spriteList.add(s);
					}

				}
			}
			/**
			 * Update the Sprite objects
			 */

			synchronized (spriteList) {
				for (Sprite spriteObj : spriteList) {
					spriteObj.update();
				}
			}
		} else if (gameState == GameState.WIN) {
			spriteList.clear();
			spriteList.add(playerSprite);
			spriteList.add(scoreSprite);
			spriteList.add(lifeSprite);
			spriteList.add(new WinSprite());

		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
				/**
				 * Exit the application
				 */
				backGroundMusic.close();
				GameEngine.stop();
				gameState = GameState.END;

			}
			if (ke.getKeyCode() == KeyEvent.VK_S) {
				/**
				 * Exit the application
				 */

				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.Start, this));
				gameState = GameState.STARTING;

			}

			if (ke.getKeyCode() == KeyEvent.VK_P) {
				/**
				 * test to see if the game is already paused
				 */
				if (gameState == GameState.PAUSE) {
					gameState = GameState.RUNNING;
					/**
					 * else Check if running
					 */
				} else if (gameState == GameState.RUNNING) {

					gameState = GameState.PAUSE;
				}
			}

		}

		/**
		 * Send the keyboard event to player sprite
		 */
		if (gameState == GameState.RUNNING) {
			playerSprite.keyboardEvent(ke);

		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		/**
		 * Send the mouse event to player sprite
		 */
		if (gameState == GameState.RUNNING) {
			playerSprite.mouseEvent(me);

		}
	}

	@Override
	public void manageGameEvent(GameEvent ge) {
		switch (ge.getType()) {
		case AddFirst: {
			synchronized (spriteList) {
				spriteList.addFirst((Sprite) ge.getAttachment());
			}
		}
			break;

		case AddLast: {
			synchronized (spriteList) {
				spriteList.addLast((Sprite) ge.getAttachment());
			}
		}
			break;

		case Remove: {

			synchronized (spriteList) {
				Sprite sprite = (Sprite) ge.getAttachment();

				spriteList.remove(sprite);
				if (sprite.getClass() == EnemySprite.class) {
					((SpritePoolObject) sprite).checkIn();
					lastEnemyCount++;

				}
			}
		}
			break;
		case RemoveBoss: {

			synchronized (spriteList) {
				Sprite sprite = (Sprite) ge.getAttachment();

				spriteList.remove(sprite);

			}
			addEnemies = true;
		}
			break;

		case Score: {
			int score = ((Integer) ge.getAttachment()).intValue();
			scoreSprite.add(score);
		}
			break;

		case lifeScore: {
			int life = ((Integer) ge.getAttachment()).intValue();
			lifeSprite.set(life);
		}
			break;

		case Start: {
			synchronized (spriteList) {
				buildRunning();
			}
		}

			break;

		case Splash: {

		}

			break;
		case End: {
			backGroundMusic.close();

			synchronized (spriteList) {
				spriteList.clear();
				spriteList.add(scoreSprite);
				spriteList.add(new EndSprite());
			}
			gameState = GameState.END;

		}

			break;
		}
	}

}

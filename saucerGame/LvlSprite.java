package saucerGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameDisplay;
import libs.Sprite;

/**
 * The Level sprite. This sprite displays the Level in the center of
 * the display
 * 
 * @author nbouck && williamhooper
 * 
 */
public class LvlSprite implements Sprite {
	private int level = 1;
	private Rectangle displayBounds;
	private boolean visible;
	private int viewCount;

	/**
	 * Constructor
	 * 
	 */
	public LvlSprite() {
		displayBounds = GameDisplay.getBounds();
	}

	@Override
	public void checkCollision(Sprite obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		if (visible) {
			Font f = new Font("Times New Roman", Font.BOLD, 44);
			g.setFont(f);
			g.setColor(Color.BLUE);
			g.drawString("LEVEL " + level, displayBounds.width / 2,
					displayBounds.height / 2);
		}
	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		// TODO Auto-generated method stub
		return null;
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
		if (viewCount > 100) {
			visible = false;
		}
		viewCount++;
		// TODO Auto-generated method stub

	}

	/**
	 * Add points to the score
	 * 
	 * @param value
	 */
	public void calcLvl(int value) {

		int tL = Math.round(value / 4000);
		if (tL > level) {
			viewCount = 0;
			visible = true;
		}
	}

	public void nextLvl() {
		viewCount = 0;
		visible = true;
		level = level + 1;
	}

	public int getLvl() {

		return level;
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

	public void set(int val) {

		level = val;
		viewCount = 0;
		visible = true;
	}

}

package saucerGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import libs.GameDisplay;

import libs.Sprite;

public class SplashSprite implements Sprite {

	private Rectangle displayBounds, displayBox;
	

	/**
	 * Constructor
	 * 
	 */
	public SplashSprite() {
		displayBounds = GameDisplay.getBounds();

		displayBox = new Rectangle(100, 100, 400, 450);

	}

	@Override
	public void checkCollision(Sprite obj) {

	}

	@Override
	public void draw(Graphics2D g) {

		Font f = new Font("Times New Roman", Font.BOLD, 24);
		Font f2 = new Font("Times New Roman", Font.BOLD, 17);
		g.setColor(Color.GRAY);
		g.fill(displayBox);

		g.setFont(f);
		g.setColor(Color.YELLOW);
		g.drawString("Press S to Start ", (displayBounds.x + 140),
				(displayBounds.y + 150));
		g.setFont(f2);
		g.setColor(Color.WHITE);
		g.drawString("M to absorb enemies ", (displayBounds.x + 140),
				(displayBounds.y + 290));
		g.setFont(f2);
		g.setColor(Color.WHITE);
		g.drawString("Arrow keys to move ", (displayBounds.x + 140),
				(displayBounds.y + 330));
		g.setFont(f2);
		g.setColor(Color.WHITE);
		g.drawString("Aim with the mouse cursor ", (displayBounds.x + 140),
				(displayBounds.y + 370));
		g.setFont(f2);
		g.setColor(Color.WHITE);
		g.drawString("Space bar and/or mouse BUTTON1 shoots ",
				(displayBounds.x + 140), (displayBounds.y + 410));

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
		// TODO Auto-generated method stub

	}

	/**
	 * Add points to the score
	 * 
	 * @param value
	 */

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

}

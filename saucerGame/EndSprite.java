package saucerGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameDisplay;

import libs.Sprite;

public class EndSprite implements Sprite {

	private Rectangle displayBounds;

	/**
	 * Constructor
	 * 
	 */
	public EndSprite() {
		displayBounds = GameDisplay.getBounds();

	}

	@Override
	public void checkCollision(Sprite obj) {

	}

	@Override
	public void draw(Graphics2D g) {

		Font f = new Font("Times New Roman", Font.BOLD, 24);
		g.setColor(Color.GRAY);

		g.fillRect(50, 200, 500, 200);

		g.setFont(f);
		g.setColor(Color.RED);
		g.drawString("Game Over ", (displayBounds.width / 2),
				(displayBounds.height / 2));
		g.setFont(f);
		g.setColor(Color.RED);
		g.drawString("S to reStart ", (displayBounds.width / 2),
				((displayBounds.height / 2) + 50));

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

	public void revVel() {
		// TODO Auto-generated method stub

	}
}

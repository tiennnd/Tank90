package com.t3h.model;

import java.awt.Image;
import java.awt.Rectangle;

public class Bullet extends Dad {
	private static final int SPACE_DEFAULT = 3;

	private int speed;

	private long currentTime = 0;

	public Bullet(int x, int y, int size, int orient, Image[] img, int speed) {
		super(x, y, size, orient, img);
		this.speed = speed;
	}

	@Override
	public void move(long time) {
		if (time - currentTime >= speed) {
			currentTime = time;
		} else {
			return;
		}

		switch (orient) {
		case LEFT:
			x -= SPACE_DEFAULT;
			break;

		case RIGHT:
			x += SPACE_DEFAULT;
			break;

		case UP:
			y -= SPACE_DEFAULT;
			break;

		case DOWN:
			y += SPACE_DEFAULT;
			break;
		}
	}

	public boolean isIntersect(Bullet bullet) {
		Rectangle rectBullet1 = new Rectangle(x, y, size, size);
		Rectangle rectBullet2= new Rectangle(bullet.getX(), bullet.getY(),
				bullet.getSize(), bullet.getSize());

		return rectBullet1.intersects(rectBullet2);
		
	}
	
	public boolean isIntersect(Tank tank) {

		int newX = x;
		int newY = y;

		switch (orient) {
		case LEFT:
			newX--;
			break;
		case RIGHT:
			newX++;
			break;
		case UP:
			newY--;
			break;
		case DOWN:
			newY++;
			break;
		}

		Rectangle rectTank = new Rectangle(newX, newY, size, size);
		Rectangle rectShape = new Rectangle(tank.getX(), tank.getY(),
				tank.getSize(), tank.getSize());

		return rectTank.intersects(rectShape);
	}

	public boolean isIntersect(Shape shape) {

		int newX = x;
		int newY = y;

		switch (orient) {
		case LEFT:
			newX--;
			break;
		case RIGHT:
			newX++;
			break;
		case UP:
			newY--;
			break;
		case DOWN:
			newY++;
			break;
		}

		Rectangle rectBullet = new Rectangle(newX, newY, size, size);
		Rectangle rectShape = new Rectangle(shape.getX(), shape.getY(),
				shape.getWidth(), shape.getHeight());

		return rectBullet.intersects(rectShape);
	}

	public int getSpeed() {
		return speed;
	}

	public Image[] getImg() {
		return img;
	}
}

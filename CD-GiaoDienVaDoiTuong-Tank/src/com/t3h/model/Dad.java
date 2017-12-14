package com.t3h.model;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Dad {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;

	
	protected int x, y, size, orient;
	protected Image[] img;
	
	public Dad(int x, int y, int size, int orient, Image[] img) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
		this.orient = orient;
		this.img = img;
	}
	
	public abstract void move(long time);
	public abstract boolean isIntersect(Shape shape);
	
	public void draw(Graphics2D g) {
		g.drawImage(img[orient], x, y, size, size, null);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getSize() {
		return size;
	}
	public int getOrient() {
		return orient;
	}
	public Image[] getImg() {
		return img;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}

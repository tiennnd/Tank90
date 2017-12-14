package com.t3h.model;

import java.awt.Graphics2D;
import java.awt.Image;

public class Shape {
	int x, y;
	int width, height;
	int type;
	Image img;

	public Shape(int x, int y, int width, int height, int type, Image img) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.img = img;
	}
	
	public void setSize(int width, int height) {
		this.width = 2*width;
		this.height = 2*height;
	}
	
	public int getX() {
		return x;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType() {
		return type;
	}

	public Image getImg() {
		return img;
	}

	public void draw(Graphics2D g) {
		g.drawImage(img, x, y, width, height, null);
		
	}

}

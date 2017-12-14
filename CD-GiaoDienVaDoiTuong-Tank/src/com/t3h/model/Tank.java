package com.t3h.model;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import com.t3h.control.Manager;

public class Tank extends Dad{

	private static final int TIME_OUT = 1000;

	private int speedMove, speedFire;
	private int timeOut=0;

	private long  currentTime = 0;
	public Tank(int x, int y, int size, int orient, Image[] img, int speedMove,
			int speedFire) {
		super(x, y, size, orient, img);
		this.speedMove = speedMove;
		this.speedFire = speedFire;
	}

	public boolean isFire(long time) {
		if (time % speedFire == 0) {
			return true;
		}
		return false;
	}

	public void changeOrient(int newOrient) {
		this.orient = newOrient;
	}
	
	@Override
	public void move(long time) {
		if(time - currentTime >= speedMove) {
			currentTime = time;
		}else {
			return;
		}
		
		switch (orient) {
		case LEFT:
			if (x <= Manager.EDGE_LEFT_MAP)
				return;
			x--;
			break;

		case RIGHT:
			if (x + size >= Manager.EDGE_RIGHT_MAP + Manager.ICON_MAP_SIZE) {
				return;
			}
			x++;
			break;

		case UP:
			if (y <= Manager.EDGE_TOP_MAP) {
				return;
			}
			y--;
			break;

		case DOWN:
			if (y + size >= Manager.EDGE_BOTTOM_MAP)
				return;
			y++;
			break;
		}
	}
	
	public int getSpeedMove() {
		return speedMove;
	}

	public int getSpeedFire() {
		return speedFire;
	}

	public boolean isFire() {
		if(timeOut <= 0) {
			return true;
		}
		return false;
	}

	public void setTimeOut() {
		this.timeOut = TIME_OUT;
	}

	public void downTimeOut(int value){
		if(timeOut > 0) {
			timeOut -= value;
		}
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
		
		Rectangle rectTank = new Rectangle(newX,newY,size,size);
		Rectangle rectShape = new Rectangle(shape.getX(),shape.getY(),Manager.ICON_MAP_SIZE, Manager.ICON_MAP_SIZE);
		
		return rectTank.intersects(rectShape);
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
		
		Rectangle rectTank = new Rectangle(newX,newY,size,size);
		Rectangle rectShape = new Rectangle(tank.getX(),tank.getY(),Manager.TANK_SIZE, Manager.TANK_SIZE);
		
		return rectTank.intersects(rectShape);
	}
	
	
	public void changeOrient() {
		Random random = new Random();
		this.orient = random.nextInt(4);
	}
	
}

package com.t3h.control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.t3h.model.Bullet;
import com.t3h.model.Dad;
import com.t3h.model.Shape;
import com.t3h.model.Tank;

public class Manager {
	
	/**
	 * các thuộc tính giúp giới hạn bản đồ chơi
	 */
	public static final int MAP_SIZE = 26;
	public static final int POS_X_START_MAP = 200;
	public static final int POS_Y_START_MAP = 20;

	/**
	 * Thông số trạng thái mặc định của xe tăng
	 */
	public static final int TANK_X = 380;
	public static final int TANK_Y = 500;
	public static final int TANK_SIZE = 35;
	public static final int TANK_ORIENT_DEFAULT = 2;
	public static final int TANK_SPEED_MOVE = 5;
	public static final int TANK_SPEED_FIRE = 5;
	public static final int TIME_OUT = 5;
	public static final int TANK_HP_DEFAULT = 100;
	
	/**
	 * Thuộc tính của đạn
	 */
	private static final int BULLET_SIZE = 20;
	private static final int BULLET_SPEED = 1;
	
	/**
	 * Thuộc tính quy định kiểu của từng thành phần trong bản đồ
	 */
	public static final int MAP_TYPE_SPACE = 0;
	public static final int MAP_TYPE_GRASS = 1;
	public static final int MAP_TYPE_WATER = 2;
	public static final int MAP_TYPE_BRICK = 3;
	public static final int MAP_TYPE_ROCK = 4;
	public static final int MAP_TYPE_KING = 7;
	public static final int ICON_MAP_SIZE = 20;

	/**
	 * Giới hạn bản đồ chơi (phần bản đồ xe tăng di chuyển)
	 */
	public static final int EDGE_LEFT_MAP = 200;
	public static final int EDGE_RIGHT_MAP = 700;
	public static final int EDGE_TOP_MAP = 20;
	public static final int EDGE_BOTTOM_MAP = 540;
	
	private static final int VALUE_WILL_FIRE = 4;
	private static final int NUMBER_OF_TANK = 4;

	private static Font font = new Font("Tahoma",Font.BOLD,20);
	private boolean loose;
	private boolean win;
	private int score = 0;
	private int countEnemies = 10;
	private int stage = 1;
	private int life = 2;

	private Tank mTank;
	private Shape[][] map;
	private int[][] mapType;
	private List<Bullet> listBullet;
	private List<Bullet>[] listBulletOtherTank;
	private List<Tank> listTank;
	private Image[] imgBullets;
	private Image[] imgTank;
	
	private AudioManager audioMgr;
	
	public Manager() {
		initObjects();
	}

	private void initObjects() {
		initMap();
		initMainTank();
		initOtherTank();
		initBullet();
		initAudio();
	}

	private void initAudio() {
		audioMgr = new AudioManager();
		audioMgr.getStart().play();
	}

	private void initOtherTank() {
		Image imageUp = new ImageIcon(getClass().getResource(
				"/IMAGE/dich1_up.png").getPath()).getImage();
		Image imageDown = new ImageIcon(getClass().getResource(
				"/IMAGE/dich1_down.png").getPath()).getImage();
		Image imageLeft = new ImageIcon(getClass().getResource(
				"/IMAGE/dich1_left.png").getPath()).getImage();
		Image imageRight = new ImageIcon(getClass().getResource(
				"/IMAGE/dich1_right.png").getPath()).getImage();
		imgTank = new Image[4];
		imgTank[0] = imageLeft;
		imgTank[1] = imageRight;
		imgTank[2] = imageUp;
		imgTank[3] = imageDown;

		Tank oTank1 = new Tank(200, 20, TANK_SIZE, Dad.RIGHT, imgTank,
				2 * TANK_SPEED_MOVE, TANK_SPEED_FIRE);
		Tank oTank2 = new Tank(680, 20, TANK_SIZE, Dad.RIGHT, imgTank,
				TANK_SPEED_MOVE, TANK_SPEED_FIRE);
		Tank oTank3 = new Tank(520, 180, TANK_SIZE, Dad.RIGHT, imgTank,
				TANK_SPEED_MOVE, TANK_SPEED_FIRE);
		Tank oTank4 = new Tank(360, 220, TANK_SIZE, Dad.RIGHT, imgTank,
				TANK_SPEED_MOVE, TANK_SPEED_FIRE);
		
		listTank = new ArrayList<Tank>();
		listTank.add(oTank1);
		listTank.add(oTank2);
		listTank.add(oTank3);
		listTank.add(oTank4);
	}

	/**
	 * 
	 * [Method name] initMap() [Descript] khởi tạo bản đồ chơi <br>
	 * [Author] TienNguyen <br>
	 * [Date] Apr 6, 2016 <br>
	 */
	private void initMap() {
		Image grass = new ImageIcon(getClass().getResource("/IMAGE/grass.png")
				.getPath()).getImage();
		Image water = new ImageIcon(getClass().getResource("/IMAGE/water.png")
				.getPath()).getImage();
		Image brick = new ImageIcon(getClass().getResource(
				"/IMAGE/brick_new.png").getPath()).getImage();
		Image rock = new ImageIcon(getClass().getResource("/IMAGE/rock.png")
				.getPath()).getImage();
		
		

		mapType = new int[MAP_SIZE][MAP_SIZE];
		mapType = getMapText();
		map = new Shape[MAP_SIZE][MAP_SIZE];

		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				switch (mapType[i][j]) {
				case MAP_TYPE_GRASS:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i, ICON_MAP_SIZE,
							ICON_MAP_SIZE, MAP_TYPE_GRASS, grass);
					break;
				case MAP_TYPE_WATER:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i, ICON_MAP_SIZE,
							ICON_MAP_SIZE, MAP_TYPE_WATER, water);
					break;
				case MAP_TYPE_BRICK:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i, ICON_MAP_SIZE,
							ICON_MAP_SIZE, MAP_TYPE_BRICK, brick);
					break;
				case MAP_TYPE_ROCK:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i, ICON_MAP_SIZE,
							ICON_MAP_SIZE, MAP_TYPE_ROCK, rock);
					break;
				case MAP_TYPE_SPACE:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i, ICON_MAP_SIZE,
							ICON_MAP_SIZE, MAP_TYPE_SPACE, null);
					break;
				case MAP_TYPE_KING:
					map[i][j] = new Shape(POS_X_START_MAP + ICON_MAP_SIZE * j,
							POS_Y_START_MAP + ICON_MAP_SIZE * i,
							ICON_MAP_SIZE * 2, ICON_MAP_SIZE * 2,
							MAP_TYPE_KING, null);
					break;
				}
			}
		}
	}

	/**
	 * 
	 * [Method name] getMapText() [Descript] lấy dữ liệu từ file ra để chuyển
	 * đổi sang từng thành phần trong Map [Author] TienNguyen <br>
	 * [Date] Apr 6, 2016 <br>
	 * 
	 * @param field
	 *            name Type <br>
	 * @return mangKieu int[][]<br>
	 */
	private int[][] getMapText() {
		
		
		int temp[][] = new int[MAP_SIZE + 1][MAP_SIZE + 1];
		try {
			File file = new File(getClass().getResource("/MAP/map.txt")
					.getPath());
			RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
			String line = accessFile.readLine();
			int hang = 0;

			while (line != null) {
				String str[] = line.split(" ");
				for (int j = 0; j < MAP_SIZE; j++) {
					temp[hang][j] = Integer.valueOf(str[j]);
				}
				hang++;
				line = accessFile.readLine();
			}
			accessFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * [Method name] initMainTank [Descript] khởi tạo xe tăng cho người chơi
	 * [Author] TienNguyen [Date] Apr 6, 2016 <br>
	 */
	private void initMainTank() {
		Image imageUp = new ImageIcon(getClass().getResource(
				"/IMAGE/tank_player_up.png").getPath()).getImage();
		Image imageDown = new ImageIcon(getClass().getResource(
				"/IMAGE/tank_player_down.png").getPath()).getImage();
		Image imageLeft = new ImageIcon(getClass().getResource(
				"/IMAGE/tank_player_left.png").getPath()).getImage();
		Image imageRight = new ImageIcon(getClass().getResource(
				"/IMAGE/tank_player_right.png").getPath()).getImage();
		Image imgTank[] = { imageLeft, imageRight, imageUp, imageDown };

		mTank = new Tank(TANK_X, TANK_Y, TANK_SIZE, TANK_ORIENT_DEFAULT,
				imgTank, TANK_SPEED_MOVE, TANK_SPEED_FIRE);
	}

	/**
	 * [Method name] initBullet [Descript] khởi tạo viên đạn hình dạng khi nó di
	 * chuyển và khi nó nổ [Author] TienNguyen [Date] Apr 6, 2016 <br>
	 */
	@SuppressWarnings("unchecked")
	private void initBullet() {
		Image bulletLeft = new ImageIcon(getClass().getResource("/IMAGE/bullet_left.png").getPath()).getImage();
		Image bulletRight = new ImageIcon(getClass().getResource("/IMAGE/bullet_right.png").getPath()).getImage();
		Image bulletUp = new ImageIcon(getClass().getResource("/IMAGE/bullet_up.png").getPath()).getImage();
		Image bulletDown = new ImageIcon(getClass().getResource("/IMAGE/bullet_down.png").getPath()).getImage();
		
		imgBullets = new Image[4];
		imgBullets[0] = bulletLeft;
		imgBullets[1] = bulletRight;
		imgBullets[2] = bulletUp;
		imgBullets[3] = bulletDown;

		listBullet = new ArrayList<Bullet>();
		listBulletOtherTank = new List[4];
		for (int i = 0; i < 4; i++) {
			listBulletOtherTank[i] = new ArrayList<Bullet>();
		}
	}

	/**
	 * [Method name] drawBackground [Descript] vẽ hình nền của Panel [Author]
	 * TienNguyen [Date] Apr 6, 2016 <br>
	 */
	private void drawBackground(Graphics2D g2d) {
		Image dark_brick = new ImageIcon(getClass().getResource(
				"/IMAGE/dark_brick.png")).getImage();
		Image score = new ImageIcon(getClass().getResource("/IMAGE/score.png"))
				.getImage();
		Image lives = new ImageIcon(getClass().getResource("/IMAGE/lives.png"))
				.getImage();
		Image stage = new ImageIcon(getClass().getResource("/IMAGE/stage.png"))
				.getImage();
		Image enemies = new ImageIcon(getClass().getResource(
				"/IMAGE/enemies.png")).getImage();

		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 50; j++) {
				int x = j * ICON_MAP_SIZE;
				int y = i * ICON_MAP_SIZE;
				if (x < EDGE_LEFT_MAP || x > EDGE_RIGHT_MAP) {
					g2d.drawImage(dark_brick, x, y, ICON_MAP_SIZE,
							ICON_MAP_SIZE, null);
				} else if (i == 0 || i == MAP_SIZE + 1) {
					g2d.drawImage(dark_brick, x, y, ICON_MAP_SIZE,
							ICON_MAP_SIZE, null);
				}
			}
		}
		g2d.drawImage(score, ICON_MAP_SIZE, ICON_MAP_SIZE,
				score.getWidth(null), score.getHeight(null), null);
		g2d.drawImage(lives, ICON_MAP_SIZE,
				2 * ICON_MAP_SIZE + score.getHeight(null),
				lives.getWidth(null), lives.getHeight(null), null);
		g2d.drawImage(enemies, EDGE_RIGHT_MAP + 2 * ICON_MAP_SIZE,
				ICON_MAP_SIZE, enemies.getWidth(null), enemies.getHeight(null),
				null);
		g2d.drawImage(stage, EDGE_RIGHT_MAP + 2 * ICON_MAP_SIZE, 2
				* ICON_MAP_SIZE + stage.getHeight(null), stage.getWidth(null),
				stage.getHeight(null), null);

	}

	/**
	 * [Method name] drawObjects [Descript] vẽ lần lượt các thứ có trong Panel
	 * [Author] TienNguyen [Date] Apr 6, 2016 <br>
	 */
	public void drawObjects(Graphics2D g) {
		mTank.draw(g);
		drawBackground(g);
		drawBullet(g);
		drawOtherTank(g);
		drawMap(g);
		drawLabel(g);
	}
	
	/**
	 * [Method name] drawLabel [Descript] vẽ các chữ mô tả như điểm, level, số
	 * lượng xe tăng [Author] TienNguyen [Date] Apr 6, 2016 <br>
	 */
	private void drawLabel(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(score), 80, 100);
		g.drawString(String.valueOf(life), 90, 225);
		g.drawString(String.valueOf(countEnemies), 810, 100); 
		g.drawString(String.valueOf(stage), 810, 225);
	}

	/**
	 * [Method name] drawOtherTank [Descript] vẽ xe tăng địch [Author]
	 * TienNguyen [Date] Apr 6, 2016 <br>
	 */
	private void drawOtherTank(Graphics2D g) {
		for (int i = 0; i < listTank.size(); i++) {
			Tank tank = listTank.get(i);
			tank.draw(g);
		}
	}

	/**
	 * [Method name] drawMap [Descript] vẽ bản đồ, các thành phần như tường, đá,
	 * nước [Author] TienNguyen [Date] Apr 6, 2016 <br>
	 */
	private void drawMap(Graphics2D g) {
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				if (map[i][j].getType() == MAP_TYPE_KING) {
					g.drawImage(map[i][j].getImg(), map[i][j].getX(),
							map[i][j].getY(), ICON_MAP_SIZE * 2,
							ICON_MAP_SIZE * 2, null);
				} else {
					g.drawImage(map[i][j].getImg(), map[i][j].getX(),
							map[i][j].getY(), ICON_MAP_SIZE, ICON_MAP_SIZE,
							null);
				}
			}
			
			//Draw Bird
			Image bird = new ImageIcon(getClass().getResource("/IMAGE/bird.png")
					.getPath()).getImage();
			g.drawImage(bird, 440, 500, ICON_MAP_SIZE*2, ICON_MAP_SIZE*2, null);
			if(loose == true) {
				Image bird_died = new ImageIcon(getClass().getResource("/IMAGE/bird_died.png")
						.getPath()).getImage();
				g.drawImage(bird_died, 440, 500, ICON_MAP_SIZE*2, ICON_MAP_SIZE*2, null);
			}
		}
	}

	private void drawBullet(Graphics2D g) {
		int dem =0;
		for (int i = 0; i < listBullet.size(); i++) {
			Bullet bullet = listBullet.get(i);
			bullet.draw(g);
		}
		for (int i = 0; i < 4; i++) {
			List<Bullet> list = listBulletOtherTank[dem];
			for (int j = 0; j < list.size(); j++) {
				Bullet bullet = list.get(j);
				bullet.draw(g);
			}
			dem++;
		}
	}

	private Shape getShape(int x, int y) {
		int i = (x - EDGE_LEFT_MAP) / ICON_MAP_SIZE;
		int j = (y - EDGE_TOP_MAP) / ICON_MAP_SIZE;
		if (i >= 0 && j >= 0 && i < MAP_SIZE && j < MAP_SIZE) {
			return map[j][i];
		}
		return null;
	}

	private Shape getShapeBulletFireTo(Bullet bullet) {
		int x, y;

		switch (bullet.getOrient()) {
		case Dad.LEFT:
			x = bullet.getX() + BULLET_SIZE/2;
			y = bullet.getY()+ BULLET_SIZE/2;
			while (x > EDGE_LEFT_MAP) {
				Shape shape = getShape(x, y);
				if (shape != null && shape.getType() > 2) {
					return shape;
				}
				x -= ICON_MAP_SIZE;
			}
			break;

		case Dad.RIGHT:
			x = bullet.getX() + BULLET_SIZE/2;
			y = bullet.getY()+ BULLET_SIZE/2;
			while (x < EDGE_RIGHT_MAP) {
				Shape shape = getShape(x, y);
				if (shape != null && shape.getType() > 2) {
					return shape;
				}
				x += ICON_MAP_SIZE;
			}
			break;

		case Dad.UP:
			x = bullet.getX()+ BULLET_SIZE/2;
			y = bullet.getY()+ BULLET_SIZE/2;
			while (y > EDGE_TOP_MAP) {
				Shape shape = getShape(x, y);
				if (shape != null && shape.getType() > 2) {
					return shape;
				}
				y -= ICON_MAP_SIZE;
			}
			break;

		case Dad.DOWN:
			x = bullet.getX()+ BULLET_SIZE/2;
			y = bullet.getY()+ BULLET_SIZE/2;
			while (y < EDGE_BOTTOM_MAP) {
				Shape shape = getShape(x, y);
				if (shape != null && shape.getType() > 2) {
					return shape;
				}
				y += ICON_MAP_SIZE;
			}
			break;
		}

		return null;
	}


	private int oldX[] = new int[4];
	private int oldY[] = new int[4];

	public void moveOtherTank(long time) {
		int dem = 0;
		for (Tank tank : listTank) {
			tank.move(time);
			
			// chuyển hướng khi gặp cạnh tường (vị trí không thay đổi)
			if (tank.getX() == oldX[dem] && tank.getY() == oldY[dem]) {
				tank.changeOrient();
			}
			oldX[dem] = tank.getX();
			oldY[dem] = tank.getY();
			dem++;

			// va chạm khi gặp gạch đá
			for (int i = 0; i < MAP_SIZE; i++) {
				for (int j = 0; j < MAP_SIZE; j++) {
					if (map[i][j].getType() > 1 && tank.isIntersect(map[i][j])) {
						tank.changeOrient();
					}
				}
			}
		}
	}

	private void addTank() {
		int posGenarate[][] = new int[4][2];
		posGenarate[0][0] = 200;
		posGenarate[0][1] = 20;
		posGenarate[1][0] = 280;
		posGenarate[1][1] = 60;
		posGenarate[2][0] = 320;
		posGenarate[2][1] = 100;
		posGenarate[3][0] = 680;
		posGenarate[3][1] = 20;
		
		Random random = new Random();
		int temp = random.nextInt(4);
		listTank.add(new Tank(posGenarate[temp][0], posGenarate[temp][1],
				TANK_SIZE, random.nextInt(4), imgTank, TANK_SPEED_MOVE,
				TANK_SPEED_FIRE));
		
	}

	public void moveBullets(long time) {

		for (int i = 0; i < listBullet.size(); i++) {
			Bullet bullet = listBullet.get(i);
			bullet.move(time);

			// ban trung xe tang
			for (int j = 0; j < listTank.size(); j++) {
				Tank tank = listTank.get(j);
				if (bullet.isIntersect(tank)) {
					if(i>0) {
						listBullet.remove(i);
						i--;
					}
					listTank.remove(j--);
					score += 40;
					countEnemies--;
					audioMgr.getExplode().setLoop(1);
					
					//kiem tra dieu kien ket thuc
					if(countEnemies == 0) {
						win = true;
						audioMgr.getWin().setLoop(1);
						return;
					}
					addTank();
				}
			}

			// ban vao gach da hoac vao tuong
			Shape shape = getShapeBulletFireTo(bullet);
			if (shape != null) {
				if (bullet.isIntersect(shape)) {
					listBullet.remove(i);
					if(i>0) {
						i--;
					}
					if (shape.getType() < MAP_TYPE_ROCK) {
						shape.setImg(null);
						audioMgr.getHitblock().setLoop(1);
						shape.setType(MAP_TYPE_SPACE);
					}
					if (shape.getType() == MAP_TYPE_ROCK) {
						audioMgr.getShootStone().setLoop(1);
					}
					
					//ban trung KING
					if (shape.getType() == MAP_TYPE_KING) {
						System.out.println(shape.getX());
						System.out.println(shape.getY());
						loose = true;
						Image bird_died = new ImageIcon(getClass().getResource(
								"/IMAGE/bird_died.png").getPath()).getImage();
						shape.setImg(bird_died);
						shape.setType(MAP_TYPE_SPACE);
						audioMgr.getGameOver().setLoop(1);
					}
				}
			} else {
				if (bullet.getX() > EDGE_RIGHT_MAP + ICON_MAP_SIZE
						|| bullet.getY() > EDGE_BOTTOM_MAP
						|| bullet.getX() < EDGE_LEFT_MAP
						|| bullet.getY() < EDGE_TOP_MAP) {
					listBullet.remove(i--);
				}
			}

		}
	}

	public void moveTank(long time) {
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				if (map[i][j].getType() > 1 && mTank.isIntersect(map[i][j])) {
					return;
				}
			}
		}
		audioMgr.getTankMove().setLoop(2);
		mTank.move(time);
	}

	private void addLife() {
		new Thread() {
			@Override
			public void run() {
				mTank.setX(0);
				mTank.setY(0);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				audioMgr.getAddLife().setLoop(1);
				mTank.setX(TANK_X);
				mTank.setY(TANK_Y);
			}
		}.start();
	}
	
	public void moveBulletOtherTank(long time) {
		for (int dem = 0; dem < listTank.size(); dem++) {
			
			for (int i = 0; i < listBulletOtherTank[dem].size(); i++) {
				Bullet bullet = listBulletOtherTank[dem].get(i);
				bullet.move(time);
				
				for (int j = 0; j < listBullet.size(); j++) {
					if(bullet.isIntersect(listBullet.get(j))) {
						listBulletOtherTank[dem].remove(bullet);
						if(dem>0) dem--;
						listBullet.remove(j--);
						audioMgr.getShoot().setLoop(1);
					}
				}
				
				if(bullet.isIntersect(mTank)) {

					this.life --;
					audioMgr.getExplode().setLoop(1);
					if(this.life == 0) {
						audioMgr.getGameOver().play();
						loose = true;
						return;
					}
					addLife();
				}
				
				// ban vao gach da hoac vao tuong
				Shape shape = getShapeBulletFireTo(bullet);
				if (shape != null) {
					if (bullet.isIntersect(shape)) {
						listBulletOtherTank[dem].remove(i--);
						if (shape.getType() < MAP_TYPE_ROCK) {
							shape.setImg(null);
							shape.setType(0);
						}
						if (shape.getType() == MAP_TYPE_KING) {
							loose = true;
						}
					}
				} else {
					if (bullet.getX() > EDGE_RIGHT_MAP + ICON_MAP_SIZE
							|| bullet.getY() > EDGE_BOTTOM_MAP
							|| bullet.getX() < EDGE_LEFT_MAP
							|| bullet.getY() < EDGE_TOP_MAP) {
						listBulletOtherTank[dem].remove(i--);
					}
				}
				
			}
			
		}
	}
	
	public boolean isLoose() {
		return loose;
	}
	
	public boolean isWin() {
		return win;
	}

	public void resetAll() {
		initObjects();
		win = false;
		loose = false;
		listBullet.removeAll(listBullet);
		for (int i = 0; i < 4; i++) {
			listBulletOtherTank[i].remove(listBulletOtherTank[i]);
		}
		
		countEnemies = 10;
		life = 3;
		score = 0;
	}

	public void tankOtherFire() {
		
		
		Random random = new Random();
		int temp = random.nextInt(150);
		if(temp != VALUE_WILL_FIRE) return;
		
		
		int demXeTang = 0;
		for (int i = 0; i < listTank.size(); i++) {
			Tank mTank = listTank.get(i);
			Bullet bullet = null;
			
			switch (mTank.getOrient()) {
			case Dad.LEFT:
					bullet = new Bullet(mTank.getX(), mTank.getY()
							+ (TANK_SIZE - BULLET_SIZE) / 2, BULLET_SIZE,
							mTank.getOrient(), imgBullets, BULLET_SPEED);
				break;
			case Dad.RIGHT:
					bullet = new Bullet(mTank.getX() + TANK_SIZE/2, mTank.getY()
							+ (TANK_SIZE - BULLET_SIZE) / 2, BULLET_SIZE,
							mTank.getOrient(), imgBullets, BULLET_SPEED
							);
				break;
			case Dad.UP:
				bullet = new Bullet(mTank.getX() + (TANK_SIZE - BULLET_SIZE)
						/ 2, mTank.getY(), BULLET_SIZE, mTank.getOrient(),
						imgBullets, BULLET_SPEED);
				break;
			case Dad.DOWN:
				bullet = new Bullet(mTank.getX() + (TANK_SIZE - BULLET_SIZE)
						/ 2, mTank.getY() + TANK_SIZE/2, BULLET_SIZE,
						mTank.getOrient(), imgBullets, BULLET_SPEED);
				break;
			}

			if(listBulletOtherTank[demXeTang].size() > NUMBER_OF_TANK) continue;
			listBulletOtherTank[demXeTang++].add(bullet);
			audioMgr.getShoot().setLoop(1);
		}
	}
	
	public void tankFire(long time) {
		if(listBullet.size() > 1) {
			return;
		}
		if (mTank.isFire()) {
			Bullet bullet = null;
			
			switch (mTank.getOrient()) {
			case Dad.LEFT:
				bullet = new Bullet(mTank.getX(), mTank.getY()
						+ (TANK_SIZE - BULLET_SIZE) / 2, BULLET_SIZE,
						mTank.getOrient(), imgBullets, BULLET_SPEED);
				break;
			case Dad.RIGHT:
				bullet = new Bullet(mTank.getX() + TANK_SIZE/2, mTank.getY()
						+ (TANK_SIZE - BULLET_SIZE) / 2, BULLET_SIZE,
						mTank.getOrient(), imgBullets, BULLET_SPEED
						);
				break;
			case Dad.UP:
				bullet = new Bullet(mTank.getX() + (TANK_SIZE - BULLET_SIZE)
						/ 2, mTank.getY(), BULLET_SIZE, mTank.getOrient(),
						imgBullets, BULLET_SPEED);
				break;
			case Dad.DOWN:
				bullet = new Bullet(mTank.getX() + (TANK_SIZE - BULLET_SIZE)
						/ 2, mTank.getY() + TANK_SIZE/2, BULLET_SIZE,
						mTank.getOrient(), imgBullets, BULLET_SPEED);
				break;
			}

			listBullet.add(bullet);
			audioMgr.getShoot().setLoop(1);
			mTank.setTimeOut();
		}
	}

	public void changeOrientTank(int newOrientTank) {
		mTank.changeOrient(newOrientTank);
	}

	public void downTimeOut(int value) {
		mTank.downTimeOut(value);
	}

	public AudioManager getAudioMgr() {
		return audioMgr;
	}

	public int getScore() {
		return score;
	}
	
}

package com.t3h.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.t3h.control.Manager;
import com.t3h.model.Dad;

public class PlayGamePanel extends BaseContainer {

	/** * */
	private static final long serialVersionUID = 1L;

	private boolean isRunThread = true;
	private Manager manager;
	private ShowMenuGamePanel gamePanel;

	private KeyAdapter keyAdapter;
	private Thread threadBullets;
	private long time;
	private BitSet mKeyValue;

	private JLabel gameOver;
	private JLabel win;

	@Override
	void initContainer() {
		setLayout(null);
		setBackground(Color.BLACK);
	}

	@Override
	void initComps() {
		mKeyValue = new BitSet(256);
		manager = new Manager();
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {
				mKeyValue.set(paramKeyEvent.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				mKeyValue.clear(e.getKeyCode());
			}
		};
		addKeyListener(keyAdapter);
		initThreadBullets();

	}

	private void excuteTaskByKeysPress() {

		if (mKeyValue.get(KeyEvent.VK_LEFT)) {
			manager.changeOrientTank(Dad.LEFT);
			manager.moveTank(time);
		}

		else if (mKeyValue.get(KeyEvent.VK_RIGHT)) {
			manager.changeOrientTank(Dad.RIGHT);
			manager.moveTank(time);
		}

		else if (mKeyValue.get(KeyEvent.VK_UP)) {
			manager.changeOrientTank(Dad.UP);
			manager.moveTank(time);
		}

		else if (mKeyValue.get(KeyEvent.VK_DOWN)) {
			manager.changeOrientTank(Dad.DOWN);
			manager.moveTank(time);
		}

		if (mKeyValue.get(KeyEvent.VK_ENTER)
				|| mKeyValue.get(KeyEvent.VK_SPACE)) {
			manager.tankFire(time);
		}
	}

	public void startThreadBullets() {
		if ( threadBullets != null && threadBullets.isAlive() ) {
			isRunThread = false;
			threadBullets.interrupt();
			
		}
		isRunThread = true;
		initThreadBullets();
		threadBullets.start();
		manager.getAudioMgr().getStart().stop();
		manager.getAudioMgr().getEnterGame().play();
	}

	private void initGameOver() {
		gameOver = new JLabel();
		ImageIcon icon = new ImageIcon(getClass().getResource(
				"/IMAGE/gameover.png").getPath());
		gameOver.setIcon(icon);
		gameOver.setBounds(300, 0, icon.getIconWidth(), icon.getIconHeight());
		this.add(gameOver);
	}

	private void initWin() {
		win = new JLabel();
		ImageIcon icon = new ImageIcon(getClass()
				.getResource("/IMAGE/pass.png").getPath());
		win.setIcon(icon);
		win.setBounds(300, 400, icon.getIconWidth(), icon.getIconHeight());
		this.add(win);
	}

	private void initThreadBullets() {
		threadBullets = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRunThread) {
					

					if (manager.isLoose()) {
						isRunThread = false;
						initGameOver();
						while (gameOver.getY() < 100) {
							try {
								Thread.sleep(30);
								gameOver.setLocation(gameOver.getX(),
										gameOver.getY() + 1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(PlayGamePanel.this,
								"Your Score: " + manager.getScore()
										+ "\nPress OK to contiue...");
						returnMenuGame();
						return;
					}

					if (manager.isWin()) {
						isRunThread = false;
						initWin();
						while (win.getY() > 300) {
							try {
								Thread.sleep(30);
								win.setLocation(win.getX(), win.getY() - 1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(PlayGamePanel.this,
								"Your Score: " + manager.getScore()
										+ "\nPress OK to contiue...");
						returnMenuGame();
						return;
					}

					try {
						time = System.currentTimeMillis();
						excuteTaskByKeysPress();
						manager.moveBullets(time);
						manager.tankOtherFire();
						manager.moveBulletOtherTank(time);
						manager.moveOtherTank(time);
//						repaint();
						Thread.sleep(10);
						manager.downTimeOut(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		manager.drawObjects(g2d);
	}

	public boolean isRunThread() {
		return isRunThread;
	}


	private void returnMenuGame() {
		manager.resetAll();
		time = 0;
		if(win != null) {
			win.setIcon(null);
		}
		if(gameOver != null) {
			gameOver.setIcon(null);
		}
		gamePanel.showMenuGame();
		getParent().getComponent(Integer.parseInt(MyContainer.MENU_ID))
				.requestFocus();
	}

	public void setGamePanel(ShowMenuGamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

}

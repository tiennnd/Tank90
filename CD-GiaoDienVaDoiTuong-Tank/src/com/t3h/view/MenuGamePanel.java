package com.t3h.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.t3h.control.ExitGame;
import com.t3h.control.PlayGame;
import com.t3h.control.ShowPlayGamePanel;

public class MenuGamePanel extends BaseContainer implements ActionListener{

	/** *  */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_BUTTON = 330;
	private static final int HEIGHT_BUTTON = 80;
	private static final String PLAY_GAME = "PLAY_GAME";
	private static final String EXIT_GAME = "EXIT_GAME";

	private JButton btPlayGame;
	private JButton btExitGame;
	private ShowPlayGamePanel showPlayGamePanel;
	private ExitGame exitGame;
	private PlayGame playGame;

	@Override
	void initContainer() {
		setBackground(Color.BLUE);
		setLayout(null);
	}

	public void setPlayGame(PlayGame playGame) {
		this.playGame = playGame;
	}

	@Override
	void initComps() {
		initPlayGame();
		initExitGame();
	}
	
	private void initPlayGame() {
		btPlayGame = new JButton();
		
		btPlayGame.setActionCommand(PLAY_GAME);
		btPlayGame.setBounds(600, 70, WIDTH_BUTTON, HEIGHT_BUTTON);
		
		btPlayGame.setBorderPainted (false);
		btPlayGame.setContentAreaFilled (false);
		btPlayGame.setFocusPainted (false);
		btPlayGame.addActionListener(this);
		
		add(btPlayGame);
	}

	private void initExitGame() {
		
		btExitGame = new JButton();
		btExitGame.setActionCommand(EXIT_GAME);
		btExitGame.setBounds(600, 220, WIDTH_BUTTON, HEIGHT_BUTTON);
		
		btExitGame.setBorderPainted (false);
		btExitGame.setContentAreaFilled (false);
		btExitGame.setFocusPainted (false);
		btExitGame.addActionListener(this);

		this.add(btExitGame);
	}
	

	public void setPlayMenu(ShowPlayGamePanel showPlayGamePanel) {
		this.showPlayGamePanel = showPlayGamePanel;
	}

	public void setExitGame(ExitGame exitGame) {
		this.exitGame = exitGame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case PLAY_GAME:
			showPlayGamePanel.showPlayGame();
			playGame.play();
			getParent().getComponent(Integer.parseInt(MyContainer.PLAY_ID)).requestFocus();
			break;
		case EXIT_GAME:
			exitGame.closeGame();
			break;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		Image image = new ImageIcon(getClass().getResource("/IMAGE/IMG_0001.PNG")).getImage();
		
		g2d.drawImage(image, 0,0 , GUI.FRAME_WIDTH, GUI.FRAME_HEIGHT, this);
	}
	
}

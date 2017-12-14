package com.t3h.view;

import java.awt.CardLayout;
import java.awt.Color;

import com.t3h.control.PlayGame;
import com.t3h.control.ShowPlayGamePanel;

public class MyContainer extends BaseContainer implements ShowPlayGamePanel, PlayGame, ShowMenuGamePanel{

	/** *  */
	private static final long serialVersionUID = 1L;

	public static final String MENU_ID = "0";
	public static final String PLAY_ID = "1";
	
	private PlayGamePanel playGamePanel;
	private MenuGamePanel menuGamePanel;
	private CardLayout mCard;

	@Override
	void initContainer() {
		mCard = new CardLayout();
		setLayout(mCard);
		setBackground(Color.WHITE);
	}

	@Override
	void initComps() {
		menuGamePanel = new MenuGamePanel();
		playGamePanel = new PlayGamePanel();
		
		menuGamePanel.setPlayMenu(this);
		menuGamePanel.setPlayGame(this);
		playGamePanel.setGamePanel(this);
		
		this.add(menuGamePanel,MENU_ID);
		this.add(playGamePanel,PLAY_ID);
	}

	@Override
	public void showMenuGame() {
		mCard.show(MyContainer.this, MENU_ID);
		MyContainer.this.requestFocus();
	}
	
	@Override
	public void showPlayGame() {
		mCard.show(MyContainer.this, PLAY_ID);
		MyContainer.this.requestFocus();
	}

	public MenuGamePanel getMenuGame() {
		return menuGamePanel;
	}

	@Override
	public void play() {
		playGamePanel.startThreadBullets();
	}

}

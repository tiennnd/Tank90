package com.t3h.view;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.t3h.control.ExitGame;

public class GUI extends JFrame implements ExitGame{

	/** *  */
	private static final long serialVersionUID = 1L;
	public static final int FRAME_WIDTH = 920;
	public static final int FRAME_HEIGHT = 590;
	private BaseContainer myContainer;
	private WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			int result = JOptionPane.showConfirmDialog(GUI.this, "Do you want to close game?", "Comfirm", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				closeGame();
			}
		}
	} ;
	
	public GUI() {
		initContainer();
		initComps();
	}
	private void initContainer() {
		setTitle("Brick City");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new CardLayout());
		setLocationRelativeTo(null);
		setResizable(false);
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/IMAGE/icon.png").getPath());
		setIconImage(icon.getImage());
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	private void initComps() {
		addWindowListener(windowAdapter);
		myContainer = new MyContainer();
		((MyContainer)myContainer).getMenuGame().setExitGame(this);
		this.add(myContainer);
	}
	@Override
	public void closeGame() {
		System.exit(0);
	}

}

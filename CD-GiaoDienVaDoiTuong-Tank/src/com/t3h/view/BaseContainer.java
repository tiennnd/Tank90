package com.t3h.view;

import java.awt.Font;

import javax.swing.JPanel;

public abstract class BaseContainer extends JPanel{

	/** *  */
	private static final long serialVersionUID = 1L;
	public static Font font = new Font("Tahoma", Font.PLAIN, 20);

	public BaseContainer() {
		initContainer();
		initComps();
	}

	abstract void initContainer();
	abstract void initComps();
}

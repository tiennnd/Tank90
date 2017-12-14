package com.t3h.control;

public class AudioManager {

	private AudioEffect enterGame;
	private AudioEffect shootStone;
	private AudioEffect tankMove;
	private AudioEffect shoot;
	private AudioEffect explode;
	private AudioEffect gameOver;
	private AudioEffect win;
	private AudioEffect hitblock;
	private AudioEffect addLife;
	private AudioEffect start;

	public AudioManager() {
		enterGame = new AudioEffect("enterGame.wav");
		start = new AudioEffect("tankbrick.wav");
		shootStone = new AudioEffect("shootStone.wav");
		tankMove = new AudioEffect("move.wav");
		shoot = new AudioEffect("shoot.wav");
		explode = new AudioEffect("explode.wav");
		gameOver = new AudioEffect("gameover.wav");
		win = new AudioEffect("win.wav");
		hitblock = new AudioEffect("hitblock.wav");
		addLife = new AudioEffect("addlife.wav");
	}

	public AudioEffect getHitblock() {
		return hitblock;
	}

	public AudioEffect getStart() {
		return start;
	}

	public AudioEffect getGameOver() {
		return gameOver;
	}

	public AudioEffect getWin() {
		return win;
	}

	public AudioEffect getExplode() {
		return explode;
	}

	public AudioEffect getEnterGame() {
		return enterGame;
	}

	public AudioEffect getShootStone() {
		return shootStone;
	}

	public AudioEffect getTankMove() {
		return tankMove;
	}

	public AudioEffect getShoot() {
		return shoot;
	}

	public void playBG() {
		shootStone.play();
	}

	public AudioEffect getAddLife() {
		return this.addLife;
	}

}

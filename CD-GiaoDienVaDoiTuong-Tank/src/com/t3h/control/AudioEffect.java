package com.t3h.control;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioEffect implements LineListener {
	private Clip mClip;
	private boolean isDone;
	
	public AudioEffect(String audioName) {
		URL url = getClass().getResource("/SOUND/"+audioName);
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			mClip = AudioSystem.getClip();
			mClip.open(audioInputStream);
			mClip.addLineListener(this);
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(mClip.isRunning()) {
			return;
		}
		
		new Thread() {
			public void run() {
				mClip.start();
				while (!isDone) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	public void stop() {
		mClip.stop();
		mClip.close();
	}
	
	public void setLoop(int count) {
		mClip.loop(count);
	}

	@Override
	public void update(LineEvent event) {
		if(event.getType() == LineEvent.Type.CLOSE ||event.getType() == LineEvent.Type.STOP) {
			isDone = true;
		}
		
	}
	
}

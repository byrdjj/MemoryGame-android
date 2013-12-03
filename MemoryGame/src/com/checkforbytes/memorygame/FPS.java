package com.checkforbytes.memorygame;

import com.badlogic.gdx.utils.TimeUtils;

public class FPS {
	
	private String fps;
	private int fpsCounter;
	private long fpsTimer;
	
	public FPS() {
		fps = "0";
		fpsTimer = 0;
		fpsCounter = 0;
	}
	
	public void count() {
		fpsCounter++;
		if(TimeUtils.nanoTime() - fpsTimer > 1000000000) {
			fps = Integer.toString(fpsCounter);
			fpsCounter = 0;
			fpsTimer = TimeUtils.nanoTime();
		}
	}
	
	public String getFPS() {
		return fps;
	}
	
}

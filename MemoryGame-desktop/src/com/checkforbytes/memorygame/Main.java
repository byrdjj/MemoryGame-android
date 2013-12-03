package com.checkforbytes.memorygame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MemoryGame";
		cfg.useGL20 = false;
		cfg.width = 540;
		cfg.height = 960;
		
		new LwjglApplication(new MemoryGame(), cfg);
	}
}

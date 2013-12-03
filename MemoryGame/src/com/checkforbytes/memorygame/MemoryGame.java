package com.checkforbytes.memorygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MemoryGame extends Game {
	
	SpriteBatch batch;
	BitmapFont font;
	
	FPS fps;
	
	@Override
	public void create() {		
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setScale(2.0f);
		
		fps = new FPS();
		
		// this.setScreen(new SplashScreen(this));  // Comment-in to enable splash screen. TODO figure out how to display only once
		// this.setScreen(new MainMenuScreen(this));
		this.setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
}

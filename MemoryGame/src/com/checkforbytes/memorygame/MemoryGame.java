package com.checkforbytes.memorygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

public class MemoryGame extends Game {
	GameGestureListener input;
	
	SpriteBatch batch;
	BitmapFont font;
	
	boolean soundOn = true;
	boolean musicOn = true;

	FPS fps;
	
	@Override
	public void create() {
		input = new GameGestureListener();
		Gdx.input.setInputProcessor(new GestureDetector(input));

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
		
		this.getScreen().dispose();
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
		this.getScreen().pause();
	}

	@Override
	public void resume() {
		this.getScreen().resume();
	}
	
}

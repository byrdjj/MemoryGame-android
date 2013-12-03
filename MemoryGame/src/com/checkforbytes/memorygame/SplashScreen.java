package com.checkforbytes.memorygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashScreen implements Screen {
	
	final MemoryGame game;
	OrthographicCamera camera;
	
	final float w = 1080;
	final float h = 1920;
	
	float fade = 1.0f;
	
	Texture texture;
	TextureRegion region;
	Sprite sprite;
	
	private float splashTimer;
	
	public SplashScreen(final MemoryGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 1920);
		
		splashTimer = 0;
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setPosition(w / 2 - sprite.getWidth() / 2, h / 2 - sprite.getHeight() / 2);
		sprite.setScale((w / sprite.getWidth()) * 0.95f);
	}
	
	@Override
	public void render(float delta) {
		splashTimer += delta;
		if(splashTimer > 3.1f) {
			game.setScreen(new MainMenuScreen(game));
			dispose();
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		if(splashTimer > 2.0 && splashTimer < 3.0f) {
			fade = (3.0f - splashTimer) * (3.0f - splashTimer);		// Fader could be cleaned up. Variables instead of hard-coded numbers.
		} else if(splashTimer > 3.0f) {
				fade = 0.0f;
			} else {
				fade = 1.0f;
			}
		
		sprite.draw(game.batch, fade);
		
		game.batch.end();	
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

}

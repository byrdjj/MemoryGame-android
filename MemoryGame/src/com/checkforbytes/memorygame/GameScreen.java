package com.checkforbytes.memorygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen {
	
	final MemoryGame game;
	OrthographicCamera camera;

	final float w = 1080;
	final float h = 1920;
	
	private int gameState;
	static final int GAME_PAUSED = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_READY = 2;
	static final int GAME_WON = 3;
	
	private Texture background;					// TODO Consolidate to an Assets class?
	private TextureRegion backgroundRegion;
	private TextureRegion pauseDimRegion;
	private TextureRegion readyRegion;
	
	public static Texture cardTextures;
	
	public Sound cardflip;
	public Sound match;
	public Sound nomatch;
	
	
	GameBoard board;
	
	public GameScreen(final MemoryGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 1920);
		
		background = new Texture(Gdx.files.internal("data/background.png"));
		backgroundRegion = new TextureRegion(background, 0, 128, 1080, 1920);
		pauseDimRegion = new TextureRegion(background, 0, 0, 9, 16);
		readyRegion = new TextureRegion(background, 1333, 1911, 714, 136);
		
		cardTextures = new Texture(Gdx.files.internal("data/animals.png"));			// Is the place to switch between "card sets"
		
		cardflip = Gdx.audio.newSound(Gdx.files.internal("data/cardflip.wav"));
		match = Gdx.audio.newSound(Gdx.files.internal("data/match.wav"));
		nomatch = Gdx.audio.newSound(Gdx.files.internal("data/nomatch.wav"));
		
		board = new GameBoard(this, 4, 5);
	}
	
	public void setState(int s) {
		gameState = s;
	}
	
	@Override
	public void render(float delta) {
		
		if(gameState == GAME_PAUSED || gameState == GAME_READY) {
			if(game.ggl.isTapped()) {													// TODO Change this to a start/resume button
				setState(GAME_RUNNING);
			}
		}
		
		if(gameState == GAME_RUNNING) {
			board.update(delta);
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.disableBlending();
		game.batch.draw(backgroundRegion, 0, 0);
		for(Card card: board.cards) {
			game.batch.draw(card.region, card.position.x, card.position.y);				// TODO Could add match/no match icons, etc.
		}
		game.batch.enableBlending(); 													
		if(gameState == GAME_PAUSED || gameState == GAME_READY) {
			game.batch.draw(pauseDimRegion, 0, 0, w, h);								
			game.batch.draw(readyRegion, w / 2 - 714 / 2 , h / 2 - 136 / 2);			// TODO Change to drawing a ready OR paused message/button
		}
		// TODO Insert sound/music on/off button drawing here. Do pause button also?
		game.font.draw(game.batch, game.fps.getFPS(), 0, h);
		game.batch.end();
		
		game.fps.count();
		
	}

	public void playSound(Sound sound) {
		sound.play();
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
		setState(GAME_PAUSED);
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// Textures
		background.dispose();
		cardTextures.dispose();
		
		// Sounds
		cardflip.dispose();
		match.dispose();
		nomatch.dispose();
	}

}

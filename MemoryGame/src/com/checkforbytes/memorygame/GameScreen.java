package com.checkforbytes.memorygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

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
	private TextureRegion soundOnRegion;
	private TextureRegion soundOffRegion;
	private TextureRegion musicOnRegion;
	private TextureRegion musicOffRegion;
	
	private Circle soundButton;
	private Circle musicButton;
	
	public static Texture cardTextures;
	
	public Sound cardflip;
	public Sound match;
	public Sound nomatch;
	
	private Music music;
	
	private Vector3 touchPos = new Vector3();
	
	GameBoard board;
	
	public GameScreen(final MemoryGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 1920);
		
		background = new Texture(Gdx.files.internal("data/textures/background.png"));
		backgroundRegion = new TextureRegion(background, 0, 128, 1080, 1920);
		pauseDimRegion = new TextureRegion(background, 0, 0, 9, 16);
		readyRegion = new TextureRegion(background, 1333, 1911, 714, 136);
		soundOnRegion = new TextureRegion(background, 1792, 0, 256, 256);
		soundOffRegion = new TextureRegion(background, 1792, 255, 256, 256);
		musicOnRegion = new TextureRegion(background, 1792, 511, 256, 256);
		musicOffRegion = new TextureRegion(background, 1792, 767, 256, 256);
		
		soundButton = new Circle(127, 127, 128);
		musicButton = new Circle(951, 127, 128);
		
		cardTextures = new Texture(Gdx.files.internal("data/textures/animals.png"));			// Is the place to switch between "card sets"
		
		cardflip = Gdx.audio.newSound(Gdx.files.internal("data/sounds/cardflip.wav"));
		match = Gdx.audio.newSound(Gdx.files.internal("data/sounds/match.wav"));
		nomatch = Gdx.audio.newSound(Gdx.files.internal("data/sounds/nomatch.wav"));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("data/music/happybee.mp3"));
		music.setVolume(0.25f);
		
		if(game.musicOn) {
			music.play();
		}
		
		board = new GameBoard(this, 4, 5);
	}
	
	public void setState(int s) {
		gameState = s;
	}
	
	@Override
	public void render(float delta) {
		
		// TODO Add a UI update method
		
		if(game.ggl.isTapped()) {
			touchPos.set(game.ggl.location);
			camera.unproject(touchPos);
						
			if(soundButton.contains(touchPos.x, touchPos.y)) {
				game.soundOn = !game.soundOn;
				game.ggl.consume();
			}
			
			if(musicButton.contains(touchPos.x, touchPos.y)) {
				game.musicOn = !game.musicOn;
				
				if(game.musicOn) {
					music.play();
				} else {
					music.pause();
				}
				
				game.ggl.consume();
			}
			
		}
		
		if(gameState == GAME_PAUSED || gameState == GAME_READY) {
			if(game.ggl.isTapped()) {													// TODO Change this to a start/resume button
				game.ggl.consume();
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
		game.batch.draw((game.soundOn ? soundOnRegion : soundOffRegion), 0, 0);
		game.batch.draw((game.musicOn ? musicOnRegion : musicOffRegion), w - 256, 0);
		game.font.draw(game.batch, game.fps.getFPS(), 0, h);
		game.batch.end();
		
		game.fps.count();
		
	}

	public void playSound(Sound sound) {
		if(game.soundOn) {
			sound.play();
		}
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

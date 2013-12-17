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
	private TextureRegion pausedRegion;
	private TextureRegion wonRegion;
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
	
	TimeCountDigits tcd;
	
	public GameScreen(final MemoryGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 1920);
		
		background = new Texture(Gdx.files.internal("data/textures/background.png"));
		backgroundRegion = new TextureRegion(background, 0, 128, 1080, 1920);
		pauseDimRegion = new TextureRegion(background, 1071, 0, 9, 16);
		readyRegion = new TextureRegion(background, 1333, 1911, 714, 136);
		pausedRegion = new TextureRegion(background, 1333, 1774, 714, 136);
		wonRegion = new TextureRegion(background, 1333, 1637, 714, 136);
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
			music.setLooping(true);
		}
		
		board = new GameBoard(this, 4, 5);
		tcd = new TimeCountDigits(background, board);
	}
	
	public void setState(int s) {
		gameState = s;
	}
	
	@Override
	public void render(float delta) {
		
		// TODO Separate the states into a switch block
		// TODO Make a GameBoard.render() method
		// TODO Add a UI update method
		
		if(game.input.isTapped()) {
			touchPos.set(game.input.location);
			camera.unproject(touchPos);
						
			if(soundButton.contains(touchPos.x, touchPos.y)) {
				game.soundOn = !game.soundOn;
				game.prefs.putBoolean("soundOn", game.soundOn);
				game.prefs.flush();
				
				game.input.consume();
			}
			
			if(musicButton.contains(touchPos.x, touchPos.y)) {
				game.musicOn = !game.musicOn;
				game.prefs.putBoolean("musicOn", game.musicOn);
				game.prefs.flush();
				
				if(game.musicOn) {
					music.play();
					music.setLooping(true);
				} else {
					music.pause();
				}
				
				game.input.consume();
			}
			
		}
		
		if(gameState != GAME_RUNNING) {
			if(game.input.isTapped()) {													// TODO Change this to a start/resume button
				game.input.consume();
				
				if(gameState == GAME_WON) {
					board.reset();
				}
				setState(GAME_RUNNING);
				board.startTimer();
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
		// game.batch.disableBlending();
		
		game.batch.draw(backgroundRegion, 0, 0);
		
		for(Card card: board.cards) {
			game.batch.draw(card.region, card.position.x, card.position.y);				// TODO Could add match/no match icons, etc.
		}
		
		// game.batch.enableBlending();
		
		drawTimer();
		
		if(gameState != GAME_RUNNING) {
			game.batch.draw(pauseDimRegion, 0, 0, w, h);

			switch(gameState) {
				case GAME_PAUSED:
					game.batch.draw(pausedRegion, w / 2 - 714 / 2 , h / 2 - 136 / 2);			// TODO Change to a paused message/button
					break;
				case GAME_READY:
					game.batch.draw(readyRegion, w / 2 - 714 / 2 , h / 2 - 136 / 2);			// TODO Change to drawing a ready message/button
					break;
				case GAME_WON:
					game.batch.draw(wonRegion, w / 2 - 714 / 2 , h / 2 - 136 / 2);			// TODO Change to drawing a won & replay message/button
					break;
			}
			
		}
		
		game.batch.draw((game.soundOn ? soundOnRegion : soundOffRegion), 0, 0);
		game.batch.draw((game.musicOn ? musicOnRegion : musicOffRegion), w - 256, 0);
		
		game.batch.flush();
		Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
		Gdx.gl10.glAlphaFunc(GL10.GL_GREATER, 0.5f);
		game.font.draw(game.batch, game.fps.getFPS(), 0, h);
		game.batch.flush();
		Gdx.gl10.glDisable(GL10.GL_ALPHA_TEST);
		
		game.batch.end();
		
		game.fps.count();
		
	}
	
	private void drawTimer() {
		// First digit, minutes
		game.batch.draw(tcd.getDigitRegion(board.timeDigits.get(0)), 0, h - 192);
		
		// Minutes:Seconds colon
		game.batch.draw(tcd.getDigitRegion(tcd.COLON), 104, h - 192);
		
		// Second digit, seconds tens
		game.batch.draw(tcd.getDigitRegion(board.timeDigits.get(1)), 170, h - 192);
		
		// Third digit, seconds ones
		game.batch.draw(tcd.getDigitRegion(board.timeDigits.get(2)), 274, h - 192);
		
		// Seconds.tenths of Seconds period
		game.batch.draw(tcd.getDigitRegion(tcd.PERIOD), 378, h - 192);
		
		// Fourth digit, seconds tenths
		game.batch.draw(tcd.getDigitRegion(board.timeDigits.get(3)), 444, h - 192);
		
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
		if(gameState == GAME_RUNNING) {
			setState(GAME_PAUSED);
		}
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
		
		// Music
		music.dispose();
	}

}

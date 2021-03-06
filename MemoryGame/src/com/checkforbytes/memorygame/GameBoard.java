package com.checkforbytes.memorygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.TimeUtils;

public class GameBoard {
	
	final GameScreen screen;

	final int COLUMNS;
	final int ROWS;
	final int nCards;
	
	private Vector3 touchPos = new Vector3();
	
	private boolean touchable = true;
	private int flipped = 0;
	
	private int nMatched = 0;
	
	Array<Rectangle> positions;
	Array<Rectangle> matchedPositions;
	Array<Rectangle> nonmatchPositions;
	
	Array<Card> cards;
	private Card testA;
	private Card testB;

	private long elapsedTime = 0;
	private long startTime = 0;
	public IntArray timeDigits;
	public String timer;
	
	public GameBoard(GameScreen screen, int columns, int rows) {
		this.screen = screen;
		this.COLUMNS = columns;
		this.ROWS = rows;

		nCards = COLUMNS * ROWS;
		cards = new Array<Card>(nCards);
		
		matchedPositions = new Array<Rectangle>(nCards);
		
		generatePositions();
		generateCards();
		
		timeDigits = new IntArray(4);
		for(int i = 0; i < 4; i++) {
			timeDigits.add(0);
		}
		
		screen.setState(GameScreen.GAME_READY);
	}
	
	public void render() {
		
	}
	
	public void update(float delta) {
		// Increment the game timer. If-else keeps the upper bound at 9:59.9 (10 minutes)
		if(elapsedTime < 599900) {
			elapsedTime = TimeUtils.millis() - startTime;
		} else {
			elapsedTime = 599900;
		}
		gameTimeMethodA();
		
		// Run decay timer for unmatched cards to flip back over (or show animation)
		for(Card card: cards) {
			card.decay(delta);
		}
		
		// Test if flipped cards have decayed. If so, enable touching
		if(flipped == 2) {
			
			if(testA.cardState == Card.DOWN && testB.cardState == Card.DOWN) {
				touchable = true;
				flipped = 0;
			}
			
		}
		
		// Check if a down card was touched. Flip it over and add it to the temporary card holders for match testing
		if(touchable && screen.game.input.isTapped()) {								// TODO Change to being a tap event?
			touchPos.set(screen.game.input.location);
			screen.camera.unproject(touchPos);
			
			for(Card card: cards) {
				if(card.cardState == Card.DOWN && card.position.contains(touchPos.x, touchPos.y)) {
					screen.game.input.consume();
					card.flip();
					flipped++;
					
					switch(flipped) {
						case 1: 	testA = card;
									screen.playSound(screen.cardflip);
									break;
						case 2: 	testB = card;
									touchable = false;
									runMatchTest();
									break;
					}
					
					break;
				}

			}
			
		}
		
		if(nMatched == nCards) {
			screen.game.highscores.submitScore(elapsedTime);
			Gdx.app.log("MemoryGame", (screen.game.highscores.highscores.toString()));
			screen.setState(GameScreen.GAME_WON);
		}
		
	}
	
	public void reset() {
		reassignCards();
		
		touchable = true;
		flipped = 0;
		nMatched = 0;
		elapsedTime = 0;
	}
	
	private void runMatchTest() {
		
		if(testA.cardID == testB.cardID) {
			testA.setMatched(); testB.setMatched();
			matchedPositions.add(testA.position); matchedPositions.add(testB.position);
			nMatched += 2;
			flipped = 0;
			touchable = true;
			screen.playSound(screen.match);
		} else {
			testA.startDecay(); testB.startDecay();
			screen.playSound(screen.nomatch);
		}
		
	}
	
	private void generatePositions() {
		
		Rectangle pos;
		positions = new Array<Rectangle>();
		
		/*
		float offset = (screen.w - 256 * COLUMNS) / (COLUMNS + 1);								// TODO Change to dynamic texture width
		float yStart = (screen.h / 2) + (offset / 2) * (ROWS - 1) + (256 / 2) * (ROWS - 2); 	// Change to scale y-offset independently? 
		
		// Generate the array of Rectangle card positions
		for(int r = 0; r < ROWS; r++) {
		
			for(int c = 0; c < COLUMNS; c++) {
				pos = new Rectangle();
				pos.x = offset + (offset + 256) * c;									// TODO Change to dynamic texture width
				pos.y = yStart;
				pos.width = 256; 														// TODO Change to dynamic texture width
				pos.height = 256; 														// TODO Change to dynamic texture height
				positions.add(pos);
			}
			
			yStart -= offset + 256; 													// TODO change texture dimensions stored in local variables to avoid repeated method calls
		}
		*/
		
		float xOffset = (screen.w - 256 * COLUMNS) / (COLUMNS + 1);								// TODO Change to dynamic texture width
		float yOffset = (1408 - 256 * ROWS) / (ROWS + 1);
		
		// Generate the array of Rectangle card positions
		for(int r = 0; r < ROWS; r++) {
			
			for(int c = 0; c < COLUMNS; c++) {
				pos = new Rectangle();
				pos.x = xOffset + (xOffset + 256) * c;									// TODO Change to dynamic texture width
				pos.y = 1408 - yOffset - (yOffset + 256) * r;
				pos.width = 256; 														// TODO Change to dynamic texture width
				pos.height = 256; 														// TODO Change to dynamic texture height
				positions.add(pos);
			}
			
		}
	}
	
	private void generateCards() {
		// TODO Currently hard-coded for a set of 12 cards.
		
		// Create an array of random numbers, 1/2 nCards
		IntArray ids = new IntArray(false, nCards);
		int i = 0;
		int r;
		while(i < nCards / 2) {					// ONLY WORKS FOR EVEN NUMBER OF CARDS!		
			r = MathUtils.random(1, 12);		// Should be 1, #cards in set
			if(!ids.contains(r)) {
				ids.add(r);
				i++;
				continue;
			} else {
				continue;
			}
		}
		
		// Double the array so there is one pair of each card. Shuffle for randomization.
		int length = ids.size;
		for(i = 0; i < length; i++) {
			ids.add(ids.get(i));
		}
		ids.shuffle();
		
		// Create the cards
		Card tCard;
		for(i = 0; i < nCards; i++) {
			tCard = new Card(ids.get(i), positions.get(i));
			cards.add(tCard);
		}
	}
	
	private void reassignCards() {
		// TODO Currently hard-coded for a set of 12 cards.
		
		// Create an array of random numbers, 1/2 nCards
		IntArray ids = new IntArray(false, nCards);
		int i = 0;
		int r;
		while(i < nCards / 2) {					// ONLY WORKS FOR EVEN NUMBER OF CARDS!		
			r = MathUtils.random(1, 12);		// Should be 1, #cards in set
			if(!ids.contains(r)) {
				ids.add(r);
				i++;
				continue;
			} else {
				continue;
			}
		}
		
		// Double the array so there is one pair of each card. Shuffle for randomization.
		int length = ids.size;
		for(i = 0; i < length; i++) {
			ids.add(ids.get(i));
		}
		ids.shuffle();
		
		int j = 0;
		for(Card card: cards) {
			card.reset(ids.get(j));
			j++;
		}
	}
	
	public void startTimer() {	
		if(elapsedTime == 0) {
			startTime = TimeUtils.millis();
		} else {
			startTime = TimeUtils.millis() - elapsedTime;
		}
	}
	
	private void gameTimeMethodA() {
		int minutes = MathUtils.floor((elapsedTime / 60000));
		int seconds = MathUtils.floor((elapsedTime % 60000) / 1000);
		int tenthSeconds = MathUtils.floor(((elapsedTime % 60000) % 1000) / 100);
		
		timeDigits.set(0, minutes);
		
		if(seconds > 9) {
			timeDigits.set(1, ((int) MathUtils.floor(seconds / 10)));
			timeDigits.set(2, seconds % 10);
		} else {
			timeDigits.set(1,  0);
			timeDigits.set(2, seconds);
		}
		
		timeDigits.set(3, tenthSeconds);
		
		timer = (Integer.toString(timeDigits.get(0)) + ':' + Integer.toString(timeDigits.get(1)) + Integer.toString(timeDigits.get(2)) + '.' + Integer.toString(timeDigits.get(3)));
		// Gdx.app.log("MemoryGame", timer);
	}
	
}

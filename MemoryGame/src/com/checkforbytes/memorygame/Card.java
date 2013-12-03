package com.checkforbytes.memorygame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Card {
	
	final int cardID;
	
	int cardState;
	static final int DOWN = 0;
	static final int FLIPPED = 1;
	static final int MATCHED = 2;
	
	static private final float DECAY_TIME = 0.5f;
	private float decay = 0.0f;
	
	Rectangle position;
	
	TextureRegion region;
	TextureRegion regionID;
	static TextureRegion down = new TextureRegion(GameScreen.cardTextures, 768, 0, 256, 256);		// TODO Draw a better texture in the image map;
	
	public Card(int ID, Rectangle pos) {
		this.cardID = ID;
		this.position = pos;
		
		cardState = DOWN;
		assignRegionID();
		
		setRegion();
	}
	
	public Card() {
		cardID = -1;
		cardState = DOWN;
		down = null;
	}
	
	public void flip() {
		cardState = FLIPPED;
		setRegion();
	}
	
	public void setMatched() {
		cardState = MATCHED;
		// setRegion(); // May be necessary? If so it is likely be a bad workaround to a bug elsewhere
	}
	
	public void setDown() {
		cardState = DOWN;
		setRegion();
	}
	
	public void startDecay() {
		if(cardState == FLIPPED) {
			decay = DECAY_TIME;
		}
	}
	
	public void decay(float delta) {
		if(decay > 0.0f) {
			decay -= delta;
			if(decay <= 0.0f && cardState == FLIPPED) {
				setDown();
			}
		}
		
	}
	
	private void setRegion() {
		region = (cardState == DOWN ? down : regionID);
	}
	
	private void assignRegionID() {				// TODO Change to being static fields to save memory?
		switch(cardID) {
			case 1: 	regionID = new TextureRegion(GameScreen.cardTextures, 0, 0, 256, 256);			// Dog
						break;
			case 2: 	regionID = new TextureRegion(GameScreen.cardTextures, 256, 0, 256, 256);		// Owl
						break;
			case 3: 	regionID = new TextureRegion(GameScreen.cardTextures, 0, 256, 256, 256);		// Cow
						break;
			case 4: 	regionID = new TextureRegion(GameScreen.cardTextures, 256, 256, 256, 256);		// Goat
						break;
			case 5: 	regionID = new TextureRegion(GameScreen.cardTextures, 0, 512, 256, 256);		// Beaver
						break;
			case 6: 	regionID = new TextureRegion(GameScreen.cardTextures, 256, 512, 256, 256);		// Elephant
						break;
			case 7: 	regionID = new TextureRegion(GameScreen.cardTextures, 512, 512, 256, 256);		// Penguin
						break;
			case 8: 	regionID = new TextureRegion(GameScreen.cardTextures, 768, 512, 256, 256);		// Sheep
						break;
			case 9: 	regionID = new TextureRegion(GameScreen.cardTextures, 0, 768, 256, 256);		// Bear
						break;
			case 10: 	regionID = new TextureRegion(GameScreen.cardTextures, 256, 768, 256, 256);		// Cat
						break;
			case 11: 	regionID = new TextureRegion(GameScreen.cardTextures, 512, 768, 256, 256);		// Elk
						break;
			case 12: 	regionID = new TextureRegion(GameScreen.cardTextures, 768, 768, 256, 256);		// Gnu
						break;
		}
	}

}

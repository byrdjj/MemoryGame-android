package com.checkforbytes.memorygame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TimeCountDigits {
	
	Texture background;
	GameBoard board;
	
	public final int COLON = 10;
	public final int PERIOD = 11;
	
	public static TextureRegion one;
	public static TextureRegion two;
	public static TextureRegion three;
	public static TextureRegion four;
	public static TextureRegion five;
	public static TextureRegion six;
	public static TextureRegion seven;
	public static TextureRegion eight;
	public static TextureRegion nine;
	public static TextureRegion zero;
	public static TextureRegion colon;
	public static TextureRegion period;
	
	
	TimeCountDigits(Texture background, GameBoard board) {
		this.background = background;
		this.board = board;
		
		assignRegions();
	}
	
	public TextureRegion getDigitRegion(int d) {
		switch(d) {
			case 0:
				return zero;
			case 1:
				return one;
			case 2:
				return two;
			case 3:
				return three;
			case 4:
				return four;
			case 5:
				return five;
			case 6:
				return six;
			case 7:
				return seven;
			case 8:
				return eight;
			case 9:
				return nine;
			case COLON:
				return colon;
			case PERIOD:
				return period;
		}
		return zero;
	}
	
	private void assignRegions() {
		one = new TextureRegion(background, 0, 0, 104, 128);
		two = new TextureRegion(background, 104, 0, 104, 128);
		three = new TextureRegion(background, 208, 0, 104, 128);
		four = new TextureRegion(background, 312, 0, 104, 128);
		five = new TextureRegion(background, 416, 0, 104, 128);
		six = new TextureRegion(background, 520, 0, 104, 128);
		seven = new TextureRegion(background, 624, 0, 104, 128);
		eight = new TextureRegion(background, 728, 0, 104, 128);
		nine = new TextureRegion(background, 832, 0, 104, 128);
		zero = new TextureRegion(background, 936, 0, 104, 128);
		colon = new TextureRegion(background, 1191, 1337, 66, 128);
		period = new TextureRegion(background, 1191, 1390, 66, 66);
	}
	
}

package com.checkforbytes.memorygame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.LongArray;

public class Highscores {
	
	Preferences prefs;
	LongArray highscores;
	
	public Highscores(Preferences prefs) {
		this.prefs = prefs;
		
		highscores = new LongArray(5);
		
		highscores.set(0, prefs.getLong("highscore1", 599900));
		highscores.set(1, prefs.getLong("highscore2", 599900));
		highscores.set(2, prefs.getLong("highscore3", 599900));
		highscores.set(3, prefs.getLong("highscore4", 599900));
		highscores.set(4, prefs.getLong("highscore5", 599900));

	}
	
	public void submitScore(long score) {			// Could return a boolean for if it's a highscore or not
		
		for(int i = 0; i < highscores.size; i++) {
		
			if(score < highscores.get(i)) {
				highscores.set(i, score);
				saveHighscore(i + 1, score);
				break;
			}
			
		}
		
	}
	
	private void saveHighscore(int i, long score) {
		
		switch(i) {
			case 1:
				prefs.putLong("highscore1", score);
				break;
			case 2:
				prefs.putLong("highscore2", score);
				break;
			case 3:
				prefs.putLong("highscore3", score);
				break;
			case 4:
				prefs.putLong("highscore4", score);
				break;
			case 5:
				prefs.putLong("highscore5", score);
				break;	
		}
		
	}
	
}

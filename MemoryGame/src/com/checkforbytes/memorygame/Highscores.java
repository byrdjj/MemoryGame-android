package com.checkforbytes.memorygame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.LongArray;

public class Highscores {
	
	Preferences prefs;
	LongArray highscores;
	
	public Highscores(Preferences prefs) {
		this.prefs = prefs;
		
		highscores = new LongArray(5);
		
		highscores.add(prefs.getLong("highscore1", 599900));
		highscores.add(prefs.getLong("highscore2", 599900));
		highscores.add(prefs.getLong("highscore3", 599900));
		highscores.add(prefs.getLong("highscore4", 599900));
		highscores.add(prefs.getLong("highscore5", 599900));

	}
	
	public void submitScore(long score) {			// Could return a boolean for if it's a highscore or not
		
		for(int i = 0; i < highscores.size; i++) {
		
			if(score < highscores.get(i)) {
				highscores.insert(i, score);
				highscores.pop();
				saveHighscores();
				break;
			}
			
		}
		
	}
	
	private void saveHighscores() {
		
		prefs.putLong("highscore1", highscores.get(0));
		prefs.putLong("highscore2", highscores.get(1));
		prefs.putLong("highscore3", highscores.get(2));
		prefs.putLong("highscore4", highscores.get(3));
		prefs.putLong("highscore5", highscores.get(4));
		
		prefs.flush();
	}
	
}

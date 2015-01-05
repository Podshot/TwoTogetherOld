package io.github.podshot.enviroment;

import java.io.File;

public class Level {
	
	private static Level instance;
	private File levelFile;

	public Level(File levelFile) {
		setInstance(this);
		this.levelFile = levelFile;
		this.loadLevel();
	}

	private void loadLevel() {
		// TODO Auto-generated method stub
		
	}

	public static Level getInstance() {
		return instance;
	}

	private static void setInstance(Level instance) {
		Level.instance = instance;
	}

}

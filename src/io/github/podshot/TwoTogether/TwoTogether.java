package io.github.podshot.TwoTogether;

import io.github.podshot.TwoTogether.entities.BlockOne;
import io.github.podshot.TwoTogether.entities.BlockTwo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TwoTogether extends StateBasedGame {

	public TwoTogether(String name, String args[]) {
		super(name);
	}

	private static Map<Integer, Level> levelMap = new HashMap<Integer, Level>();

	public static void main(String[] args) throws Exception {
		boolean doLevelEditor = false;
		String template = null;
		for (int i=0;i<args.length;i++) {
			if (args[i].equalsIgnoreCase("-leveleditor")) {
				doLevelEditor = true;
			}
			if (args[i].equalsIgnoreCase("-template")) {
				template = args[i+1];
			}
		}
		if (!doLevelEditor) {
			addLevel(1, new Level(1));
			AppGameContainer container = new AppGameContainer(new Game("Two Together"));
			container.setDisplayMode(600, 480, false);
			container.start();
		} else {
			AppGameContainer container = new AppGameContainer(new LevelEditor("Two Together - LevelEditor", template));
			container.setDisplayMode(600, 450, false);
			container.start();
		}
		new TwoTogether("TwoTogether", args);
	}

	@SuppressWarnings("unchecked")
	public static void log_coords() {
		BlockOne bo = BlockOne.getInstance();
		BlockTwo bt = BlockTwo.getInstance();
		JSONObject json = new JSONObject();
		JSONObject block1 = new JSONObject();
		JSONObject block2 = new JSONObject();

		block1.put("Minimum X", bo.getX());
		block1.put("Minimum Y", bo.getY());
		block1.put("Maximum X", bo.getX()+bo.getXBounds());
		block1.put("Maximum Y", bo.getY()+bo.getYBounds());

		block2.put("Minimum X", bt.getX());
		block2.put("Minimum Y", bt.getY());
		block2.put("Maximum X", bt.getX()+bt.getXBounds());
		block2.put("Maximum Y", bt.getY()+bt.getYBounds());

		json.put("Block One", block1);
		json.put("Block Two", block2);

		try {	 
			FileWriter file = new FileWriter("debug.json");
			file.write(json.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addLevel(int id, Level lvl) {
		if (!(levelMap.containsKey(id))) {
			levelMap.put(id, lvl);
		}
	}

	public static Level getLevel(int id) {
		return levelMap.get(id);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		//addState(new Menu());
	}

}

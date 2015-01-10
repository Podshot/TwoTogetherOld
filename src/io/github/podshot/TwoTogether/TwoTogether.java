package io.github.podshot.TwoTogether;

import io.github.podshot.TwoTogether.entities.BlockOne;
import io.github.podshot.TwoTogether.entities.BlockTwo;
import io.github.podshot.TwoTogether.gamestates.CreditsState;
import io.github.podshot.TwoTogether.gamestates.LevelOneState;
import io.github.podshot.TwoTogether.gamestates.MenuState;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TwoTogether extends StateBasedGame {

	public TwoTogether(String name, String args[]) {
		super(name);
		addState(new MenuState());
		addState(new LevelOneState());
		addState(new CreditsState());
		this.enterState(0);
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer container = new AppGameContainer(new TwoTogether("TwoTogether", args));
		container.setDisplayMode(600, 450, false);
		container.start();
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

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		//addState(new MenuState());
		//addState(new Game());
	}

}

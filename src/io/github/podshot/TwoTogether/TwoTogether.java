package io.github.podshot.TwoTogether;

import io.github.podshot.TwoTogether.gamestates.CreditsState;
import io.github.podshot.TwoTogether.gamestates.LevelOneState;
import io.github.podshot.TwoTogether.gamestates.MenuState;

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

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		//addState(new MenuState());
		//addState(new Game());
	}

}

package io.github.podshot.TwoTogether.gamestates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState {

	private Rectangle playGameBTN = new Rectangle(240, 95, 100, 30);
	private Rectangle quitBTN = new Rectangle(240, 150, 100, 30);
	private Color bgcolor;
	private Color btncolor;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setShowFPS(false);
		bgcolor = new Color(204, 204, 204);
		btncolor = new Color(127, 127, 127);
		//playGameBTN = new Rectangle(240, 95, 100, 30);
		//quitBTN = new Rectangle(240, 150, 100, 30);

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setBackground(bgcolor);
		g.setColor(btncolor);
		g.fill(playGameBTN);
		g.fill(quitBTN);
		
		g.setColor(btncolor);
		g.drawString("Two Together", 235, 50);
		g.setColor(Color.white);
		g.drawString("Play Game", 250, 100);
		//g.drawString("2. Select a Level", 900, 120);
		g.drawString("Quit", 270, 155);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Input input = container.getInput();
			if (playGameBTN.contains(input.getMouseX(), input.getMouseY())) {
				System.out.println("Should play game");
				game.enterState(1);
			} else if (quitBTN.contains(input.getMouseX(), input.getMouseY())) {
				System.out.println("Quitting...");
				container.exit();
			}
		}

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}

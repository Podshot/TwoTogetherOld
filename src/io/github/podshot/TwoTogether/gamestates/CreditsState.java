package io.github.podshot.TwoTogether.gamestates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class CreditsState extends BasicGameState {

	private Color bgcolor;
	private Color btncolor;
	private boolean mousedOver = false;
	private Rectangle mainmenuBTN = new Rectangle(210, 140, 175, 30);

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setShowFPS(false);
		bgcolor = new Color(204, 204, 204);
		btncolor = new Color(127, 127, 127);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setBackground(bgcolor);
		g.setColor(btncolor);
		g.fill(mainmenuBTN);
		
		g.drawString("Two Together", 240, 50);
		g.drawString("Original Game Idea by Blachub\nProgramming by Ben (Podshot)", 175, 80);
		
		g.setColor(Color.white);
		if (mousedOver) {
			g.setColor(Color.lightGray);
		}
		g.drawString("Back to Main Menu", 220, 145);
		if (mousedOver) {
			g.setColor(Color.white);
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (mainmenuBTN.contains(input.getMouseX(), input.getMouseY())) {
				game.enterState(0);
				System.out.println("Going to Main Menu");
			}
		}
		if (mainmenuBTN.contains(input.getMouseX(), input.getMouseY())) {
			mousedOver = true;
		} else {
			mousedOver = false;
		}
		
	}

	@Override
	public int getID() {
		return -1;
	}

}

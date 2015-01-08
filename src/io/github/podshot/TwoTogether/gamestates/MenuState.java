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
	private Rectangle quitBTN = new Rectangle(240, 180, 100, 30);
	private Rectangle creditsBTN = new Rectangle(240, 137, 100, 30);
	private Color bgcolor;
	private Color btncolor;
	private boolean[] mousedOver = new boolean[3];
	/* mousedover states entries | Corresponding button
	 * 0 | Play Game
	 * 1 | Quit
	 * 2 | Credits
	 */

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
		g.fill(creditsBTN);
		g.fill(quitBTN);
		
		g.setColor(btncolor);
		g.drawString("Two Together", 235, 50);
		g.setColor(Color.white);
		if (mousedOver[0]) {
			g.setColor(Color.lightGray);
		}
		g.drawString("Play Game", 250, 100);
		if (mousedOver[0]) {
			g.setColor(Color.white);
		}
		//g.drawString("Select a Level", 900, 120);
		if (mousedOver[1]) {
			g.setColor(Color.lightGray);
		}
		g.drawString("Quit", 270, 185);
		if (mousedOver[1]) {
			g.setColor(Color.white);
		}
		
		if (mousedOver[2]) {
			g.setColor(Color.lightGray);
		}
		g.drawString("Credits", 258, 142);
		if (mousedOver[2]) {
			g.setColor(Color.white);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (playGameBTN.contains(input.getMouseX(), input.getMouseY())) {
				System.out.println("Should play game");
				game.enterState(1);
			} else if (quitBTN.contains(input.getMouseX(), input.getMouseY())) {
				System.out.println("Quitting...");
				container.exit();
			} else if (creditsBTN.contains(input.getMouseX(), input.getMouseY())) {
				System.out.println("Should show credits");
				game.enterState(-1);
			}
		}
		if (playGameBTN.contains(input.getMouseX(), input.getMouseY()) && !input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			mousedOver[0] = true;
		} else {
			mousedOver[0] = false;
		}
		if (quitBTN.contains(input.getMouseX(), input.getMouseY()) && !input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			mousedOver[1] = true;
		} else {
			mousedOver[1] = false;
		}
		if (creditsBTN.contains(input.getMouseX(), input.getMouseY()) && !input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			mousedOver[2] = true;
		} else {
			mousedOver[2] = false;
		}
		
		// Start easter egg code
		// End easter egg code

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}

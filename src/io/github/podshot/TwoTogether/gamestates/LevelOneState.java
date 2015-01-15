package io.github.podshot.TwoTogether.gamestates;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//@SuppressWarnings("unused")
public class LevelOneState extends BasicGameState {

	// Small Box fields
	private boolean b1jumping;
	private float b1verticalSpeed = 0.0f;
	private float blockOneX;
	private float blockOneY;

	// Large Box fields
	private boolean b2falling = false;
	private boolean b2jumping;
	private float b2verticalSpeed = 0.0f;
	private float blockTwoX;
	private float blockTwoY;
	
	// Constants
	
	private String description;
	private Color bgcolor;
	private static ArrayList<Shape> terrain_normal = new ArrayList<Shape>();
	private static ArrayList<Shape> terrain_background = new ArrayList<Shape>();

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(bgcolor);
		for (Shape rect : terrain_background) {
			g.fill(rect);
		}
		g.setColor(Color.gray);
		g.drawString(description, 110, 50);
		g.setBackground(new Color(204, 204, 204));
		g.setColor(Color.red);
		g.fillRect(this.blockOneX, this.blockOneY, 10, 10);
		g.setColor(Color.cyan);
		g.fillRect(this.blockTwoX, this.blockTwoY, 10, 20);

		g.setColor(Color.gray);
		for (Shape rect : terrain_normal) {
			g.fill(rect);
		}
		g.drawString("FPS: "+container.getFPS(), 50, 30);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		bgcolor = new Color(178,178,178);

		JSONObject level = null;
		try {
			level = (JSONObject) new JSONParser().parse(new FileReader("level_1.json"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		JSONArray terrainJSON = (JSONArray) level.get("Shapes");
		this.description = (String) level.get("Description");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iter = terrainJSON.iterator();
		while (iter.hasNext()) {
			JSONObject shape_json = iter.next();
			System.out.println(shape_json.toJSONString());
			Rectangle rectangle = new Rectangle(Float.parseFloat(shape_json.get("Min X").toString()), Float.parseFloat(shape_json.get("Min Y").toString()), Float.parseFloat(shape_json.get("Width").toString()), Float.parseFloat(shape_json.get("Height").toString()));
			if (shape_json.get("Type").equals("STATIC")) {
				terrain_normal.add(rectangle);
			} else if (shape_json.get("Type").equals("BACKGROUND")) {
				terrain_background.add(rectangle);
			} else if (shape_json.get("Type").equals("BLOCK_ONE_OBJECTIVE")) {
				
			} else if (shape_json.get("Type").equals("BLOCK_TWO_OBJECTIVE")) {
				
			}
		}
		container.setShowFPS(true);
		
		this.blockOneX = 100;
		this.blockOneY = 100;
		this.blockTwoX = 0;
		this.blockTwoY = 0;
		//Music backroundMusic = new Music("io/github/podshot/TwoTogether/sound/backround_music.ogg");
		//backroundMusic.loop(1f, 0.30f);
		//container.setShowFPS(true);
		//container.setTargetFrameRate(60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// Start controlling the small block
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			this.blockOneMoveLeft(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			this.blockOneMoveRight(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_UP) && !b1jumping && !this.blockOneCollidedOnTop()) {
			b1verticalSpeed = -1.0f * delta;
			b1jumping = true;
		}


		// End controlling the small block

		// Start controlling the big block
		if (container.getInput().isKeyDown(Input.KEY_A)) {
			this.blockTwoMoveLeft(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_D)) {
			this.blockTwoMoveRight(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_W)  && !b2jumping && !b2falling && !this.blockTwoCollidedOnTop()) {
			b2verticalSpeed = -1.0f * delta;
			b2jumping = true;
		}

		if (b1jumping && !b2jumping) {
			if (this.blockOneIntersectsSomething()) {
				b1jumping = false;
				b1verticalSpeed = -1.1f;
				b1verticalSpeed = 0;
			}
			if (b1jumping) {
				b1verticalSpeed += .009f * delta;
			}
			float b1speedY = this.blockOneY;
			b1speedY += b1verticalSpeed;
			this.blockOneY = b1speedY;

			if (this.blockOneIntersectsSomething()) {
				b1jumping = false;
				b1verticalSpeed = 0;
				if (this.blockOneBounds().intersects(this.blockTwoBounds())) {
					this.blockOneY = this.blockOneY-0.9f;
					System.out.println("Fired when jumped");
				} else {
					this.blockOneY = this.blockOneY-0.9f;
				}
			}
		} else if (!b1jumping && b2jumping) {

			if (this.blockTwoIntersectsSomething()) {
				b2jumping = false;
				b2verticalSpeed = -1.1f;
				b2verticalSpeed = 0;
			}
			if (b2jumping) {
				b2verticalSpeed += .007f * delta;
			}
			float b2speedY = this.blockTwoY;
			b2speedY += b2verticalSpeed;
			this.blockTwoY = b2speedY;

			if (this.blockTwoIntersectsSomething()) {
				b2jumping = false;
				b2verticalSpeed = 0;
				
				if (this.blockTwoBounds().intersects(this.blockOneBounds())) {
					this.blockTwoY = this.blockTwoY - 1.0f;
					System.out.println("Fired when jumped");
				} else {
					this.blockTwoY = this.blockTwoY - 1.0f;
				}
				
				//}
			}
		} else if (!b1jumping && !b2jumping) {
			
		} else {
			b1jumping = false;
			b2jumping = false;
			System.out.println("Both jumped");
		}

		if (!this.blockOneCollidedOnBottom() && !b1jumping) {
			this.blockOneY = this.blockOneY + (1.0f*delta);
			if (this.blockOneIntersectsSomething()) {
				this.blockOneY = this.blockOneY - (1.0f*delta);
			}
		}
		if (!this.blockTwoCollidedOnBottom() && !b2jumping) {
			this.blockTwoY = this.blockTwoY + (1.0f*delta);
			if (this.blockTwoIntersectsSomething()) {
				this.blockTwoY = this.blockTwoY - (1.0f*delta);
			}
		}
		// End controlling the big block

		if (this.blockOneCollidedOnBottom() && !this.blockOneCollidedOnTop()) {
			this.blockOneY -= .5f;
		}
		if (this.blockOneCollidedOnTop() && !this.blockOneCollidedOnBottom()) {
			this.blockOneY += .5f;
		}
		
		if (this.blockTwoCollidedOnBottom() && !this.blockTwoCollidedOnTop()) {
			this.blockTwoY -= .5f;
		}
		if (this.blockTwoCollidedOnTop() && !this.blockTwoCollidedOnBottom()) {
			this.blockTwoY += .5f;
		}
		
		// Debug logging
		/*
		System.out.println("Block One intersects something: "+this.blockOneIntersectsSomething());
		System.out.println("Block One collided on bottom: "+this.blockOneCollidedOnBottom());
		System.out.println("Block One collided on top: "+this.blockOneCollidedOnTop());
		*/
		if (container.getInput().isKeyPressed(Input.KEY_R)) {
			container.reinit();
		}
	}

	public ArrayList<Shape> getTerrain() {
		return terrain_normal;
	}

	@Override
	public int getID() {
		return 1;
	}
	
	// Start Block One
	private Shape blockOneBounds() {
		return new Rectangle(this.blockOneX, this.blockOneY, 10, 10);
	}
	
	private boolean blockOneIntersectsSomething() {
		boolean isIntersecting = false;
		if (this.blockOneBounds().intersects(this.blockTwoBounds())) {
			isIntersecting = true;
		} else {
			for (Shape shape : this.getTerrain()) {
				isIntersecting = isIntersecting || this.blockOneBounds().intersects(shape);
			}
		}
		return isIntersecting;
		
	}
	
	private void blockOneMoveLeft(int delta) {
		if (0 >= Math.round(this.blockOneX)) {
			return;
		}
		this.blockOneX -= 0.1f*delta;
		if (this.blockOneIntersectsSomething()) {
			this.blockOneX += 0.1f*delta;
		}
	}
	
	private void blockOneMoveRight(int delta) {
		if (790 <= Math.round(this.blockOneX)) {
			return;
		}
		this.blockOneX += 0.1f*delta;
		if (this.blockOneIntersectsSomething()) {
			this.blockOneX -= 0.1f*delta;
		}
	}
	
	private boolean blockOneCollidedOnTop() {
		boolean toReturn = false;
		Shape bt_bounds = this.blockTwoBounds();
		toReturn = (bt_bounds.contains(this.blockOneBounds().getMinX(), (this.blockOneBounds().getCenterY()-6))
				|| bt_bounds.contains(this.blockOneBounds().getCenterX(), (this.blockOneBounds().getCenterY()-6))
				|| bt_bounds.contains(this.blockOneBounds().getMaxX(), (this.blockOneBounds().getCenterY()-6)));
		for (Shape shape : this.getTerrain()) {
			//toReturn = toReturn || this.blockOneBounds().intersects(shape);
			toReturn = toReturn || (shape.contains(this.blockOneBounds().getMinX(), (this.blockOneBounds().getCenterY()-6))
					|| shape.contains(this.blockOneBounds().getCenterX(), (this.blockOneBounds().getCenterY()-6))
					|| shape.contains(this.blockOneBounds().getMaxX(), (this.blockOneBounds().getCenterY()-6)));
		}
		return toReturn;
	}
	
	private boolean blockOneCollidedOnBottom() {
		boolean toReturn = false;
		Shape bt_bounds = this.blockTwoBounds();
		toReturn = (bt_bounds.contains(this.blockOneBounds().getMinX(), (this.blockOneBounds().getCenterY()+6))
				|| bt_bounds.contains(this.blockOneBounds().getCenterX(), (this.blockOneBounds().getCenterY()+6))
				|| bt_bounds.contains(this.blockOneBounds().getMaxX(), (this.blockOneBounds().getCenterY()+6)));
		for (Shape shape : this.getTerrain()) {
			//toReturn = toReturn || this.blockOneBounds().intersects(rect);
			toReturn = toReturn || (shape.contains(this.blockOneBounds().getMinX(), (this.blockOneBounds().getCenterY()+6))
					|| shape.contains(this.blockOneBounds().getCenterX(), (this.blockOneBounds().getCenterY()+6))
					|| shape.contains(this.blockOneBounds().getMaxX(), (this.blockOneBounds().getCenterY()+6)));
		}
		return toReturn;
	}
	// End Block One
	
	// Start Block Two
	private boolean blockTwoIntersectsSomething() {
		boolean isIntersecting = false;
		if (this.blockTwoBounds().intersects(this.blockOneBounds())) {
			isIntersecting = true;
		} else {
			for (Shape shape : this.getTerrain()) {
				isIntersecting = isIntersecting || this.blockTwoBounds().intersects(shape);
			}
		}
		return isIntersecting;
	}

	private Shape blockTwoBounds() {
		return new Rectangle(this.blockTwoX, this.blockTwoY, 10, 20);
	}
	
	private void blockTwoMoveLeft(int delta) {
		if (0 >= Math.round(this.blockTwoX)) {
			return;
		}
		this.blockTwoX -= 0.1f*delta;
		if (this.blockTwoIntersectsSomething()) {
			this.blockTwoX += 0.1f*delta;
		}
	}
	
	private void blockTwoMoveRight(int delta) {
		if (790 <= Math.round(this.blockTwoX)) {
			return;
		}
		this.blockTwoX += 0.1f*delta;
		if (this.blockTwoIntersectsSomething()) {
			this.blockTwoX -= 0.1f*delta;
		}
	}
	
	private boolean blockTwoCollidedOnTop() {
		boolean toReturn = false;
		toReturn = (this.blockOneBounds().contains(this.blockTwoBounds().getMinX(), (this.blockTwoBounds().getCenterY()-11))
				|| this.blockOneBounds().contains(this.blockTwoBounds().getCenterX(), (this.blockTwoBounds().getCenterY()-11))
				|| this.blockOneBounds().contains(this.blockTwoBounds().getMaxX(), (this.blockTwoBounds().getCenterY()-11)));
		for (Shape shape : this.getTerrain()) {
			//toReturn = toReturn || this.blockTwoBounds().intersects(shape);
			toReturn = toReturn || (shape.contains(this.blockTwoBounds().getMinX(), (this.blockTwoBounds().getCenterY()-11))
					|| shape.contains(this.blockTwoBounds().getCenterX(), (this.blockTwoBounds().getCenterY()-11))
					|| shape.contains(this.blockTwoBounds().getMaxX(), (this.blockTwoBounds().getCenterY()-11)));
		}
		return toReturn;
	}
	
	private boolean blockTwoCollidedOnBottom() {
		boolean toReturn = false;
		toReturn = (this.blockOneBounds().contains(this.blockTwoBounds().getMinX(), (this.blockTwoBounds().getCenterY()+11))
				|| this.blockOneBounds().contains(this.blockTwoBounds().getCenterX(), (this.blockTwoBounds().getCenterY()+11))
				|| this.blockOneBounds().contains(this.blockTwoBounds().getMaxX(), (this.blockTwoBounds().getCenterY()+11)));
		for (Shape shape : this.getTerrain()) {
			//toReturn = toReturn || this.blockTwoBounds().intersects(shape);
			toReturn = toReturn || (shape.contains(this.blockTwoBounds().getMinX(), (this.blockTwoBounds().getCenterY()+11))
					|| shape.contains(this.blockTwoBounds().getCenterX(), (this.blockTwoBounds().getCenterY()+11))
					|| shape.contains(this.blockTwoBounds().getMaxX(), (this.blockTwoBounds().getCenterY()+11)));
		}
		return toReturn;
	}
	
	// End Block Two

}

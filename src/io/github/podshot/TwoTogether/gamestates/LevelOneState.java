package io.github.podshot.TwoTogether.gamestates;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import io.github.podshot.TwoTogether.TwoTogether;
import io.github.podshot.TwoTogether.entities.BlockOne;
import io.github.podshot.TwoTogether.entities.BlockTwo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

@SuppressWarnings("unused")
public class LevelOneState extends BasicGameState {

	private AppGameContainer app;
	private Input input;
	private BlockOne bo;
	private BlockTwo bt;

	// Small Box fields
	private float b1_x;
	private float b1_y;
	private float b1_bounds_x = 20;
	private float b1_bounds_y = 20;
	private boolean b1jumping;
	private float b1verticalSpeed = 0.0f;
	private boolean b1falling;

	// Large Box fields
	private float b2_x;
	private float b2_y;
	private float b2_bounds_x = 20;
	private float b2_bounds_y = 40;
	private boolean b2falling = false;
	private boolean b2jumping;
	private float b2verticalSpeed = 0.0f;
	private boolean b1fix;
	private boolean b2fix;
	private String description;
	
	//private Level level;
	private static ArrayList<Shape> terrain_normal = new ArrayList<Shape>();

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//this.drawBounds(g);
		g.drawString(description, 110, 50);
		g.setBackground(new Color(204, 204, 204));
		g.setColor(Color.red);
		g.fillRect(bo.getX(), bo.getY(), bo.getXBounds(), bo.getYBounds());
		g.setColor(Color.cyan);
		g.fillRect(bt.getX(), bt.getY(), bt.getXBounds(), bt.getYBounds());
		g.setColor(Color.green);


		for (Shape rect : terrain_normal) {
			g.setColor(Color.gray);
			g.fill(rect);
		}
	}

	@Deprecated
	private void drawBounds(Graphics g) {
		g.drawLine(0, 0, 800, 0);
		g.drawLine(0, 0, 0, 600);
		g.drawLine(0, 600, 800, 600);
		g.drawLine(0, 600, 800, 600);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		if (container instanceof AppGameContainer) {
			app = (AppGameContainer) container;
		}

		JSONObject level = null;
		try {
			level = (JSONObject) new JSONParser().parse(new FileReader("level_1.json"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		JSONArray terrainJSON = (JSONArray) level.get("Shapes");
		this.description = (String) level.get("Description");
		input = container.getInput();
		bo = BlockOne.getInstance();
		bt = BlockTwo.getInstance();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iter = terrainJSON.iterator();
		while (iter.hasNext()) {
			JSONObject shape_json = iter.next();
			System.out.println(shape_json.toJSONString());
			Rectangle rectangle = new Rectangle(Float.parseFloat(shape_json.get("Min X").toString()), Float.parseFloat(shape_json.get("Min Y").toString()), Math.abs(Float.parseFloat(shape_json.get("Width").toString())), Math.abs(Float.parseFloat(shape_json.get("Height").toString())));
			terrain_normal.add(rectangle);
		}

		//Music backroundMusic = new Music("io/github/podshot/TwoTogether/sound/backround_music.ogg");
		//backroundMusic.loop(1f, 0.30f);
		//this.app.setShowFPS(false);
		//this.app.setTargetFrameRate(60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// Start controlling the small block
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			bo.move_left(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			bo.move_right(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
			bo.move_down(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_UP) && !b1jumping && !bo.collidedOnTop()) {
			b1verticalSpeed = -1.0f * delta;
			b1jumping = true;
		}


		// End controlling the small block

		// Start controlling the big block
		if (container.getInput().isKeyDown(Input.KEY_A)) {
			bt.move_left(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_D)) {
			bt.move_right(delta);
		}
		if (container.getInput().isKeyDown(Input.KEY_W)  && !b2jumping && !b2falling && !bt.collidedOnTop()) {
			b2verticalSpeed = -1.0f * delta;
			b2jumping = true;
		}
		if (container.getInput().isKeyDown(Input.KEY_S)) {
			bt.move_down(delta);
		}

		if (b1jumping && !b2jumping) {
			if (bo.collided()) {
				b1jumping = false;
				b1verticalSpeed = -1.1f;
				b1verticalSpeed = 0;
			}
			if (b1jumping) {
				b1verticalSpeed += .007f * delta;
				b1fix = true;
			}
			float b1speedY = bo.getY();
			b1speedY += b1verticalSpeed;
			bo.setY(b1speedY);

			if (bo.collided()) {
				b1jumping = false;
				b1verticalSpeed = 0;
				//if (580 <= Math.round(bo.getX())){
				//	bo.setY(bo.getX()+(0.5f*delta));
				//} else {
				if (bo.bounds().intersects(bt.bounds())/* && b1fix*/) {
					bo.setY(bo.getY()-/*(*/0.9f/*delta)*/);
					System.out.println("Fired when jumped");
					b1fix = false;
				} else {
					bo.setY(bo.getY()-/*(*/0.9f/*delta)*/);
				}
				//}
			}
		} else if (!b1jumping && b2jumping) {

			if (bt.collided()) {
				b2jumping = false;
				b2verticalSpeed = -1.1f;
				b2verticalSpeed = 0;
			}
			if (b2jumping) {
				b2verticalSpeed += .007f * delta;
			}
			float b2speedY = bt.getY();
			b2speedY += b2verticalSpeed;
			bt.setY(b2speedY);

			if (bt.collided()) {
				b2jumping = false;
				b2verticalSpeed = 0;
				//if (560 <= Math.round(this.y)) {
				//	bt.setY(bt.getY()+(0.5f*delta));
				//} else {
				if (bt.bounds().intersects(bo.bounds())) {
					bt.setY(bt.getY()-/*(*/1.0f/*delta)*/);
					System.out.println("Fired when jumped");
					System.out.println(bt.bounds().intersects(bo.bounds()));
				} else {
					bt.setY(bt.getY()-/*(*/0.9f/*delta)*/);
				}
				//}
			}
		} else if (!b1jumping && !b2jumping) {
			if (bo.collidedOnBottom() && b1fix) {
				bo.setY(bt.getY()+(0.5f*delta));
				System.out.println("Always firing (1)....");
				b1fix = false;
				//} else if (bo.collidedOnTop() && b1fix) {
				//bo.setY(bt.getY()-(0.5f*delta));
				//System.out.println("Always firing (2)....");
				//b1fix = false;
			}
			bo.collidedOnTop();
			bo.collidedOnBottom();
			bt.collidedOnTop();
			bt.collidedOnBottom();
		} else {
			b1jumping = false;
			b2jumping = false;
			System.out.println("Both jumped");
		}

		if (!bo.collidedOnBottom() && !b1jumping) {
			bo.setY(bo.getY()+(1.0f*delta));
			if (bo.collided()) {
				bo.setY(bo.getY()-(1.0f*delta));
			}
		}
		if (!bt.collidedOnBottom() && !b2jumping) {
			bt.setY(bt.getY()+(1.0f*delta));
			if (bt.collided()) {
				bt.setY(bt.getY()-(1.0f*delta));
			}
		}
		// End controlling the big block

		// Gravity implementation


		// DEBUG stuff
		if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			TwoTogether.log_coords();
		}
		//System.out.println(container.getInput().getMouseX()+":"+container.getInput().getMouseY());
	}

	@Deprecated
	private Shape b1_bounds() {
		return new Rectangle(this.getB1_x(), this.getB1_y(), this.b1_bounds_x, this.b1_bounds_y);
	}

	@Deprecated
	private Shape b2_bounds() {
		return new Rectangle(this.getB2_x(), this.getB2_y(), this.b2_bounds_x, this.b2_bounds_y);
	}

	public float getB1_x() {
		return b1_x;
	}

	public void setB1_x(float b1_x) {
		this.b1_x = b1_x;
	}

	public float getB1_y() {
		return b1_y;
	}

	public void setB1_y(float b1_y) {
		this.b1_y = b1_y;
	}

	public float getB2_x() {
		return b2_x;
	}

	public void setB2_x(float b2_x) {
		this.b2_x = b2_x;
	}

	public float getB2_y() {
		return b2_y;
	}

	public void setB2_y(float b2_y) {
		this.b2_y = b2_y;
	}
	
	public static ArrayList<Shape> getTerrain() {
		return terrain_normal;
	}

	@Deprecated
	private enum Movement {
		LEFT, RIGHT, DOWN, UP;
	}

	@Deprecated
	private enum Entity {
		BLOCK_1, BLOCK_2;
	}
	
	@Override
	public int getID() {
		return 1;
	}
}

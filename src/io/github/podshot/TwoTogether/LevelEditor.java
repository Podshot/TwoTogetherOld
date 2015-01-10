package io.github.podshot.TwoTogether;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

@Deprecated
public class LevelEditor extends BasicGame {

	private Image template;
	private AppGameContainer app;
	private String template_string;
	private Map<String, Shape> terrain = new HashMap<String, Shape>();
	private Map<String, ShapeType> data = new HashMap<String, ShapeType>();
	private boolean isDrawing;
	private int startPoint_x;
	private int startPoint_y;
	private ShapeType drawingType;

	public LevelEditor(String title, String template) {
		super(title);
		this.template_string = template;
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (this.template != null) {
			g.drawImage(template, 0, 0);
		}
		if (isDrawing) {
			g.setColor(Color.white);
			g.fillRect(this.startPoint_x, this.startPoint_y, container.getInput().getMouseX()-this.startPoint_x, container.getInput().getMouseY()-this.startPoint_y);
			String id = this.startPoint_x+":"+this.startPoint_y;
			if (this.terrain.containsKey(id)) {
				this.terrain.remove(id);
			}
			if (this.data.containsKey(id)) {
				this.data.remove(id);
			}
			this.terrain.put(id, new Rectangle(this.startPoint_x, this.startPoint_y, container.getInput().getMouseX()-this.startPoint_x, container.getInput().getMouseY()-this.startPoint_y));
			this.data.put(id, this.drawingType);
		}
		if (!(this.terrain.isEmpty())) {
			for (String key : this.terrain.keySet()) {
				g.fill(this.terrain.get(key));
			}
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer) {
			app = (AppGameContainer) container;
		}
		if (this.template != null) {
			this.template = new Image(this.template_string);
		}
		this.drawingType = ShapeType.STATIC;

	}

	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
		if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !this.isDrawing) {
			this.isDrawing = true;
			this.startPoint_x = container.getInput().getMouseX();
			this.startPoint_y = container.getInput().getMouseY();
		}
		if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && this.isDrawing) {
			this.isDrawing = false;
		}
		if (container.getInput().isKeyPressed(Input.KEY_1)) {
			this.drawingType = ShapeType.STATIC;
		}
		if (container.getInput().isKeyPressed(Input.KEY_2)) {
			this.drawingType = ShapeType.OBJECTIVE_BLOCK_ONE;
		}
		if (container.getInput().isKeyPressed(Input.KEY_3)) {
			this.drawingType = ShapeType.OBJECTIVE_BLOCK_TWO;
		}

		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			this.logTerrain(this.terrain);
		}
	}

	@SuppressWarnings("unchecked")
	private void logTerrain(Map<String, Shape> terrain) {
		Scanner in = new Scanner (System.in);
		System.out.println("Enter level description");
		String desc = in.nextLine();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (String key : terrain.keySet()) {
			JSONObject shape_json = new JSONObject();
			Rectangle shape = (Rectangle) terrain.get(key);
			shape_json.put("Min X", shape.getMinX());
			shape_json.put("Min Y", shape.getMinY());
			shape_json.put("Width", shape.getWidth());
			shape_json.put("Height", shape.getHeight());
			array.add(shape_json);
		}
		json.put("Shapes", array);
		json.put("Description", desc);
		try {	 
			FileWriter file = new FileWriter("level_1.json");
			file.write(json.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.close();

	}
	
	public enum ShapeType {
		STATIC, OBJECTIVE_BLOCK_ONE, OBJECTIVE_BLOCK_TWO;
	}

}

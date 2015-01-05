package io.github.podshot.TwoTogether;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class LevelEditor extends BasicGame {

	private Image template;
	private AppGameContainer app;
	private String template_string;
	private Map<String, Shape> terrain =  new HashMap<String, Shape>();
	private boolean isDrawing;
	private int startPoint_x;
	private int startPoint_y;
	private ArrayList<float[]> points = new ArrayList<float[]>();
	private ArrayList<Polygon> polys = new ArrayList<Polygon>();

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
			Polygon poly = new Polygon();
			for (float[] xy : points) {
				poly.addPoint(xy[0], xy[1]);
			}
			g.fill(poly);
			//g.fillRect(this.startPoint_x, this.startPoint_y, container.getInput().getMouseX()-this.startPoint_x, container.getInput().getMouseY()-this.startPoint_y);
			//String id = this.startPoint_x+":"+this.startPoint_y;
			//if (this.terrain.containsKey(id)) {
				//this.terrain.remove(id);
			//}
			//this.terrain.put(id, new Rectangle(this.startPoint_x, this.startPoint_y, container.getInput().getMouseX()-this.startPoint_x, container.getInput().getMouseY()-this.startPoint_y));
		}
		//if (!(this.terrain.isEmpty())) {
			//for (String key : this.terrain.keySet()) {
				//g.fill(this.terrain.get(key));
			//}
		//}
		for (Polygon shap : polys) {			
			g.fill(shap);
		} 
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer) {
			app = (AppGameContainer) container;
		}
		this.template = new Image(this.template_string);

	}

	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
		/*if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !this.isDrawing) {
			this.isDrawing = true;
			this.startPoint_x = container.getInput().getMouseX();
			this.startPoint_y = container.getInput().getMouseY();
		}
		if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && this.isDrawing) {
			this.isDrawing = false;
		}*/
		if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isDrawing) {
			this.isDrawing = true;
			points.add(new float[]{container.getInput().getMouseX(),container.getInput().getMouseY()});
		}
		if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isDrawing) {
			points.add(new float[]{container.getInput().getMouseX(),container.getInput().getMouseY()});
		}
		if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && isDrawing) {
			points.add(new float[]{container.getInput().getMouseX(),container.getInput().getMouseY()});
			this.isDrawing = false;
			Polygon p = new Polygon();
			for (float[] point : points) {
				p.addPoint(point[0], point[1]);
			}
			polys.add(p);
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
		/*for (String key : terrain.keySet()) {
			JSONObject shape_json = new JSONObject();
			Rectangle shape = (Rectangle) terrain.get(key);
			shape_json.put("Min X", shape.getMinX());
			shape_json.put("Min Y", shape.getMinY());
			shape_json.put("Width", shape.getWidth());
			shape_json.put("Height", shape.getHeight());
			array.add(shape_json);
		}*/
		for (Polygon p : polys) {
			JSONObject t = new JSONObject();
			for (int i=0;i<p.getPointCount();i++) {
			}
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

}

package io.github.podshot.TwoTogether;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level {
	
	private static Level instance;
	private int id;
	private JSONObject contents;
	private JSONArray terrain;
	private String description;
	//private static String level_path = "src<sep>io<sep>github<sep>podshot<sep>TwoTogether<sep>levels<sep>".replace("<sep>", File.separator);
	private static String level_path = "";

	public Level(int id) throws ParseException, IOException {
		instance = this;
		this.id = id;
		
	    this.contents = (JSONObject) new JSONParser().parse(new FileReader(level_path+"level_"+id+".json"));
	    this.terrain = (JSONArray) this.contents.get("Shapes");
	    this.description = (String) this.contents.get("Description");
	}
	
	public int getID() {
		return this.id;
	}
	
	public JSONArray getTerrain() {
		return this.terrain;
	}
	
	public String getDescription() {
		return this.description;
	}
	

}

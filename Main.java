import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends PApplet{
	private Character character;
	private List<Renderable> renderables;
	/**
	 * Singleton design pattern so we don't need to keep passing reference to this class around. We need
	 * the draw methods inside PApplet (like ellipse(float, float, float, float))
	 */
	private static Main instance;

	public static void main(String[] args){
		PApplet.main("Main");
	}

	@Override
	public void settings(){
		instance = this;
		size(1000, 500);
		renderables = new ArrayList<>();
	}

	@Override
	public void setup(){

	}

	@Override
	public void draw(){
		fill(255);
		renderables.forEach(Renderable::render);
	}
}

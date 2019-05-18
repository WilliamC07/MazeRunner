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

	/**
	 * Access the singleton.
	 * Use this to access draw methods (the ellipse(), line() and more).
	 * @return Get the single instance of this class.
	 */
	public static Main getInstance(){
		return instance;
	}

	@Override
	public void settings(){
		instance = this;
		size(1000, 500);
		renderables = new ArrayList<>();
	}

	@Override
	public void setup(){
		// TODO: replace with maze
		List<Wall> walls = new ArrayList<>();
		walls.add(new Wall(new Point(50, 50), new Point(50, 100)));
		renderables.addAll(walls);

		character = new Character(new Point((float) width / 2, (float) height / 2), walls);
		renderables.add(character);
	}

	@Override
	public void draw(){
		fill(255);
		renderables.forEach(Renderable::render);
	}
}

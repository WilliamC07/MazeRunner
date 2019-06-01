import processing.core.PApplet;

import java.util.*;

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
		size(1000, 1000);
		renderables = new ArrayList<>();
	}

	@Override
	public void setup(){
		Maze maze = new Maze(30,30,this);
		List<Wall> walls = maze.getFlat();
		Collections.addAll(walls, maze.getBorder());
		character = new Character(new Point((float) width / 2, (float) height / 2), walls, maze.verticies());

		renderables.add(maze);
		renderables.add(character);
	}

	@Override
	public void draw(){
		background(255);
		renderables.forEach(Renderable::render);
	}

  public void keyPressed(){
		if(key=='W' || key=='w'){
			character.move(0f,-5f);
		} else if(key=='S' || key=='s'){
			character.move(0f,5f);
		} else if(key=='A' || key=='a'){
			character.move(-5f,0f);
		} else if(key=='D' || key=='d'){
			character.move(5f,0f);
		}
	}
}

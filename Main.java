import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet{
	private Character character;
	private Maze maze;
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
		character.move();
	}

	@Override
	public void keyPressed(){
		switch(key){
			case 'H':
			case 'h':
				maze.hint(character.getPos(),10);
				break;
			case 'G':
			case 'g':
				maze.hint(character.getPos(),maze.getLength()*maze.getWidth());
				break;
			default:
				character.setVelocity(keyCode,true);
				break;
		}
	}

	@Override
	public void keyReleased(){
		character.setVelocity(keyCode,false);
	}
}

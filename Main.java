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
		size(750, 750);
		renderables = new ArrayList<>();
	}

	@Override
	public void setup(){
    boolean testRayMode = false;

		if(testRayMode){
			List<Wall> walls = new ArrayList<>();
			Point a = new Point(20, 20);
			Point b = new Point(20, 100);
			Point c = new Point(100, 100);
			walls.add(new Wall(a, b));
			walls.add(new Wall(b, c));
			renderables.addAll(walls);

			character = new Character(new Point((float) width / 2, (float) height / 2), walls);
			//renderables.add(character);
		}else{
			Maze maze = new Maze(50,50,this);
			List<Wall> walls = maze.getFlat();
			character = new Character(new Point((float) width / 2, (float) height / 2), walls);
			renderables.add(maze);
			character = new Character(new Point((float) width / 2, (float) height / 2), maze.getFlat());
			renderables.add(character);
		}
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

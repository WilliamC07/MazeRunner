import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet{
	private Character character;
	private Maze maze;
	private List<Character> movables;
	private List<Renderable> renderables;
	private boolean isGodMode;
	private int screen = 0; //0 for maze, 1 for minimap
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
		maze = new Maze(30,30,this);
		character = new Character(maze);
		movables = new ArrayList<Character>();
		movables.add(character);
		renderables.add(character);
		for(int i = 0; i<3; i++){
			Monster monster = new Monster((int)(Math.random()*maze.getWidth()),(int)(Math.random()*maze.getLength()),maze);
			movables.add(monster);
			renderables.add(monster);
		}
		renderables.add(maze);
	}

	@Override
	public void draw(){
		background(isGodMode ? 255 : 0);
		if(screen==0){
			renderables.forEach(Renderable::render);
			movables.forEach(Character::move);
		} else {
			maze.renderMinimap();
			if(isGodMode){
				maze.renderMinimapGod();
			}
		}
	}

	@Override
	public void keyPressed(){
		if(screen==0){
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
		switch(key){
			case 'P':
			case 'p':
				isGodMode = !isGodMode;
				break;
			case 'M':
			case 'm':
				screen = (screen+1)%2;
				break;
		}
	}

	/**
	 * God mode means you can see the whole map (ray casting off).
	 * @return
	 */
	public boolean isGodMode(){
		return isGodMode;
	}

	@Override
	public void keyReleased(){
		character.setVelocity(keyCode,false);
	}
}

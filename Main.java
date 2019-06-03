import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet{
	private Character character;
	private Maze maze;
	private List<Renderable> renderables;
	private boolean isGodMode;
	private Screen screen;
	/**
	* Singleton design pattern so we don't need to keep passing reference to this class around. We need
	* the draw methods inside PApplet (like ellipse(float, float, float, float))
	*/
	private static Main instance;
	private TitleScreen titleScreen;

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
		screen = Screen.TITLE_SCREEN;
		instance = this;
		titleScreen = new TitleScreen();
		size(1000, 1000);
		renderables = new ArrayList<>();
	}

	@Override
	public void setup(){
		maze = new Maze(30,30,this);
		character = new Character(maze);
		renderables.add(character);
		renderables.add(maze);
	}

	@Override
	public void draw(){
		switch(screen){
			case TITLE_SCREEN:
				background(255);
				break;
			case MINIMAP:
				background(isGodMode ? 255 : 0);
				maze.renderMinimap();
				if(isGodMode){
					maze.renderMinimapGod();
				}
				break;
			case PLAYING:
				background(isGodMode ? 255 : 0);
				renderables.forEach(Renderable::render);
				character.move();
				break;
		}
	}

	@Override
	public void keyPressed(){
		// title screen commands
		if(screen == Screen.TITLE_SCREEN){
			titleScreen.feedCharacter(key);
		}

		// playing only keys
		if(screen == Screen.PLAYING){
			switch(key){
				case 'H':
				case 'h':
					maze.hint(character.getPos(),10);
					break;
				case 'G':
				case 'g':
					maze.hint(character.getPos(),maze.getLength()*maze.getWidth());
					break;
				case 'P':
				case 'p':
					isGodMode = !isGodMode;
					break;
				default:
					character.setVelocity(keyCode,true);
					break;
			}
		}

		// minimap and playing commands
		if(screen == Screen.MINIMAP || screen == Screen.PLAYING){
			switch(key){
				case 'M':
				case 'm':
					screen = screen == Screen.MINIMAP ? Screen.PLAYING : Screen.MINIMAP;
					break;
			}
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

	private enum Screen{
		PLAYING,
		MINIMAP,
		TITLE_SCREEN,
	}
}

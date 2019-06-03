import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet{
	private Character character;
	private Maze maze;
	private List<Character> movables;
	private List<Renderable> renderables;
	private boolean isGodMode;
	private Screen screen;
	private EndScreen endScreen;
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
		size(1000, 1000);
		titleScreen = new TitleScreen();
		endScreen = new EndScreen();
		renderables = new ArrayList<>();
		movables = new ArrayList<>();
	}

	@Override
	public void draw(){
		switch(screen){
			case TITLE_SCREEN:
				background(255);
				titleScreen.render();
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
				movables.forEach(Character::move);
				break;
			case END:
				background(255);
				endScreen.render();
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
	public void mouseClicked() {
		if(screen == Screen.TITLE_SCREEN){
			titleScreen.click();
		}else if(screen == Screen.END){
			endScreen.click();
		}
	}

	@Override
	public void keyReleased(){
		if(screen == Screen.PLAYING){
			character.setVelocity(keyCode,false);
		}
	}

	public void startGame(int rows, int columns, int amountMonsters){
		// for when we restart the game
		renderables.clear();
		movables.clear();

		maze = new Maze(rows, columns, this);
		character = new Character(maze);
		movables.add(character);
		renderables.add(character);
		for(int i = 0; i<amountMonsters; i++){
			Monster monster = new Monster((int)(Math.random()*maze.getWidth()),(int)(Math.random()*maze.getLength()),maze, character);
			movables.add(monster);
			renderables.add(monster);
		}
		renderables.add(maze);
		screen = Screen.PLAYING;
	}

	public List<Character> getMovables(){
		return movables;
	}

	public void endGame(EndScreen.EndType endType){
		screen = Screen.END;
		endScreen.show(endType);
	}

	public void bringToTitleScreen(){
		titleScreen = new TitleScreen();
		endScreen = new EndScreen();
		this.screen = Screen.TITLE_SCREEN;
	}
	
	private enum Screen{
		PLAYING,
		MINIMAP,
		TITLE_SCREEN,
		END
	}
}

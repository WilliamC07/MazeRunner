import processing.core.PApplet;
import java.util.*;
public class Main extends PApplet{
	private Character player;
	private Maze maze;
	@Override
	public void settings(){
		size(500, 500);
		player = new Character(new Point(width/2,height/2),this);
	}
	public void draw(){
		background(255);
		player.render();
	}
	public void keyPressed(){
		if(key=='W' || key=='w'){
			player.move((float)0,(float)-5);
		} else if(key=='S' || key=='s'){
			player.move((float)0,(float)5);
		} else if(key=='A' || key=='a'){
			player.move((float)-5,(float)0);
		} else if(key=='D' || key=='d'){
			player.move((float)5,(float)0);
		}
	}
	public static void main(String[] args){
		PApplet.main("Main");
	}
}

import processing.core.PApplet;
import java.util.*;
public class Main extends PApplet{
	private ArrayList<Renderable> graphics;
	@Override
	public void settings(){
		size(500, 500);
		graphics = new ArrayList<Renderable>();
		graphics.add(new Character(new Point(width/2,height/2),this));
	}
	public void draw(){
		background(255);
		for(Renderable graphic : graphics){
			graphic.render();
		}
	}
	public static void main(String[] args){
		PApplet.main("Main");
	}
}

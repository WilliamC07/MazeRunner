import processing.core.PApplet;

import java.util.ArrayList;

public class Main extends PApplet{
	private ArrayList<Renderable> renderables;

	public static void main(String[] args){
		PApplet.main("Main");
	}

	@Override
	public void settings(){
		size(1000, 500);
	}

	@Override
	public void setup(){

	}

	@Override
	public void draw(){
		fill(255);

	}
}

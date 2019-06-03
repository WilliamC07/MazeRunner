import java.util.*;
import processing.core.PApplet;
public class Monster implements Renderable{
    private ArrayList<Cell> path;
    private int x,y;
    private PApplet sketch;
    private Maze maze;
    public Monster(int x, int y, Maze maze){
        this.x=x;
        this.y=y;
        this.maze=maze;
        sketch = Main.getInstance();
    }
    public void render(){
        sketch.noStroke();
        sketch.fill(160,160,160);
        sketch.ellipse(x,y,20,20);
    }
}
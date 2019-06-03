import java.util.*;
import processing.core.PApplet;
public class Monster extends Character{
    private ArrayList<Cell> path;
    private int x,y;
    private boolean chase;
    public Monster(int x, int y, Maze maze){
        super(maze);
        this.x=x;
        this.y=y;
        chase = false;
    }
    public void move(){
        if(chase){

        } else {
            
        }
    }
    public void render(){
        sketch.noStroke();
        sketch.fill(160,160,160);
        sketch.ellipse(x,y,20,20);
    }
}
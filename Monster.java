import java.util.*;
import processing.core.PApplet;
public class Monster extends Character{
    private ArrayList<Cell> path;
    private int x,y;
    private boolean chase;
    private PApplet sketch;
    private Maze maze;
    public Monster(int x, int y, Maze maze){
        super(maze);
        System.out.println(x+" "+y);
        this.x=x;
        this.y=y;
        this.maze=maze;
        sketch=Main.getInstance();
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
        sketch.ellipse(x*maze.WALL_SCALE+maze.getOffsetX()+maze.WALL_SCALE/2,y*maze.WALL_SCALE+maze.getOffsetY()+maze.WALL_SCALE/2,20,20);
    }
}
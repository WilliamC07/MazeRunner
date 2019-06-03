import java.util.*;
import processing.core.PApplet;
public class Monster extends Character{
    private ArrayList<Cell> path;
    private int x,y;
    private boolean chase;
    private PApplet sketch;
    private Maze maze;
    private float matrixX, matrixY;
    private Character player;
    public Monster(int x, int y, Maze maze, Character player){
        super(maze);
        this.x=x;
        this.y=y;
        this.maze=maze;
        sketch=Main.getInstance();
        chase = false;
        path = generateRandomPath();
        matrixX = x*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
        matrixY = y*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
        this.player = player;
    }
    public void move(){
        int velocity;
        if(chase){
            velocity = 25;
        } else {
            velocity = 100;
        }
        if(path.size()!=0){
            Cell next = path.get(path.size()-1);
            float nextMatrixX = next.getY()*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
            float nextMatrixY = next.getX()*Maze.WALL_SCALE+Maze.WALL_SCALE/2;
            if(matrixX==nextMatrixX && matrixY==nextMatrixY){
                path.remove(path.size()-1);
            } else {
                matrixX += (nextMatrixX-matrixX>0? 1:(nextMatrixX-matrixX==0? 0:-1))*Maze.WALL_SCALE/velocity;
                matrixY += (nextMatrixY-matrixY>0? 1:(nextMatrixY-matrixY==0? 0:-1))*Maze.WALL_SCALE/velocity;
                int[] matrixPoint = Maze.getMatrixPoint(new Point(matrixX,matrixY));
                x = matrixPoint[0]/2;
                y = matrixPoint[1]/2;
            }
        } else {
            path = generateRandomPath();
        }
    }
    public void render(){
        sketch.noStroke();
        sketch.fill(160,160,160);
        sketch.ellipse(matrixX+maze.getOffsetX(),matrixY+maze.getOffsetY(),20,20);
        sketch.fill(0,0,0);
        sketch.text(player.canSeeCharacter(player,this)+"",matrixX+maze.getOffsetX(),matrixY+maze.getOffsetY());
        if(player.canSeeCharacter(player,this)){
            chase=true;
            int[] playerPos = Maze.getMatrixPoint(player.getPos());
            ArrayList<Cell> tempPath = maze.solve(y,x,playerPos[1]/2,playerPos[0]/2);
            if(tempPath.size()>0){
                tempPath.remove(tempPath.size()-1);
            }
            path = tempPath;
        } else if(chase){
            chase=false;
            path = generateRandomPath();
        }
        for(Cell cell : path){
            sketch.fill(0,255,0);
            sketch.ellipse(cell.getY()*50f+maze.getOffsetX()+25,cell.getX()*50f+maze.getOffsetY()+25,20,20);
        }
    }
    public ArrayList<Cell> generateRandomPath(){
        ArrayList<Cell> returnPath = maze.solve(y,x,(int)(Math.random()*maze.getLength()),(int)(Math.random()*maze.getWidth()));
        if(returnPath.size()>0){
            returnPath.remove(returnPath.size()-1);
        }
        return returnPath;
    }
    public Point location(){
        return new Point(matrixX+maze.getOffsetX(), matrixY+maze.getOffsetY());
    }
}
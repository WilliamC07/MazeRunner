import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
public class Maze implements Renderable{
    private Wall[][] walls;
    private int length;
    private int width;
    public Maze(int rows, int cols, PApplet sketch){
        length = rows;
        width = cols;
        float screenWidth = sketch.width;
        float screenHeight = sketch.height;
        walls = new Wall[2*rows-1][];
        float rowWidth = 4*screenHeight/5/rows;
        float colWidth = 4*screenWidth/5/cols;
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[cols-1];

            } else {
                walls[i] = new Wall[cols];
            }
            for(int j = 0; j<cols-(i%2==0? 1:0); j++){
                float startX,startY,endX,endY;
                if(i%2==0){
                    startX = screenWidth/10+(j+1)*colWidth;
                    startY = screenHeight/10+(i/2)*rowWidth+1;
                    endX = startX;
                    endY = screenHeight/10+(i/2+1)*rowWidth-1;
                } else {
                    startX = screenWidth/10+j*colWidth+1;
                    startY = screenHeight/10+(i/2+1)*rowWidth;
                    endX = screenWidth/10+(j+1)*colWidth-1;
                    endY = startY;
                }
                Point start = new Point(startX,startY);
                Point end = new Point(endX,endY);
                walls[i][j] = new Wall(start,end);
            }
        }
        generate(0,0);
    }
    private boolean unvisited(int row, int col){
        if(col!=0 && walls[2*row][col-1]==null){
            return false;
        }
        if(col!=width-1 && walls[2*row][col]==null){
            return false;
        }
        if(row!=0 && walls[2*row-1][col]==null){
            return false;
        }
        if(row!=length-1 && walls[2*row+1][col]==null){
            return false;
        }
        return true;
    }
    public void generate(int row, int col){
        ArrayList<Integer> adjacentWalls = new ArrayList<Integer>();
        if(col!=0 && walls[2*row][col-1]!=null){
            adjacentWalls.add(0); //left
        }
        if(col!=width-1 && walls[2*row][col]!=null){
            adjacentWalls.add(1); //right
        }
        if(row!=0 && walls[2*row-1][col]!=null){
            adjacentWalls.add(2); //up
        }
        if(row!=length-1 && walls[2*row+1][col]!=null){
            adjacentWalls.add(3); //down
        }
        Collections.shuffle(adjacentWalls);
        for(Integer direction : adjacentWalls){
            if(direction==0 && unvisited(row,col-1)){
                walls[2*row][col-1]=null;
                generate(row,col-1);
            }
            if(direction==1 && unvisited(row,col+1)){
                walls[2*row][col]=null;
                generate(row,col+1);
            }
            if(direction==2 && unvisited(row-1,col)){
                walls[2*row-1][col]=null;
                generate(row-1,col);
            }
            if(direction==3 && unvisited(row+1,col)){
                walls[2*row+1][col]=null;
                generate(row+1,col);
            }
        }
    }
    @Override
    public void render(){
        for(Wall[] row : walls){
            for(Wall wall : row){
                if(wall!=null){
                    wall.render();
                }
            }
        }
    }
}

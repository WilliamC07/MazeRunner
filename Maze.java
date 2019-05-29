import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
public class Maze implements Renderable{
    private ArrayList<Wall> flatMaze;
    private boolean[][] maze;
    private int length;
    private Wall[][] wallsFormatted;
    private int width;
    private int trueHeight, trueWidth;
    private PApplet sketch;

    /**
     * Each cell in a wall 2D array is a square of this side length in pixels
     */
    private final float WALL_SCALE = 50;
    /**
     * Offset is so the edge of the maze doesn't touch the border of the window
     */
    private final float OFF_SET_Y = 50;
    private final float OFF_SET_X = 50;

    public Maze(int rows, int cols, PApplet sketch){
        length = rows;
        width = cols;
        this.sketch = sketch;
        Wall[][] walls = new Wall[2*rows-1][];
        this.trueHeight = 2*length-1;
        this.trueWidth = 2*width-1;
        maze = new boolean[trueHeight][trueWidth];
        fillWalls(length,width,walls);
        generate(0,0,walls);
        convertToBool(walls);
        convertToList(walls);

        for(int r = 0; r < maze.length; r++){
            for(int c = 0; c < maze[r].length; c++){
                if(maze[r][c]){
                    System.out.print("#");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        System.out.println("my generated");
        Wall[][] w = generateWallFormatted(maze);
        for(int r = 0; r < trueHeight; r++){
            for(int c = 0; c < trueWidth; c++){
                Wall here = w[r][c];
                if(here != null){
                    System.out.print("#");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        System.out.println("finished");


    }
    public void fillWalls(int rows, int cols, Wall[][] walls){
        float rowWidth = 4f*sketch.height/5/rows;
        float colWidth = 4f*sketch.width/5/cols;
        for(int i = 0; i<walls.length; i++){
            if(i%2==0){
                walls[i] = new Wall[cols-1];

            } else {
                walls[i] = new Wall[cols];
            }
            for(int j = 0; j<cols-(i%2==0? 1:0); j++){
                float startX,startY,endX,endY;
                if(i%2==0){
                    startX = sketch.width/10f+(j+1)*colWidth;
                    startY = sketch.height/10f+(i/2)*rowWidth+1;
                    endX = startX;
                    endY = sketch.height/10f+(i/2+1)*rowWidth-1;
                } else {
                    startX = sketch.width/10f+j*colWidth+1;
                    startY = sketch.height/10f+(i/2+1)*rowWidth;
                    endX = sketch.width/10f+(j+1)*colWidth-1;
                    endY = startY;
                }
                Point start = new Point(startX,startY);
                Point end = new Point(endX,endY);
                walls[i][j] = new Wall(start,end);
            }
        }
    }
    public void generate(int row, int col, Wall[][] walls){
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
            if(direction==0 && unvisited(row,col-1,walls)){
                walls[2*row][col-1]=null;
                generate(row,col-1,walls);
            }
            if(direction==1 && unvisited(row,col+1,walls)){
                walls[2*row][col]=null;
                generate(row,col+1,walls);
            }
            if(direction==2 && unvisited(row-1,col,walls)){
                walls[2*row-1][col]=null;
                generate(row-1,col,walls);
            }
            if(direction==3 && unvisited(row+1,col,walls)){
                walls[2*row+1][col]=null;
                generate(row+1,col,walls);
            }
        }
    }
    private boolean unvisited(int row, int col, Wall[][] walls){
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
    private void convertToBool(Wall[][] walls){
        for(int i = 0; i < walls.length; i++){
            for(int j = 0; j < walls[i].length; j++){
                if(walls[i][j]!=null){
                    if(i%2==0){
                        maze[i][j*2+1]=true;
                        if(i<walls.length-1 && walls[i+2][j]!=null){
                            maze[i+1][j*2+1]=true;
                        }
                    } else {
                        maze[i][j*2]=true;
                        if(j<length-1 && (walls[i-1][j]!=null || walls[i][j+1]!=null)){
                            maze[i][j*2+1]=true;
                        }
                        if(j>0 && walls[i-1][j-1]!=null){
                            maze[i][j*2-1]=true;
                        }
                        if(j<length-1 && walls[i+1][j]!=null){
                            maze[i][j*2+1]=true;
                        }
                        if(j>0 && walls[i+1][j-1]!=null){
                            maze[i][j*2-1]=true;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze[i].length; j++){
                if(maze[i][j]){
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    public void convertToList(Wall[][] walls){
        flatMaze = new ArrayList<Wall>();
        for(int i = 0; i < walls.length; i++){
            for(int j = 0; j < walls[i].length; j++){
                if(walls[i][j]!=null){
                    Point start = walls[i][j].getStart();
                    Point end = walls[i][j].getEnd();
                    if(i%2==0){
                        int index = i;
                        while(index < walls.length && walls[index][j]!=null){
                            end = walls[index][j].getEnd();
                            walls[index][j]=null;
                            index+=2;
                        }
                    } else {
                        int index = j;
                        while(index < walls[i].length && walls[i][index]!=null){
                            end = walls[i][index].getEnd();
                            walls[i][index]=null;
                            index++;
                        }
                    }
                    flatMaze.add(new Wall(start,end));
                }
            }
        }
    }
    public boolean[][] getBool(){
        return maze;
    }
    public ArrayList<Wall> getFlat(){
        return flatMaze;
    }
    public int getLength(){
        return length;
    }
    public int getWidth(){
        return width;
    }

    private Wall[][] generateWallFormatted(boolean[][] walls){
        Wall[][] output = new Wall[trueHeight][trueWidth];

        // create horizontal walls
        for(int r = 0; r < trueHeight; r++){
            for(int c = 0; c < trueWidth;){
                // (make sure there is a wall at the spot (false means no wall)) and
                // (there is a wall to the right or a border)
                if(!(walls[r][c] && (c + 1 == trueWidth || walls[r][c+1]))){
                    c++;
                    continue;
                }

                Point startPoint;
                Point endPoint;
                // if a horizontal wall is next to the border, it needs to be extended to touch it
                float startY = OFF_SET_Y + r * WALL_SCALE + // Top edge
                               WALL_SCALE / 2;              // to center
                if(c == 0){
                    // border to left
                    float x = OFF_SET_X + c * WALL_SCALE - // left edge
                            WALL_SCALE / 2;                // to reach border
                    startPoint = new Point(x, startY);
                }else if(c == trueWidth - 1){
                    // border to right
                    float x = OFF_SET_X + c * WALL_SCALE + // left edge
                            WALL_SCALE / 2;                // to reach border
                    startPoint = new Point(x, startY);
                }else{
                    // start at middle of the cell if not adjacent to border
                    startPoint = middleOfCellPoint(r, c);
                }

                // if there is a wall to the right, there must be a continuous horizontal wall
                // find how many cells it spans
                int endColumn = c;
                while(endColumn < trueWidth && walls[r][endColumn]){
                    endColumn++;
                }

                // if the end wall is next to the border, it needs to be extended. it can only touch the right border
                if(endColumn == trueWidth - 1){
                    float x = OFF_SET_X + c * WALL_SCALE + // left edge
                            WALL_SCALE / 2;                // to reach border
                    float y = OFF_SET_Y + r * WALL_SCALE;
                    endPoint = new Point(x, y);
                }else{
                    // end at the midpoint
                    endPoint = middleOfCellPoint(r, endColumn);
                }

                Wall wall = new Wall(startPoint, endPoint);
                // fill up the 2d array
                for(int startColumn = c; startColumn < endColumn; startColumn++){
                    output[r][startColumn] = wall;
                }

                c = endColumn + 1;
            }
        }

        // create vertical walls
        for(int c = 0; c < trueWidth; c++){
            for(int r = 0; r < trueHeight;){
                if(!(walls[r][c] && (r + 1 == trueHeight || walls[r+1][c]))){
                    r++;
                    continue;
                }

                Point startPoint = null;
                Point endPoint = null;

                // TODO: End point and start point



            }
        }

        return output;
    }

    private Point middleOfCellPoint(int row, int column){
        float x = OFF_SET_X + column * WALL_SCALE +    // left edge of cell horizontally
                  WALL_SCALE / 2;                      // center horizontally
        float y = OFF_SET_Y + row * WALL_SCALE +       // top edge of cell vertically
                  WALL_SCALE / 2;                      // center vertically
        return new Point(x, y);

    }

    public Wall[][] getWallsFormatted(){
        return this.wallsFormatted;
    }

    @Override
    public void render(){
        sketch.stroke(0);
        sketch.line(sketch.width/10,sketch.height/10,9*sketch.width/10,sketch.height/10);
        sketch.line(sketch.width/10,sketch.height/10,sketch.width/10,9*sketch.height/10);
        sketch.line(9*sketch.width/10,sketch.height/10,9*sketch.width/10,9*sketch.height/10);
        sketch.line(sketch.width/10,9*sketch.height/10,9*sketch.width/10,9*sketch.height/10);
        for(Wall wall : flatMaze){
            wall.render();
        }
    }
}

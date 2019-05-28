import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
public class Maze implements Renderable{
    private ArrayList<Wall> flatMaze;
    private boolean[][] maze;
    private int length;
    private int width;
    private PApplet sketch;
    public Maze(int rows, int cols, PApplet sketch){
        length = rows;
        width = cols;
        this.sketch = sketch;
        Wall[][] walls = new Wall[2*rows-1][];
        maze = new boolean[2*length-1][2*width-1];
        fillWalls(length,width,walls);
        generate(0,0,walls);
        convertToBool(walls);
        convertToList(walls);
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
    private boolean contains(ArrayList<Cell> list, Cell target){
        for(Cell check : list){
            if(check.getX()==target.getX() && check.getY()==target.getY()){
                return true;
            }
        }
        return false;
    }
    private Cell find(ArrayList<Cell> list, Cell target){
        for(Cell check : list){
            if(check.getX()==target.getX() && check.getY()==target.getY()){
                return check;
            }
        }
        return null;
    }
    public ArrayList<Cell> solve(int startX, int startY, int endX, int endY){
        ArrayList<Cell> closed = new ArrayList<Cell>();
        ArrayList<Cell> open = new ArrayList<Cell>();
        ArrayList<Cell> path = new ArrayList<Cell>();
        open.add(new Cell(startX,startY));
        while(open.size()>0){
            open = Collections.sort(open,Collections.reverseOrder());
            Cell current = open.remove(open.size()-1);
            closed.add(current);
            if(current.getX()==endX && current.getY()==endY){
                break;
            }
            ArrayList<Cell> adjacent = new ArrayList<Cell>();
            if(current.getX()-1>0 && !maze[2*current.getX()-1][2*current.getY()]){
                adjacent.add(new Cell(current.getX()-1,current.getY(),endX,endY,current));
            }
            if(current.getX()+1<width && !maze[2*current.getX()+1][2*current.getY()]){
                adjacent.add(new Cell(current.getX()+1,current.getY(),endX,endY,current));
            }
            if(current.getY()-1>0 && !maze[2*current.getX()][2*current.getY()+1]){
                adjacent.add(new Cell(current.getX(),current.getY()-1,endX,endY,current));
            }
            if(current.getY()-1<length && !maze[2*current.getX()][2*current.getY()-1]){
                adjacent.add(new Cell(current.getX(),current.getY()+1,endX,endY,current));
            }
            for(Cell neighbor : adjacent){
                if(contains(closed,neighbor) && find(closed,neighbor).getF()<=neighbor.getF()){
                    continue;
                }
                if(contains(open,neighbor) && find(open,neighbor).getF()<=neighbor.getF()){
                    continue;
                }
                open.add(neighbor);
            }
        }
        Cell visited = closed.get(closed.size()-1);
        while(visited.hasParent()){
            path.add(visited);
            visited = visited.getParent();
        }
        return path;
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
    private class Cell extends Comparable<Cell>{
        int x,y;
        int f,g,h;
        Cell parent;
        public Cell(int x, int y){
            this.x=x;
            this.y=y;
            f = 0;
            g = 0;
            h = 0;
        }
        public Cell(int x, int y, int goalX, int goalY, Cell parent){
            this.x=x;
            this.y=y;
            this.parent=parent;
            h = Math.abs(goalX-x)+Math.abs(goalY-y);
            g = parent.getG()+1;
            f = g+h;

        }
        public int getG(){
            return g;
        }
        public int getF(){
            return f;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public int compareTo(Cell other){
            return f-other.getF();
        }
        public Cell getParent(){
            return parent;
        }
        public boolean hasParent(){
            return parent==null;
        }
    }
}

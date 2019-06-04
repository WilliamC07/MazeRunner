import processing.core.PApplet;

import java.util.*;

public class Maze implements Renderable{
    private List<Wall> flatMaze = new ArrayList<>();
    private boolean[][] maze;
    private int length;
    private int width;
    private int trueHeight, trueWidth;
    private PApplet sketch;
    private float offsetX,offsetY;
    private Cell pathKeeper;
    /**
     * Each cell in a wall 2D array is a square of this side length in pixels
     */
    public static final float WALL_SCALE = 50;

    private ArrayList<Cell> hint;
    public Maze(int rows, int cols, PApplet sketch){
        length = rows;
        width = cols;
        this.sketch = sketch;
        hint = new ArrayList<Cell>();
        Wall[][] walls = new Wall[2*rows-1][];
        this.trueHeight = 2*length-1;
        this.trueWidth = 2*width-1;
        pathKeeper = new Cell(0,0);
        maze = new boolean[trueHeight][trueWidth];
        fillWalls(length,width,walls);
        generate(0,0,walls);
        convertToBool(walls);
        convertToList(walls);
    }
    private void fillWalls(int rows, int cols, Wall[][] walls){
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
    private void generate(int row, int col, Wall[][] walls){
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
                        if(j<walls[i].length-1 && (walls[i-1][j]!=null || walls[i][j+1]!=null)){
                            maze[i][j*2+1]=true;
                        }
                        if(j>0 && walls[i-1][j-1]!=null){
                            maze[i][j*2-1]=true;
                        }
                        if(j<walls[i].length-1 && walls[i+1][j]!=null){
                            maze[i][j*2+1]=true;
                        }
                        if(j>0 && walls[i+1][j-1]!=null){
                            maze[i][j*2-1]=true;
                        }
                    }
                }
            }
        }
    }
    public void convertToList(Wall[][] walls){
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
                }
            }
        }
    }
    public boolean[][] getBool(){
        return maze;
    }
    public int getLength(){
        return length;
    }
    public int getWidth(){
        return width;
    }

    /**
     * Adds the walls of the maze to {@link #flatMaze}. This does not include the border. Use {@link #generateBorder(float, float)} )}
     * for that.
     */
    private void generateFormattedWalls(float offsetX, float offsetY, float horizontalWidth, float verticalWidth){
        // IMPORTANT:
        // every other column is a wall, so divide by 2 and add 1
        // every other row is a wall, so divide by 2 and add 1

        // create horizontal walls
        for(int r = 0; r < trueHeight; r++){
            for(int c = 0; c < trueWidth;){
                if(!maze[r][c] ||  // continue if there is not wall
                   !(c == 0 || c == trueWidth - 1 || maze[r][c + 1])){  // continue if the wall is not adjacent to a border or next to another wall
                    c++;
                    continue;
                }

                // because the wall is horizontal, the start and end point share the same y
                float y = offsetY + (r / 2 + 1) * verticalWidth;
                Point start = new Point(offsetX + ((c + 1) / 2) * horizontalWidth, y);
                Point end;

                // find the span of the wall
                int endColumn = c;
                while(endColumn < trueWidth && maze[r][endColumn]){
                    endColumn++;
                }
                end = new Point(offsetX + ((endColumn + 1) / 2) * horizontalWidth, y);

                // keep track of the wall
                Wall wall = new Wall(start, end);
                flatMaze.add(wall);
                c = endColumn + 1;
            }
        }

        // create vertical walls
        for(int c = 0; c < trueWidth; c++) {
            for (int r = 0; r < trueHeight; ) {
                if (!maze[r][c] ||  // continue if there is no wall
                    !(r == 0 || r == trueHeight - 1 || maze[r + 1][c])) {  // continue if the wall is not adjacent to a border or another vertical wall
                    r++;
                    continue;
                }

                // because the wall is vertical, the start and end share the same x
                float x = offsetX + (c / 2 + 1) * horizontalWidth;
                Point start = new Point(x,offsetY + ((r + 1) / 2) * verticalWidth);
                Point end;

                // find the span of the wall
                int endRow = r;
                while(endRow < trueHeight && maze[endRow][c]){
                    endRow++;
                }
                end = new Point(x, offsetY + ((endRow + 1) / 2) * verticalWidth);

                // keep track of the wall
                Wall wall = new Wall(start, end);
                flatMaze.add(wall);
                r = endRow + 1;
            }
        }
    }

    /**
     * Generates all the intersections and endpoints of the walls of the maze including the border.
     * @return Unique intersection and endpoints to be used by ray casting.
     */
    public Set<Point> verticies(float offsetX, float offsetY){
        Set<Point> points = new HashSet<>();

        // add all endpoints of the walls
        for (Wall wall : flatMaze) {
            points.add(wall.getStart());
            points.add(wall.getEnd());
        }

        // add all the intersection
        for(int r = 0; r < trueHeight; r++){
            for(int c = 0; c < trueWidth; c++){
                // and intersection is one where a point is part of a wall that is both vertical and horizontal
                if(maze[r][c] && //must be part of a wall
                        ((r != 0 && maze[r - 1][c]) || (r != trueHeight - 1 && maze[r + 1][c])) && // need a wall on top or bottom
                        ((c != 0 && maze[r][c-1]) || (c != trueWidth - 1 && maze[r][c+1]))){ // need a wall on left or right

                    float x = offsetX + ((c + 1) / 2) * WALL_SCALE;
                    float y = offsetY + (r / 2 + 1) * WALL_SCALE;
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    private Wall[] generateBorder(float offsetX, float offsetY, float horizontalWidth, float verticalWidth){
        Wall[] borders = new Wall[4];

        // border end points
        Point topLeft = new Point(offsetX, offsetY);
        Point topRight = new Point(offsetX + width * horizontalWidth, offsetY);
        Point bottomLeft = new Point(offsetX, offsetY + length * verticalWidth);
        Point bottomRight = new Point(topRight.getX(), bottomLeft.getY());

        // top border
        flatMaze.add(new Wall(topLeft, topRight));
        // left border
        flatMaze.add(borders[1] = new Wall(topLeft, bottomLeft));
        // bottom border
        flatMaze.add(borders[2] = new Wall(bottomLeft, bottomRight));
        // right border
        flatMaze.add(borders[3] = new Wall(bottomRight, topRight));

        return borders;
    }

    public List<Wall> getWalls(){
        return flatMaze;
    }

    public void refresh(float offsetX, float offsetY, float horizontalWidth, float verticalWidth){
        flatMaze.clear();
        // add the walls to the flatMaze
        generateFormattedWalls(offsetX, offsetY, horizontalWidth, verticalWidth);
        // add the border to the flatMaze
        generateBorder(offsetX, offsetY, horizontalWidth, verticalWidth);
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
            Collections.sort(open,Collections.reverseOrder());
            Cell current = open.remove(open.size()-1);
            closed.add(current);
            if(current.getX()==endX && current.getY()==endY){
                while(current.hasParent()){
                    path.add(current);
                    current = current.getParent();
                }
                path.add(current);
                break;
            }
            ArrayList<Cell> adjacent = new ArrayList<Cell>();
            if(current.getX()-1>=0 && !maze[2*current.getX()-1][2*current.getY()]){
                adjacent.add(new Cell(current.getX()-1,current.getY(),endX,endY,current));
            }
            if(current.getX()+1<length && !maze[2*current.getX()+1][2*current.getY()]){
                adjacent.add(new Cell(current.getX()+1,current.getY(),endX,endY,current));
            }
            if(current.getY()-1>=0 && !maze[2*current.getX()][2*current.getY()-1]){
                adjacent.add(new Cell(current.getX(),current.getY()-1,endX,endY,current));
            }
            if(current.getY()+1<width && !maze[2*current.getX()][2*current.getY()+1]){
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
        return path;
    }
    public void hint(Point position, int pathLength){
        int[] playerPoint = getMatrixPoint(position);
        ArrayList<Cell> fullPath = solve(playerPoint[1]/2, playerPoint[0]/2, length-1, width-1);
        hint = new ArrayList<Cell>();
        for(int i = 0; i<fullPath.size()-1 && i<pathLength; i++){
            hint.add(fullPath.get(fullPath.size()-i-2));
        }
    }
    @Override
    public void render(){
        sketch.stroke(0);
        for(Wall wall : flatMaze){
            wall.render();
        }
        sketch.noStroke();
        sketch.fill(255,0,0,100);
        sketch.square(offsetX + WALL_SCALE/2,offsetY + WALL_SCALE/2,WALL_SCALE);
        sketch.fill(0,255,0,100);
        sketch.square(offsetX + (width-1) * WALL_SCALE + WALL_SCALE/2,offsetY + (length-1) * WALL_SCALE + WALL_SCALE/2,WALL_SCALE);
        sketch.stroke(0,255,0);
        sketch.strokeWeight(5);
        float startX;
        float startY;
        float endX;
        float endY;
        for(int i = 0; i<hint.size()-1; i++){
            if(hint.get(i)!=null){
                startX = WALL_SCALE/2f+hint.get(i).getY()*WALL_SCALE+offsetX;
    		    startY = WALL_SCALE/2f+hint.get(i).getX()*WALL_SCALE+offsetY;
                endX = WALL_SCALE/2f+hint.get(i+1).getY()*WALL_SCALE+offsetX;
                endY = WALL_SCALE/2f+hint.get(i+1).getX()*WALL_SCALE+offsetY;
                sketch.line(startX,startY,endX,endY);
            }
        }
        sketch.strokeWeight(1);
    }

    /**
     * Convert the character's position to the matrix representation.
     * @return [row, column]
     */
    public static int[] getMatrixPoint(Point location){
        return new int[]{
                (int) (location.getX() / WALL_SCALE) * 2,
                (int) (location.getY() / WALL_SCALE) * 2
        };
    }
    public void setOffsetX(float offsetX){
        this.offsetX=offsetX;
    }
    public void setOffsetY(float offsetY){
        this.offsetY=offsetY;
    }
    public void renderMinimap(){
        Cell copy = pathKeeper;
        sketch.stroke(0,255,0);
        while(copy.hasParent()){
            float startX = sketch.width/10f+2f*sketch.width/5/width+copy.getX()*4f*sketch.width/5/width;
            float startY = sketch.height/10f+2f*sketch.height/5/length+copy.getY()*4f*sketch.height/5/length;
            float endX = sketch.width/10f+2f*sketch.width/5/width+copy.getParent().getX()*4f*sketch.width/5/width;
            float endY = sketch.height/10f+2f*sketch.height/5/length+copy.getParent().getY()*4f*sketch.height/5/length;
            sketch.line(startX,startY,endX,endY);
            copy = copy.getParent();
        }
        sketch.noStroke();
        sketch.fill(255,0,0);
        float playerX = sketch.width/10f+2f*sketch.width/5/width+pathKeeper.getX()*4f*sketch.width/5/width;
        float playerY = sketch.height/10f+2f*sketch.height/5/length+pathKeeper.getY()*4f*sketch.height/5/length;
        sketch.ellipse(playerX,playerY,15,15);
    }
    public void renderMinimapGod(){
        refresh(sketch.width/10f,sketch.height/10f,4f*sketch.width/5/width,4f*sketch.height/5/length);
        sketch.stroke(0);
        for(Wall wall : flatMaze){
            wall.render();
        }
    }
    public int[] getPathKeeper(){
        int[] cell = new int[2];
        cell[0] = pathKeeper.getX();
        cell[1] = pathKeeper.getY();
        return cell;
    }
    public float getOffsetX(){
        return offsetX;
    }
    public float getOffsetY(){
        return offsetY;
    }
    public void updateCell(int x, int y){
        pathKeeper = new Cell(x,y,pathKeeper);
    }
}

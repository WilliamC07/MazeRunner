public class Cell implements Comparable<Cell>{
    private int x,y;
    private int f,g,h;
    private Cell parent;
    public Cell(int x, int y){
        this.x=x;
        this.y=y;
        f = 0;
        g = 0;
        h = 0;
    }
    public Cell(int x, int y, Cell parent){
        this.x=x;
        this.y=y;
        this.parent = parent;
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
        return parent!=null;
    }
}
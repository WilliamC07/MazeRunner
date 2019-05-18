/**
 * A wall is just a line segment that the user cannot walk through
 */
public class Wall implements Renderable{
    /**
     * The first point of the wall.
     */
    private Point start;
    /**
     * The second point of the wall.
     */
    private Point end;

    /**
     * Constructor for Wall.
     * @param start Start point of the wall.
     * @param end End point of the wall.
     */
    public Wall(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    @Override
    public void render(){
        Main.getInstance().line(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @Override
    public int hashCode(){
        // pretty bad, i should learn how to make a effective one
        return (int) start.getX() * 1000 + (int) start.getY() * 100 + (int) end.getX() * 10 + (int) end.getY();
    }
}
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

    }
}
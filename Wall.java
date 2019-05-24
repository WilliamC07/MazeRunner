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

    private final Direction direction;


    /**
     * Constructor for Wall.
     * @param start Start point of the wall.
     * @param end End point of the wall.
     */
    public Wall(Point start, Point end){
        this.start = start;
        this.end = end;

        if(start.getX() == end.getX()){
            direction = Direction.VERTICAL;
        }else if(start.getY() == end.getY()){
            direction = Direction.HORIZONTAL;
        }else{
            throw new IllegalStateException(String.format("Cannot draw this line.\nStart: %s\nEnd: %s", start, end));
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wall wall = (Wall) o;
        return start.equals(wall.start) &&
                end.equals(wall.end);
    }

    @Override
    public String toString() {
        return "Wall{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean shareCommonEnd(Wall other){
        return other.end.equals(end) || other.end.equals(start) ||
                other.start.equals(end) || other.start.equals(start);
    }

    public boolean areDistinct(Wall other){
        return !other.end.equals(end) && !other.end.equals(start) &&
                !other.start.equals(end) && !other.start.equals(start);
    }

    /**
     * Determines if the point is part of the line segment.
     * @param p Point to check if it is part of this wall
     * @return True if the point is an endpoint, false otherwise
     */
    public boolean isAEndPoint(Point p){
        return start.equals(p) || end.equals(p);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    private enum Direction{
        HORIZONTAL,
        VERTICAL
    }
}
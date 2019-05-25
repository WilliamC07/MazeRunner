/**
 * A wall is just a line segment that the user cannot walk through
 */
public class Wall implements Renderable{
    /**
     * The first point of the wall.
     * If the wall is vertical, the y value is less than the y value of {@link #end}. (Top of)
     * If the wall is horizontal, the x value is less than the x value of {@link #end}. (Left of)
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
        if(start.getX() == end.getX()){
            direction = Direction.VERTICAL;
            // order points such that start is above the end
            if(start.getY() > end.getY()){
                this.start = end;
                this.end = start;
            }else{
                this.start = start;
                this.end = end;
            }
        }else if(start.getY() == end.getY()){
            direction = Direction.HORIZONTAL;
            if(start.getX() > end.getX()){
                this.start = end;
                this.end = start;
            }else{
                this.start = start;
                this.end = end;
            }
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

    public boolean isBetweenTwoPoints(Point a, Point b){
        switch(direction){
            case VERTICAL:
                return start.getX() == a.getX() && start.getX() == b.getX();
            case HORIZONTAL:
                return start.getY() == a.getY() && start.getY() == b.getY();
        }

        throw new IllegalStateException("Direction is invalid");
    }

    public Direction getDirection(){
        return this.direction;
    }

    /**
     * Determines if the point can be found on the wall.
     * @param p Point to check if it lies on this wall
     * @return True if the point is one the wall, false otherwise
     */
    public boolean isPointOnWall(Point p){
        switch(direction){
            case VERTICAL:
                return p.getY() >= start.getY() && p.getY() <= end.getY() && Math.abs(p.getX() - this.getStart().getX()) <= .1;
            case HORIZONTAL:
                return p.getX() >= start.getX() && p.getX() <= end.getX() && Math.abs(p.getY() - this.getStart().getY()) <= .1;
        }

        throw new IllegalStateException("Direction is invalid");
    }

    public enum Direction{
        HORIZONTAL,
        VERTICAL
    }
}
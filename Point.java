import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* The Point class stores an abscissa and ordinate.
*/
public class Point{
    /**
    * The abscissa.
    */
    private float x;
    /**
    * The ordinate.
    */
    private float y;
    /**
    * Constructs a Point given an abscissa and an ordinate.
    *@param x the abscissa for the Point
    *@param y the ordinate for the Point
    */
    public Point(float x, float y){
        this.x=x;
        this.y=y;
    }
    /**
    * Accessor for the abscissa.
    *@return the abscissa of the Point
    */
    public float getX(){
        return x;
    }
    /**
    * Accessor for the ordinate.
    *@return the ordinate of the Point
    */
    public float getY(){
        return y;
    }

    /**
     * Finds the midpoint of the two points.
     * This can overflow and break.
     * @param a A point
     * @param b A point
     * @return The midpoint of a and b.
     */
    public static Point midpoint(Point a, Point b){
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Float.compare(point.x, x) == 0 &&
                Float.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode(){
        return (int) (x * 10_000 + y);
    }

    @Override
    public String toString() {
        return String.format("x: %f y: %f", x, y);
    }
}

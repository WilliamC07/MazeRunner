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
     * Performs the square of the distance between points. This is to avoid doing the useless calculation
     * of sqrt, which is expensive.
     *
     * d^2 = (x1 - x2)^2 + (y1 - y2)^2
     * Note that the "^" means exponent and not the exclusive or symbol in java
     * @return Distance between this point and the other point. This does not calculate square root
     */
    public double distancePortion(Point other){
        return Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2);
    }

    /**
     * Calculates the slope between two points. If the two points are collinear and horizontal, the slope will be a
     * very small number.
     * @param other Other point in the line
     * @return Slope of the line.
     */
    public float slope(Point other){
        float deltaX = this.x - other.x;
        float deltaY = this.y - other.y;

        if(deltaX == 0){
            deltaX = .00001F; // don't want division by 0;
        }

        return deltaY / deltaX;
    }
}

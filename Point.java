public class Point{
    private float x,y;
    public Point(float x, float y){
        this.x=x;
        this.y=y;
    }
    public float getX(){
        return x;
    }
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
}

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
    *{@link Point#x}
    *@return the abscissa of the Point
    */
    public float getX(){
        return x;
    }
    /**
    * Accessor for the ordinate.
    *{@link Point#y}
    *@return the ordinate of the Point
    */
    public float getY(){
        return y;
    }
}

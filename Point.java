/**
* The Point class stores an abscissa and ordinate.
*/
public class Point{
    private float x,y;
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
}

/**
* The Ray class represents a line segment that can be extended infintely.
*/
public class Ray implements Comparable<Ray>{
    /**
    * The Point at the origin of the Ray.
    */
    private Point start;
    /**
    *The Point at the head of the Ray.
    */
    private Point end;
    /**
    * The slope of the Ray in radians.
    */
    private float slope;
    /**
    *Constructs a Ray given start and end Points.
    *@param start the origin of the Ray
    *@param end the head of the Ray
    */
    public Ray(Point start, Point end){
        this.start=start;
        this.end=end;
        slope=(float)Math.atan2(end.getY()-start.getY(),end.getX()-start.getX());
    }
    /**
    *Constructs a Ray given a start Point and a slope in radians.
    *@param start the origin of the Ray
    *@param slope the slope of the Ray in radians
    */
    public Ray(Point start, float slope){
        this.start=start;
        this.slope=slope;
    }
    /**
    *
    *@param wall
    *@return
    */
    public Point intersects(Wall wall){
        return new Point((float)0,(float)0);
    }
    /**
    *
    *@param other the Ray that this is being compared to
    *@return
    */
    public int compareTo(Ray other){
        return 0;
    }
}

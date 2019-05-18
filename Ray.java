public class Ray{
    private Point start;
    private Point end;
    private double slope;

    /**
     * Creates a ray given the start position and degree coming from the character
     * @param start The character's location
     * @param end The end of the line
     */
    public Ray(Point start, Point end){
        this.start = start;
        this.end = end;
    }


}

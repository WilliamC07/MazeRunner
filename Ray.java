public class Ray implements Renderable, Comparable<Ray>{
    private Point start;
    private Point end;
    /**
     * To optimize ray tracing, we are drawing a line segment from the character position to a wall endpoint. However,
     * to prevent a choppy view, we have to draw another ray of a certain degree away from this line segment. Since we
     * only know the certain degree away and not where it will land, we have to make it a ray.
     */
    private boolean isRay;

    /**
     * Creates a ray given the start position and end position from the character.
     * @param start The character's location
     * @param end The end of the line
     */
    public Ray(Point start, Point end){
        this.start = start;
        this.end = end;
        this.isRay = false;
    }

    /**
     * Creates a ray given the start position and the degree of slope
     * @param start Start point of the ray
     * @param slope The slope of the line
     */
    public Ray(Point start, float slope){
        this.start = start;
        this.isRay = true;

        // TODO: the negative slope is BADDDD
        this.end = new Point(start.getX() + 1, start.getY() + slope);
    }

    /**
     * Determines if this wall (using the start and end point) intersects a wall. If there is an intersection, it will
     * return the point of intersection. If there is no intersection, it return nulls
     * @param wall Wall to check if the current ray intersects
     * @return null if no intersection, a point instance if there is an intersection
     */
    public Point intersects(Wall wall){
        float[] result = lineLineIntersectionValues(wall);
        // collinear lines means that we return the wall endpoint that is closest to the ray start point
        // the result is length 3 to differentiate from t and u values returned if the lines are no collinear
        if(result == null){
            return null;
        }

        float t = result[0];
        float u = result[1];
        if(isRay){
            if(u <= 1 && u >= 0){
                Point wallStart = wall.getStart();
                Point wallEnd = wall.getEnd();
                float intersectX = (wallStart.getX() + u * (wallEnd.getX() - wallStart.getX()));
                float intersectY = (wallStart.getY() + u * (wallEnd.getY() - wallStart.getY()));
                return new Point(intersectX, intersectY);
            }
        }else {
            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                // there is an intersection
                float intersectX = (start.getX() + t * (end.getX() - start.getX()));
                float intersectY = (start.getY() + t * (end.getY() - start.getY()));
                return new Point(intersectX, intersectY);
            }
        }

        // no intersection
        return null;
    }

    /**
     * Performs the line-line intersection algorithm.
     * Algorithm: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param wall Wall to check if there is an intersection
     * @return {t, u} if there is an intersection. null if there is no intersection
     */
    private float[] lineLineIntersectionValues(Wall wall){
        // Write out all of this so there is no mistake when implementing the formula
        float x1 = start.getX();
        float x2 = end.getX();
        float x3 = wall.getStart().getX();
        float x4 = wall.getEnd().getX();

        float y1 = start.getY();
        float y2 = end.getY();
        float y3 = wall.getStart().getY();
        float y4 = wall.getEnd().getY();

        float denominator = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        if(denominator == 0){
            // Causes division by 0 which means line segments are collinear.
            // Return null since we can no longer calculate the t and u values
            return null;
        }

        float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        float u = -1 * (((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator);
        return new float[]{t, u};
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public int compareTo(Ray ray) {
        return (int) (Math.atan2(ray.end.getY() - start.getY(), ray.end.getX()- start.getX()) * 1000 - Math.atan2(end.getY()- start.getY(), end.getX()- start.getX()) * 1000);
    }

    @Override
    public void render() {
        Main.getInstance().line(start.getX(), start.getY(), end.getX(), end.getY());
    }
}

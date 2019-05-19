public class Ray implements Renderable{
    private Point start;
    private Point end;

    /**
     * Creates a ray given the start position and end position from the character.
     * @param start The character's location
     * @param end The end of the line
     */
    public Ray(Point start, Point end){
        this.start = start;
        this.end = end;

    }

    /**
     * Creates a ray given the start position and the degree of slope
     * @param start Start point of the ray
     * @param degreeOfSlope The arctan of the slope is the degree of the line
     */
    public Ray(Point start, double degreeOfSlope){
        this.start = start;

    }

    /**
     * Determines if this wall (using the start and end point) intersects a wall. If there is an intersection, it will
     * return the point of intersection. If there is no intersection, it return nulls
     * @param wall Wall to check if the current ray intersects
     * @return null if no intersection, a point instance if there is an intersection
     */
    public Point intersects(Wall wall){
        if(end == null) {
            throw new IllegalStateException("No end point determined");
        }

        float[] result = lineLineIntersectionValues(wall);
        // collinear lines means that we return the wall endpoint that is closest to the ray start point
        // the result is length 3 to differentiate from t and u values returned if the lines are no collinear
        if(result.length == 3){
            float distanceToWallStart = result[0];
            float distanceToWallEnd = result[1];
            return distanceToWallStart > distanceToWallEnd ? wall.getEnd() : wall.getStart();
        }

        float t = result[0];
        float u = result[1];
        if(t >= 0 && t <= 1 && u >= 0 && u <= 1){
            // there is an intersection
            float intersectX = (start.getX() + t * (end.getX() - start.getX()));
            float intersectY = (start.getY() + t * (end.getY() - start.getY()));
            return new Point(intersectX, intersectY);
        }

        // no intersection
        return null;
    }

    /**
     * Performs the line-line intersection algorithm.
     * Algorithm: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param wall Wall to check if there is an intersection
     * @return {t, u} if there is an intersection. {distanceToWallStart, distanceToWallEnd, 0} if the lines are collinear.
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
            float distanceToWallStart = (float) start.distancePortion(wall.getStart());
            float distanceToWallEnd = (float) start.distancePortion(wall.getEnd());
            return new float[]{distanceToWallStart, distanceToWallEnd, 0};
        }

        float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        float u = -1 * ((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator;
        return new float[]{t, u};
    }

    @Override
    public void render() {
        Main.getInstance().line(start.getX(), start.getY(), end.getX(), end.getY());
    }
}

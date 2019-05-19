public class Ray implements Renderable{
    private Point start;
    private Point end;
    private double degreeOfSlope;

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
     * Creates a ray given the start position and the slope (delta x / delta y)
     * @param start
     * @param slope
     */
    public Ray(Point start, double slope){
        // the arctan of the slope is the degree of the line
        this.start = start;
        this.degreeOfSlope = Math.atan(slope);
    }

    /**
     * Determines if this wall (using the start and end point) intersects a wall. If there is an intersection, it will
     * return the point of intersection. If there is no intersection, it return nulls
     * @param wall Wall to check if the current ray intersects
     * @return null if no intersection, a point instance if there is an intersection
     */
    public Point intersects(Wall wall){
        if(end == null){
            throw new IllegalStateException("No end point determined");
        }

        // Write out all of this so there is no mistake when implementing the formula
        // Algorithm: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
        float x1 = start.getX();
        float x2 = end.getX();
        float x3 = wall.getStart().getX();
        float x4 = wall.getEnd().getX();

        float y1 = start.getY();
        float y2 = end.getY();
        float y3 = wall.getStart().getY();
        float y4 = wall.getEnd().getY();

        float demoninator = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        if(demoninator == 0){
            // Causes division by 0 the line segments are collinear. Return the closest point
            double distanceToWallEnd = start.distancePortion(wall.getEnd());
            double distanceToWallStart = start.distancePortion(wall.getStart());
            return distanceToWallEnd > distanceToWallStart ? wall.getStart() : wall.getEnd();
        }

        float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / demoninator;
        float u = -1 * ((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / demoninator;
        if(t >= 0 && t <= 1 && u >= 0 && u <= 1){
            // there is an intersection
            float intersectX = (x1 + t * (x2 - x1));
            float intersectY = (y1 + t * (y2 - y1));
            return new Point(intersectX, intersectY);
        }else{
            return null;
        }
    }

    @Override
    public void render() {
        Main.getInstance().line(start.getX(), start.getY(), end.getX(), end.getY());
    }
}

public class Ray implements Comparable<Ray>{
    private final Point start;
    private Point end;
    private final boolean isMainRay;
    private Ray auxiliaryRay;

    public Ray(Point start, Point end, boolean isMainRay) {
        this.start = start;
        this.end = end;
        this.isMainRay = isMainRay;
    }

    /**
     * Determines if this wall (using the start and end point) intersects a wall. If there is an intersection, it will
     * return the point of intersection. If there is no intersection, it return nulls
     * @param wall Wall to check if the current ray intersects
     * @return null if no intersection, a point instance if there is an intersection
     */
    public Point intersects(Wall wall){
        float[] result = lineLineIntersectionValues(wall);
        // collinear lines means parallel lines, which have no intersection
        if(result == null){
            return null;
        }

        float t = result[0];
        float u = result[1];
        if(!isMainRay){
            if(t>= 0 && u <= 1 && u >= 0){
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

    public Point getStart(){
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Ray getAuxiliaryRay() {
        return auxiliaryRay;
    }

    public void setAuxiliaryRay(Ray auxiliaryRay) {
        this.auxiliaryRay = auxiliaryRay;
    }

    public void setEnd(Point end) {
        if(isMainRay){
            throw new IllegalStateException("Cannot reset end point for main ray");
        }
        this.end = end;
    }

    @Override
    public int compareTo(Ray ray) {
        return (int) (Math.atan2(ray.end.getY() - start.getY(), ray.end.getX()- start.getX()) * 1_000_000 - Math.atan2(end.getY()- start.getY(), end.getX()- start.getX()) * 1_000_000);
    }

    public boolean isMainRay(){
        return this.isMainRay;
    }
}

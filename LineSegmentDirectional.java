public class LineSegmentDirectional{
    private final Point start;
    private final Point end;
    private Directional.Horizontal horizontal;
    private Directional.Vertical vertical;

    public LineSegmentDirectional(Point start, Point end){
        this.start = start;
        this.end = end;

        // determine vertical direction
        if(start.getY() > end.getY()){
            this.vertical = Directional.Vertical.BOTTOM;
        }else if(start.getY() < end.getY()){
            this.vertical = Directional.Vertical.TOP;
        }else{
            this.vertical = Directional.Vertical.NONE;
        }

        // determine horizontal direction
        if(start.getX() > end.getX()){
            this.horizontal = Directional.Horizontal.LEFT;
        }else if(start.getX() < end.getX()){
            this.horizontal = Directional.Horizontal.RIGHT;
        }else{
            this.horizontal = Directional.Horizontal.NONE;
        }
    }

    public double slope(){
        return start.slope(end);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Directional.Horizontal getHorizontal() {
        return horizontal;
    }

    public Directional.Vertical getVertical() {
        return vertical;
    }
}